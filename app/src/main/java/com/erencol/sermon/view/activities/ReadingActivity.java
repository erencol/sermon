package com.erencol.sermon.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.erencol.sermon.Data.Service.manager.SharedPreferencesManager;
import com.erencol.sermon.model.Sermon;
import com.erencol.sermon.R;
import com.erencol.sermon.databinding.ActivityReadingBinding;
import com.erencol.sermon.viewmodelpkg.ReadingViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.util.Objects;

public class ReadingActivity extends AppCompatActivity {
    Sermon sermon;
    SharedPreferencesManager sharedPreferencesManager;
    ActivityReadingBinding activityReadingBinding;
    int fontSize = 8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReadingBinding = DataBindingUtil.setContentView(this, R.layout.activity_reading);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        setFontSize();
        getExtrasFromIntent();
        setToolbar(sermon.title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFontSize();
    }

    public void setFontSize(){
        fontSize = sharedPreferencesManager.getFontSize(sharedPreferencesManager.getDEFAULT_FONT_SIZE());
        activityReadingBinding.text.setTextSize(Float.parseFloat(String.valueOf(fontSize*6)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.aboutid) {
            showAboutActivity();
            return true;
        } else if (id == R.id.specialdaysid) {
            goToSpecialDaysActivity();
            return true;
        } else if (id == R.id.settingsid) {
            goToSettingsActivity();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void goToSpecialDaysActivity(){
        Intent i = new Intent(ReadingActivity.this, SpecialDaysActivity.class);
        startActivity(i);
    }

    public void goToSettingsActivity(){
        Intent i = new Intent(ReadingActivity.this,SettingsActivity.class);
        startActivity(i);
    }

    public void showAboutActivity(){
        Intent i = new Intent(ReadingActivity.this, AboutActivity.class);
        startActivity(i);
    }

    public void setToolbar(@NonNull String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(title);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
    }

    public void getExtrasFromIntent(){
        sermon = new Sermon();
        sermon = (Sermon) Objects.requireNonNull(getIntent().getExtras()).getSerializable("sermon");
        ReadingViewModel readingViewModel = new ReadingViewModel(sermon);
        activityReadingBinding.setSermonDetailViewModel(readingViewModel);
    }

}
