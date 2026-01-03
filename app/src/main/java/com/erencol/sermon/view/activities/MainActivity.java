package com.erencol.sermon.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryPurchasesParams;
import com.erencol.sermon.Data.service.manager.PremiumManager;
import com.erencol.sermon.view.adapters.SermonAdapter;
import com.erencol.sermon.databinding.ActivityMainBinding;
import com.erencol.sermon.viewmodelpkg.MainViewModel;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import com.erencol.sermon.R;

public class MainActivity extends AppCompatActivity implements Observer, PurchasesUpdatedListener {
    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;
    private BillingClient billingClient;
    private PremiumManager premiumManager;
    private SermonAdapter sermonAdapter;
    private static final String PREMIUM_SKU = "premium"; // Google Play Console'da tanımladığınız SKU ID'si
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initBillingClient();
        checkisPremium();
    }

    private void initBillingClient() {
        PendingPurchasesParams pendingPurchasesParams = PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build();
        
        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases(pendingPurchasesParams)
                .build();
        
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    queryPurchases();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Billing servisi bağlantısı kesildi, tekrar bağlanmayı dene
            }
        });
    }

    private void queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),
            (billingResult, purchases) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    handlePurchases(purchases);
                }
            }
        );
    }

    private void handlePurchases(List<Purchase> purchases) {
        boolean isPremium = false;
        for (Purchase purchase : purchases) {
            if (purchase.getProducts().contains(PREMIUM_SKU) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                isPremium = true;
                break;
            }
        }
        premiumManager.setPremium(isPremium);
        updateAdapterPremiumStatus();
    }

    public void checkisPremium(){
        // Premium durumu adapter'a geçirilecek
        updateAdapterPremiumStatus();
    }
    
    private void updateAdapterPremiumStatus() {
        if (sermonAdapter != null) {
            sermonAdapter.setPremium(premiumManager.isPremium());
        }
    }

    public void initDataBinding(){
        premiumManager = PremiumManager.getInstance(getApplicationContext());
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new MainViewModel();
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);
        setSupportActionBar(binding.toolbar);
        setListSermonListview(binding.sermonsRecyclerview);
        setupObserver(mainViewModel);
    }

    private void showPremiumDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Premium İçerik")
                .setMessage("Bu içerik sadece premium kullanıcılar için. Premium sürümü satın almak ister misiniz?")
                .setPositiveButton("Satın Al", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("İptal", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void setupObserver(Observable observable) {
        observable.addObserver(this);
    }
    private void setListSermonListview(RecyclerView listPeople) {
        sermonAdapter = new SermonAdapter();
        sermonAdapter.setPremium(premiumManager.isPremium());
        listPeople.setAdapter(sermonAdapter);
        listPeople.setLayoutManager(new LinearLayoutManager(this));
        mainViewModel.getSermons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aboutid) {
            showPremiumDialog();
            //showAboutActivity();
            return true;
        } else if (id == R.id.specialdaysid) {
            goToSpecialDaysActivity();
            return true;
        } else if (id == R.id.settingsid) {
            goToSettingsActivity();
            return true;
        } /* else if (id == R.id.qiblaId) {
            goToQibla();
            return true;
        }*/
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void goToSpecialDaysActivity(){
        Intent i = new Intent(MainActivity.this, SpecialDaysActivity.class);
        startActivity(i);
    }

    public void goToSettingsActivity(){
        Intent i = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(i);
    }

    public void showAboutActivity(){
        Intent i = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(i);
    }

    public void goToQibla(){
        Intent i = new Intent(MainActivity.this, CompassActivity.class);
        startActivity(i);
    }

    @Override public void update(Observable observable, Object data) {
        if (observable instanceof MainViewModel mainViewModel) {
            SermonAdapter adapter = (SermonAdapter) binding.sermonsRecyclerview.getAdapter();
            assert adapter != null;
            adapter.setSermonList(mainViewModel.getSermonList().getValue());
            adapter.setPremium(premiumManager.isPremium());
        }
    }
    
    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Her activity'ye dönüldüğünde premium durumunu kontrol et
        if (billingClient != null && billingClient.isReady()) {
            queryPurchases();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billingClient != null) {
            billingClient.endConnection();
        }
    }


}
