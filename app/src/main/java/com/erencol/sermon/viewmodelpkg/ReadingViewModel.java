package com.erencol.sermon.viewmodelpkg;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModel;
import com.erencol.sermon.model.Sermon;
import com.erencol.sermon.R;

public class ReadingViewModel extends ViewModel {
    private final Sermon sermon;

    public ReadingViewModel(Sermon sermon){
        this.sermon = sermon;
    }

    public String getPictureSermon(){
        return sermon.getImageUrl();
    }

    public String getSermonText(){
        return sermon.getText();
    }

    public void share(View view){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = sermon.getText();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, view.getContext().getResources().getString(R.string.app_name)+" uygulaması ile paylaşıyorum.");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        view.getContext().startActivity(Intent.createChooser(sharingIntent, "Şununla paylaş"));

    }
}
