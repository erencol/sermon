package com.erencol.sermon.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.erencol.sermon.fcm.FirebaseTopicManager

class BillingManager(
    private val context: Context,
    private val firebaseTopicManager: FirebaseTopicManager
) : PurchasesUpdatedListener {

    private val _isPremium = MutableLiveData(false)
    val isPremium: LiveData<Boolean> = _isPremium

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    companion object {
        private const val PREMIUM_SKU_ID = "premium_sermon_access"
        private const val TAG = "BillingManager"
    }

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing Setup Finished")
                    queryPurchases()
                } else {
                    Log.e(TAG, "Billing Setup Failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Billing Service Disconnected")
                // Retry logic can be implemented here
            }
        })
    }

    fun queryPurchases() {
        if (!billingClient.isReady) {
            Log.e(TAG, "Billing Client not ready")
            return
        }

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                processPurchases(purchases)
            } else {
                Log.e(TAG, "Query Purchases process failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun processPurchases(purchases: List<Purchase>) {
        var hasPremium = false
        for (purchase in purchases) {
            if (purchase.products.contains(PREMIUM_SKU_ID) && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                hasPremium = true
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
                break
            }
        }
        
        // Premium durumu değiştiyse FCM topic aboneliğini güncelle
        val wasPremium = _isPremium.value ?: false
        if (hasPremium && !wasPremium) {
            // Yeni premium kullanıcı - bildirim kanalına abone ol
            Log.d(TAG, "New premium user detected - subscribing to FCM topic")
            firebaseTopicManager.subscribeToSermonsTopic()
        } else if (!hasPremium && wasPremium) {
            // Premium iptal edildi - bildirim kanalından çık
            Log.d(TAG, "Premium cancelled - unsubscribing from FCM topic")
            firebaseTopicManager.unsubscribeFromSermonsTopic()
        }
        
        _isPremium.postValue(hasPremium)
        Log.d(TAG, "Is Premium: $hasPremium")
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(params) { billingResult ->
             if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                 Log.d(TAG, "Purchase Acknowledged")
             }
        }
    }

    fun launchPurchaseFlow(activity: Activity) {
        if (!billingClient.isReady) {
            Log.e(TAG, "Billing Client not ready")
            activity.runOnUiThread {
                android.widget.Toast.makeText(context, "Ödeme sistemi hazır değil. Lütfen tekrar deneyin.", android.widget.Toast.LENGTH_LONG).show()
            }
            return
        }

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PREMIUM_SKU_ID)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            Log.d(TAG, "Query Product Details Response Code: ${billingResult.responseCode}, Debug Message: ${billingResult.debugMessage}")
            Log.d(TAG, "Product Details List Size: ${productDetailsList.size}")
            
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]
                Log.d(TAG, "Product Found: ${productDetails.name}, ID: ${productDetails.productId}, Type: ${productDetails.productType}")
                
                val offerDetails = productDetails.subscriptionOfferDetails
                if (offerDetails.isNullOrEmpty()) {
                    Log.e(TAG, "No offer details found for subscription")
                    activity.runOnUiThread {
                        android.widget.Toast.makeText(context, "Abonelik planı bulunamadı. Temel Plan (Base Plan) tanımladığınızdan emin olun.", android.widget.Toast.LENGTH_LONG).show()
                    }
                    return@queryProductDetailsAsync
                }

                val offerToken = offerDetails.first().offerToken
                Log.d(TAG, "Offer Token Found: $offerToken")

                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build()
                )

                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                
                billingClient.launchBillingFlow(activity, billingFlowParams)
            } else {
                 Log.e(TAG, "Product Details not found or error. Response Code: ${billingResult.responseCode}, Debug Msg: ${billingResult.debugMessage}, List Empty: ${productDetailsList.isEmpty()}")
                 activity.runOnUiThread {
                     val errorMessage = "Hata Kodu: ${billingResult.responseCode}\nListe Boş mu: ${productDetailsList.isEmpty()}\nMesaj: ${billingResult.debugMessage}"
                     android.widget.Toast.makeText(activity, "Ürün bilgisi alınamadı.\n$errorMessage", android.widget.Toast.LENGTH_LONG).show()
                 }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            processPurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "User canceled purchase")
        } else {
            Log.e(TAG, "Purchase Error: ${billingResult.debugMessage}")
        }
    }
}
