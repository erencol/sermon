package com.erencol.sermon.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.erencol.sermon.R
import com.erencol.sermon.databinding.ActivityMainBinding
import com.erencol.sermon.view.about.AboutActivity
import com.erencol.sermon.view.settings.SettingsActivity
import com.erencol.sermon.view.specialdays.SpecialDaysActivity
import com.erencol.sermon.view.main.SermonAdapter
import com.erencol.sermon.view.main.MainViewModel
import java.util.Observable
import java.util.Observer

class MainActivity : AppCompatActivity(), Observer {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

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
        setListSermonListview()
        setupObserver(mainViewModel)
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