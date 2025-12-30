package com.erencol.sermon.viewmodelpkg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.erencol.sermon.model.Sermon;
import com.erencol.sermon.R;
import com.erencol.sermon.SermonApplication;
import com.erencol.sermon.view.activities.ReadingActivity;

public class SermonCellViewModel extends BaseObservable {
    private Sermon sermon;
    SermonApplication sa;

    public SermonCellViewModel(Sermon sermon) {
        this.sermon = sermon;
        if(sa==null)
            sa = new SermonApplication();
    }

    public String getTitle(){
        return sermon.getTitle();
    }

    public String getShortText (){
        return sermon.getShortText();
    }

    public String getImageUrl(){
        return sermon.getImageUrl();
    }

    public String getDate(){return sermon.getDate(); }

    public int getNew(){
       if(sermon.getNew())
           return View.VISIBLE;
       else
           return View.GONE;
    }

    public void onItemClick(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("sermon", sermon);
        Intent goToSermonDetail = new Intent(sa.getAppContext(), ReadingActivity.class);
        goToSermonDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        goToSermonDetail.putExtras(bundle);
        sa.getAppContext().startActivity(goToSermonDetail);
    }

    @BindingAdapter("imageUrl") public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url) .transition(GenericTransitionOptions.with(R.anim.fade)).into(imageView);
    }
    public void setSermon(Sermon sermon){
        this.sermon = sermon;
        notifyChange();
    }
}
