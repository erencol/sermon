package com.erencol.sermon.view.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.erencol.sermon.Data.Service.manager.SharedPreferencesManager
import com.erencol.sermon.R
import com.erencol.sermon.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    lateinit var settingsBinding: ActivitySettingsBinding
    lateinit var seekBar: SeekBar
    lateinit var seekText: TextView
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    var seekBarValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        seekBar = settingsBinding.fontSelectionSeekbar
        seekText = settingsBinding.seekbarProgressText
        setToolBar()
        sharedPreferencesManager = SharedPreferencesManager(applicationContext)
        seekBarValue = sharedPreferencesManager.DEFAULT_FONT_SIZE
        seekText.text = getFontsize().toString()
        seekBar.progress =  Integer.parseInt(seekText.text.toString())
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekText.text = progress.toString()
                seekBarValue = progress
                sharedPreferencesManager.putFontSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    fun getFontsize(): Int {
        return if(sharedPreferencesManager.getFontSize(0) == 0) {
            sharedPreferencesManager.DEFAULT_FONT_SIZE
        } else {
            sharedPreferencesManager.getFontSize(0)
        }
    }

    fun setToolBar (){
        setSupportActionBar(settingsBinding.toolbar as Toolbar?)
        supportActionBar?.title = resources.getString(R.string.settings_menu_item)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            super.onBackPressed()
            return true
        } else {
            return  super.onOptionsItemSelected(item)
        }
    }
}