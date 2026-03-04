package com.erencol.sermon.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.erencol.sermon.R
import com.erencol.sermon.data.manager.SharedPreferencesManager
import com.erencol.sermon.databinding.ActivityMainBinding
import com.erencol.sermon.fcm.FirebaseTopicManager
import com.erencol.sermon.view.about.AboutActivity
import com.erencol.sermon.view.settings.SettingsActivity
import com.erencol.sermon.view.specialdays.SpecialDaysActivity
import java.util.Observable
import java.util.Observer

class MainActivity : AppCompatActivity(), Observer {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var billingManager: com.erencol.sermon.billing.BillingManager
    private lateinit var firebaseTopicManager: FirebaseTopicManager

    companion object {
        private const val TAG = "MainActivity"
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        checkRateUsDialog()
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = MainViewModel()
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        
        setupBilling()
        setupFirebaseMessaging()
        setListSermonListview()
        setupObserver(mainViewModel)
        setupFab()
    }

    private fun setupFab() {
        binding.fabPremium.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.premium_popup_title))
                .setMessage(getString(R.string.premium_popup_message))
                .setPositiveButton(getString(R.string.analyze)) { _, _ ->
                    billingManager.launchPurchaseFlow(this)
                }
                .setNegativeButton(getString(R.string.later), null)
                .show()
        }
    }

    private fun setupBilling() {
        // FirebaseTopicManager'ı önce oluştur
        firebaseTopicManager = FirebaseTopicManager(this)
        
        // BillingManager'a FirebaseTopicManager'ı geç
        billingManager = com.erencol.sermon.billing.BillingManager(this, firebaseTopicManager)
        billingManager.startConnection()
        billingManager.isPremium.observe(this, { isPremium ->
            val adapter = binding.sermonsRecyclerview.adapter as? SermonAdapter
            adapter?.isPremium = isPremium
            binding.fabPremium.visibility = if (isPremium) View.GONE else View.VISIBLE
        })
    }

    private fun setupFirebaseMessaging() {
        // Android 13+ için bildirim izni kontrolü
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            } else {
                Log.d(TAG, "Notification permission already granted")
            }
        } else {
            // Android 13'ten önceki sürümler için izin gerekmiyor
            Log.d(TAG, "Notification permission not required for this Android version")
        }
        
        // FCM Token'ı logla (debug için)
        firebaseTopicManager.getFcmToken { token ->
            Log.d(TAG, "FCM Token: $token")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted")
            } else {
                Log.d(TAG, "Notification permission denied")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun setupObserver(observable: Observable) {
        observable.addObserver(this)
    }

    private fun setListSermonListview() {
        val adapter = SermonAdapter()
        adapter.onPremiumContentClick = {
             androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.premium_popup_title))
                .setMessage(getString(R.string.premium_popup_message))
                .setPositiveButton(getString(R.string.analyze)) { _, _ ->
                    billingManager.launchPurchaseFlow(this)
                }
                .setNegativeButton(getString(R.string.later), null)
                .show()
        }
        binding.sermonsRecyclerview.adapter = adapter
        binding.sermonsRecyclerview.layoutManager = LinearLayoutManager(this)
        mainViewModel.getSermons()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.aboutid) {
            showAboutActivity()
            return true
        } else if (id == R.id.specialdaysid) {
            goToSpecialDaysActivity()
            return true;
        } else if (id == R.id.settingsid) {
            goToSettingsActivity()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun goToSpecialDaysActivity() {
        val i = Intent(this@MainActivity, SpecialDaysActivity::class.java)
        startActivity(i)
    }

    private fun goToSettingsActivity() {
        val i = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(i)
    }

    private fun showAboutActivity() {
        val i = Intent(this@MainActivity, AboutActivity::class.java)
        startActivity(i)
    }

    override fun update(observable: Observable, data: Any?) {
        if (observable is MainViewModel) {
            val sermonAdapter = binding.sermonsRecyclerview.adapter as SermonAdapter?
            sermonAdapter?.setSermonList(observable.sermonList.value)
        }
    }

    private fun checkRateUsDialog() {
        val prefs = SharedPreferencesManager.getInstance(this)
        val currentVersionCode = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, 0).longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0).versionCode
            }
        } catch (e: Exception) {
            0
        }

        val lastVersionCode = prefs.getIntForKey(SharedPreferencesManager.KEY_LAST_VERSION_CODE, 0)
        val launchCount = prefs.getIntForKey(SharedPreferencesManager.KEY_LAUNCH_COUNT, 0)
        val shownKey = SharedPreferencesManager.KEY_RATE_SHOWN_PREFIX + currentVersionCode
        val isShownForThisVersion = prefs.getBoolean(shownKey, false)

        if (isShownForThisVersion) return

        if (lastVersionCode == 0) {
            // First time install initialization
            prefs.putIntForKey(SharedPreferencesManager.KEY_LAST_VERSION_CODE, currentVersionCode)
            prefs.putIntForKey(SharedPreferencesManager.KEY_LAUNCH_COUNT, 1)
        } else if (lastVersionCode == currentVersionCode) {
            // Ongoing session for the same version
            val newCount = launchCount + 1
            prefs.putIntForKey(SharedPreferencesManager.KEY_LAUNCH_COUNT, newCount)
            if (newCount == 3) {
                showRateUsDialog(shownKey)
            }
        } else if (lastVersionCode < currentVersionCode) {
            // Updated user
            showRateUsDialog(shownKey)
            prefs.putIntForKey(SharedPreferencesManager.KEY_LAST_VERSION_CODE, currentVersionCode)
        }
    }

    private fun showRateUsDialog(shownKey: String) {
        val view = layoutInflater.inflate(R.layout.dialog_rate_us, null)
        val textView = view.findViewById<android.widget.TextView>(R.id.textViewMessage)
        textView.text = getString(R.string.rate_us_message, getString(R.string.app_name))

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                SharedPreferencesManager.getInstance(this).putBoolean(shownKey, true)
                openPlayStore()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                SharedPreferencesManager.getInstance(this).putBoolean(shownKey, true)
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun openPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=$packageName")
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            }
            startActivity(webIntent)
        }
    }
}