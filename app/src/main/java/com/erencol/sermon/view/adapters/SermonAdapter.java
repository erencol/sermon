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

    public SermonAdapter() {
        this.sermonList = Collections.emptyList();
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
        holder.bindSermon(sermonList.get(position));
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
