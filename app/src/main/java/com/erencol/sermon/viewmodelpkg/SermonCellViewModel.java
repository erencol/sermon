package com.erencol.sermon.viewmodelpkg;


import android.content.Intent;
import android.net.Uri;
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
import com.erencol.sermon.Data.service.manager.PremiumManager;
import com.erencol.sermon.view.activities.ReadingActivity;

public class SermonCellViewModel extends BaseObservable {
    private Sermon sermon;
    private boolean isLocked;
    private int position;
    SermonApplication sa;
    PremiumManager premiumManager;

    public SermonCellViewModel(Sermon sermon) {
        this(sermon, false, 0);
    }

    public SermonCellViewModel(Sermon sermon, boolean isLocked, int position) {
        this.sermon = sermon;
        this.isLocked = isLocked;
        this.position = position;
        if(sa==null)
            sa = new SermonApplication();
        premiumManager = PremiumManager.getInstance(sa.getAppContext());
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

    public int getPremiumBadgeVisibility(){
        if(isLocked)
            return View.VISIBLE;
        else
            return View.GONE;
    }

    public void onItemClick(View view){
        // Premium kontrolü - eğer kilitliyse ve premium değilse, satın alma sayfasına yönlendir
        if(isLocked && !premiumManager.isPremium()) {
            openPremiumPurchase();
            return;
        }
        
        Bundle bundle = new Bundle();
        bundle.putSerializable("sermon", sermon);
        bundle.putInt("sermon_position", position);
        Intent goToSermonDetail = new Intent(view.getContext(), ReadingActivity.class);
        goToSermonDetail.putExtras(bundle);
        view.getContext().startActivity(goToSermonDetail);
    }

    private void openPremiumPurchase() {
        // Google Play Store'da uygulamanın premium satın alma sayfasına yönlendir
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + sa.getAppContext().getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sa.getAppContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter("imageUrl") public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url) .transition(GenericTransitionOptions.with(R.anim.fade)).into(imageView);
    }
    
    public void setSermon(Sermon sermon){
        setSermon(sermon, false, 0);
    }

    public void setSermon(Sermon sermon, boolean isLocked, int position){
        this.sermon = sermon;
        this.isLocked = isLocked;
        this.position = position;
        notifyChange();
    }
}
