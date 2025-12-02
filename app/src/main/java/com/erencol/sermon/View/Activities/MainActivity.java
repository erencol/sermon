package com.erencol.sermon.View.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.erencol.sermon.View.Adapters.SermonAdapter;
import com.erencol.sermon.databinding.ActivityMainBinding;
import com.erencol.sermon.viewmodelpkg.MainViewModel;
import java.util.Observable;
import java.util.Observer;
import com.erencol.sermon.R;

public class MainActivity extends AppCompatActivity implements Observer {
    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
    }

    public void initDataBinding(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new MainViewModel();
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);
        setSupportActionBar(binding.toolbar);
        setupListPeopleView(binding.sermonsRecyclerview);
        setupObserver(mainViewModel);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void setupObserver(Observable observable) {
        observable.addObserver(this);
    }
    private void setupListPeopleView(RecyclerView listPeople) {
        SermonAdapter adapter = new SermonAdapter();
        listPeople.setAdapter(adapter);
        listPeople.setLayoutManager(new LinearLayoutManager(this));
        mainViewModel.getSermons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aboutid) {
            showAboutActivity();
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
        Intent i = new Intent(MainActivity.this,SpecialDays.class);
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
            SermonAdapter sermonAdapter = (SermonAdapter) binding.sermonsRecyclerview.getAdapter();
            assert sermonAdapter != null;
            sermonAdapter.setSermonList(mainViewModel.getSermonList().getValue());
        }
    }


}
