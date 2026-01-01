package com.erencol.sermon.view.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.erencol.sermon.model.SpecialDay;
import com.erencol.sermon.R;
import com.erencol.sermon.view.adapters.ViewHolders.SpecialDayViewHolder;
import com.erencol.sermon.databinding.SpecialDaysCellBinding;

import java.util.Collections;
import java.util.List;

public class SpecialDayAdapter extends RecyclerView.Adapter<SpecialDayViewHolder> {
    private List<SpecialDay> specialDays;
    private OnItemClickListener listener;

    // Click listener interface
    public interface OnItemClickListener {
        void onItemClick(SpecialDay specialDay, int position);
    }

    public SpecialDayAdapter() {
        this.specialDays = Collections.emptyList();
    }

    // Click listener'ı set etmek için method
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpecialDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SpecialDaysCellBinding sermonCellBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.special_days_cell, parent, false);
        return new SpecialDayViewHolder(sermonCellBinding);
    }

    @Override
    public void onBindViewHolder(final SpecialDayViewHolder holder, final int position) {
        SpecialDay specialDay = specialDays.get(position);
        holder.bindSpecialDay(specialDay);

        // Item'a click event ekleme
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(specialDay, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(specialDays != null)
            return specialDays.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSpecialDays(List<SpecialDay> specialDays) {
        this.specialDays = specialDays;
        notifyDataSetChanged();
    }
}