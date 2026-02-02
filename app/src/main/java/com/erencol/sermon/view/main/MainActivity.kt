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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.erencol.sermon.R
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
}