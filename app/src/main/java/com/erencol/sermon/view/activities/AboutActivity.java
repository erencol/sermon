package com.erencol.sermon.view.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.erencol.sermon.R;
import com.erencol.sermon.databinding.ActivityAboutBinding;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding activityAboutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        setToolbar(getResources().getString(R.string.sermons));
    }

    public void setToolbar(@NonNull String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(title);
        this.setTitle(title);
    }

    public void drawingClick(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mobizoe.drawing")));
    }

    public void voiceNotesClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mobizoe.senote")));
    }

    public void copy(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Hutbeler Iban", getResources().getString(R.string.iban_copy_text));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(),"Kopyalandı.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}