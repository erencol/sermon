package com.erencol.sermon.view.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.erencol.sermon.R;
import com.erencol.sermon.model.Sermon;
import com.erencol.sermon.view.adapters.ViewHolders.SermonViewHolder;
import com.erencol.sermon.databinding.SermonCellBinding;
import java.util.Collections;
import java.util.List;

public class SermonAdapter extends RecyclerView.Adapter<SermonViewHolder> {
    private List<Sermon> sermonList;
    private boolean isPremium;
    private static final int FREE_SERMON_LIMIT = 5;

    public SermonAdapter() {
        this.sermonList = Collections.emptyList();
        this.isPremium = false;
    }

    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
        notifyDataSetChanged();
    }

    @Override
    public SermonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SermonCellBinding sermonCellBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.sermon_cell, parent, false);
        return new SermonViewHolder(sermonCellBinding);
    }

    @Override
    public void onBindViewHolder(final SermonViewHolder holder, final int position) {
        Sermon sermon = sermonList.get(position);
        boolean isLocked = !isPremium && position >= FREE_SERMON_LIMIT;
        holder.bindSermon(sermon, isLocked, position);

    }

    @Override
    public int getItemCount() {
        if(sermonList!=null)
            return sermonList.size();
        else
            return 0;
    }
    public void setSermonList(List<Sermon> sermonList) {
        this.sermonList = sermonList;
        notifyDataSetChanged();
    }

}
