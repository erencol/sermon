package com.erencol.sermon.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.erencol.sermon.R
import com.erencol.sermon.databinding.SermonCellBinding
import com.erencol.sermon.model.Sermon

class SermonAdapter : RecyclerView.Adapter<SermonViewHolder>() {
    private var sermonList: List<Sermon> = emptyList()
    var isPremium: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onPremiumContentClick: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SermonViewHolder {
        val sermonCellBinding = DataBindingUtil.inflate<SermonCellBinding>(
            LayoutInflater.from(parent.context),
            R.layout.sermon_cell, parent, false
        )
        return SermonViewHolder(sermonCellBinding)
    }

    override fun onBindViewHolder(holder: SermonViewHolder, position: Int) {
        val isLocked = !isPremium && position >= 5
        holder.bindSermon(sermonList[position], isLocked, onPremiumContentClick)
    }

    override fun getItemCount(): Int {
        return sermonList.size
    }

    fun setSermonList(sermonList: List<Sermon>?) {
        this.sermonList = sermonList ?: emptyList()
        notifyDataSetChanged()
    }
}