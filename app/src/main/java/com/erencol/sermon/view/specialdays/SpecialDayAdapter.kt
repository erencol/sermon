package com.erencol.sermon.view.specialdays

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.erencol.sermon.R
import com.erencol.sermon.databinding.SpecialDaysCellBinding
import com.erencol.sermon.model.SpecialDay
import com.erencol.sermon.view.specialdays.SpecialDayViewHolder

class SpecialDayAdapter : RecyclerView.Adapter<SpecialDayViewHolder>() {
    private var specialDays: List<SpecialDay> = emptyList()
    private var listener: OnItemClickListener? = null

    // Click listener interface
    interface OnItemClickListener {
        fun onItemClick(specialDay: SpecialDay, position: Int)
    }

    // Method to set click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // Support kotlin lambda style
    fun setOnItemClickListener(listener: (SpecialDay, Int) -> Unit) {
        this.listener = object : OnItemClickListener {
            override fun onItemClick(specialDay: SpecialDay, position: Int) {
                listener(specialDay, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialDayViewHolder {
        val specialDaysCellBinding = DataBindingUtil.inflate<SpecialDaysCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.special_days_cell, parent, false
        )
        return SpecialDayViewHolder(specialDaysCellBinding)
    }

    override fun onBindViewHolder(holder: SpecialDayViewHolder, position: Int) {
        val specialDay = specialDays[position]
        holder.bindSpecialDay(specialDay)

        // Add item click event
        holder.itemView.setOnClickListener {
            listener?.onItemClick(specialDay, position)
        }
    }

    override fun getItemCount(): Int {
        return specialDays.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSpecialDays(specialDays: List<SpecialDay>?) {
        this.specialDays = specialDays ?: emptyList()
        notifyDataSetChanged()
    }
}