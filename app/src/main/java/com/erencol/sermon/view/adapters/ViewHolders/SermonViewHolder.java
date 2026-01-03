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
    public View view;

    public SermonViewHolder(SermonCellBinding sermonCellBinding) {
        super(sermonCellBinding.itemSermon);
        this.sermonCellBinding = sermonCellBinding;
    }

    public void bindSermon(Sermon sermon) {
        bindSermon(sermon, false, 0);
    }

    public void bindSermon(Sermon sermon, boolean isLocked, int position) {
        if (sermonCellBinding.getSermonCellViewModel() == null) {
            sermonCellBinding.setSermonCellViewModel(
                    new SermonCellViewModel(sermon, isLocked, position));
        } else {
            sermonCellBinding.getSermonCellViewModel().setSermon(sermon, isLocked, position);
        }
        if(sermon.isNew)
            sermonCellBinding.newAlert.setVisibility(View.VISIBLE);
        else
            sermonCellBinding.newAlert.setVisibility(View.GONE);

    }

}
