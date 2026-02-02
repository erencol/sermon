package com.erencol.sermon.fcm

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import androidx.core.content.edit

class FirebaseTopicManager(private val context: Context) {

    companion object {
        private const val TAG = "FirebaseTopicManager"
        private const val PREFS_NAME = "sermon_fcm_prefs"
        private const val KEY_SUBSCRIBED = "is_subscribed_to_sermons"
        const val TOPIC_SERMONS = "sermons"
    }

    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Kullanıcıyı "sermons" topic'ine abone eder
     */
    fun subscribeToSermonsTopic() {
        if (isSubscribed()) {
            return
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_SERMONS)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveSubscriptionStatus(true)
                } else {
                    Log.e(TAG, "Failed to subscribe to sermons topic", task.exception)
                }
            }
    }

    /**
     * Kullanıcının topic aboneliğini iptal eder
     */
    fun unsubscribeFromSermonsTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_SERMONS)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveSubscriptionStatus(false)
                } else {
                    Log.e(TAG, "Failed to unsubscribe from sermons topic", task.exception)
                }
            }
    }

    /**
     * Kullanıcının abone olup olmadığını kontrol eder
     */
    fun isSubscribed(): Boolean {
        return prefs.getBoolean(KEY_SUBSCRIBED, false)
    }

    /**
     * FCM Token'ı alır
     */
    fun getFcmToken(onTokenReceived: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                onTokenReceived(token)
            } else {
                Log.e(TAG, "Failed to get FCM token", task.exception)
            }
        }
    }

    private fun saveSubscriptionStatus(isSubscribed: Boolean) {
        prefs.edit { putBoolean(KEY_SUBSCRIBED, isSubscribed) }
    }
}
