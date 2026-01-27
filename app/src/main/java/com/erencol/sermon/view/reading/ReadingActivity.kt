package com.erencol.sermon.view.reading

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.erencol.sermon.R
import com.erencol.sermon.data.manager.SharedPreferencesManager
import com.erencol.sermon.databinding.ActivityReadingBinding
import com.erencol.sermon.model.Sermon
import com.erencol.sermon.view.about.AboutActivity
import com.erencol.sermon.view.settings.SettingsActivity
import com.erencol.sermon.view.specialdays.SpecialDaysActivity
import com.google.android.material.appbar.CollapsingToolbarLayout

class ReadingActivity : AppCompatActivity() {
    private var sermon: Sermon? = null
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var activityReadingBinding: ActivityReadingBinding
    private var fontSize = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReadingBinding = DataBindingUtil.setContentView(this, R.layout.activity_reading)
        sharedPreferencesManager = SharedPreferencesManager(applicationContext)
        getExtrasFromIntent()
        setFontSize()
        sermon?.title?.let { setToolbar(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        setFontSize()
    }

    private fun setFontSize() {
        fontSize = sharedPreferencesManager.getFontSize(sharedPreferencesManager.DEFAULT_FONT_SIZE)
        activityReadingBinding.text.textSize = (fontSize * 6).toFloat()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        } else if (id == R.id.aboutid) {
            showAboutActivity()
            return true
        } else if (id == R.id.specialdaysid) {
            goToSpecialDaysActivity()
            return true
        } else if (id == R.id.settingsid) {
            goToSettingsActivity()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun goToSpecialDaysActivity() {
        val i = Intent(this@ReadingActivity, SpecialDaysActivity::class.java)
        startActivity(i)
    }

    private fun goToSettingsActivity() {
        val i = Intent(this@ReadingActivity, SettingsActivity::class.java)
        startActivity(i)
    }

    private fun showAboutActivity() {
        val i = Intent(this@ReadingActivity, AboutActivity::class.java)
        startActivity(i)
    }

    private fun setToolbar(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout)
        collapsingToolbar.title = title
        collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
        collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
    }

    private fun getExtrasFromIntent() {
        sermon = intent.getSerializableExtra("sermon") as Sermon?
        if (sermon != null) {
            val readingViewModel = ReadingViewModel(sermon!!)
            activityReadingBinding.sermonDetailViewModel = readingViewModel
        }
    }
}