package com.erencol.sermon.view.adapters.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.erencol.sermon.model.Sermon;
import com.erencol.sermon.databinding.SermonCellBinding;
import com.erencol.sermon.viewmodelpkg.SermonCellViewModel;

public class SermonViewHolder extends RecyclerView.ViewHolder {
    SermonCellBinding sermonCellBinding;

    public TextView sermonTitle,sermonShortText,sermonDate;
    public View view;
    public ImageView sermonImage;

    public SermonViewHolder(SermonCellBinding sermonCellBinding) {
        super(sermonCellBinding.itemSermon);
        this.sermonCellBinding = sermonCellBinding;
    }

    public void bindSermon(Sermon sermon) {
        if (sermonCellBinding.getSermonCellViewModel() == null) {
            sermonCellBinding.setSermonCellViewModel(
                    new SermonCellViewModel(sermon));
            if(sermon.isNew)
                sermonCellBinding.newAlert.setVisibility(View.VISIBLE);
            else
                sermonCellBinding.newAlert.setVisibility(View.GONE);
        } else {
            sermonCellBinding.getSermonCellViewModel().setSermon(sermon);
            if(sermon.isNew)
                sermonCellBinding.newAlert.setVisibility(View.VISIBLE);
            else
                sermonCellBinding.newAlert.setVisibility(View.GONE);
        }
    }

}
