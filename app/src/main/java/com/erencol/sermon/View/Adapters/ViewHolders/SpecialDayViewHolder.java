package com.erencol.sermon.View.Adapters.ViewHolders;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.erencol.sermon.Model.SpecialDay;
import com.erencol.sermon.databinding.SpecialDaysCellBinding;
import com.erencol.sermon.viewmodelpkg.SpecialDaysCellViewModel;

public class SpecialDayViewHolder extends RecyclerView.ViewHolder {
    SpecialDaysCellBinding specialDaysCellBinding;

    public SpecialDayViewHolder(SpecialDaysCellBinding specialDaysCellBinding) {
        super(specialDaysCellBinding.itemSermon);
        this.specialDaysCellBinding = specialDaysCellBinding;
    }

    public void bindSpecialDay(SpecialDay specialDay) {
        specialDaysCellBinding.setSpecialDaysCellViewModel(new SpecialDaysCellViewModel(specialDay));

    }

}
