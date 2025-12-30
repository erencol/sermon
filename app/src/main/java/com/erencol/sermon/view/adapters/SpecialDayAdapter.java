package com.erencol.sermon.view.adapters;

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

    public SpecialDayAdapter() {
        this.specialDays = Collections.emptyList();
    }

    @Override
    public SpecialDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SpecialDaysCellBinding sermonCellBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.special_days_cell, parent, false);
        return new SpecialDayViewHolder(sermonCellBinding);
    }

    @Override
    public void onBindViewHolder(final SpecialDayViewHolder holder, final int position) {
        holder.bindSpecialDay(specialDays.get(position));
    }

    @Override
    public int getItemCount() {
        if(specialDays!=null)
            return specialDays.size();
        else
            return 0;
    }
    public void setSpecialDays(List<SpecialDay> specialDays) {
        this.specialDays = specialDays;
        notifyDataSetChanged();
    }


}
