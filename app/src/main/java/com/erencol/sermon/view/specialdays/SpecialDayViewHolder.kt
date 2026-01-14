package com.erencol.sermon.view.specialdays

import androidx.recyclerview.widget.RecyclerView
import com.erencol.sermon.databinding.SpecialDaysCellBinding
import com.erencol.sermon.model.SpecialDay
import com.erencol.sermon.view.specialdays.SpecialDaysCellViewModel

class SpecialDayViewHolder(private val specialDaysCellBinding: SpecialDaysCellBinding) :
    RecyclerView.ViewHolder(specialDaysCellBinding.itemSermon) {

    fun bindSpecialDay(specialDay: SpecialDay) {
        specialDaysCellBinding.specialDaysCellViewModel = SpecialDaysCellViewModel(specialDay)
    }
}