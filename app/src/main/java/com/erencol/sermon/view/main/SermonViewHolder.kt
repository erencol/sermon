package com.erencol.sermon.view.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erencol.sermon.databinding.SermonCellBinding
import com.erencol.sermon.model.Sermon
import com.erencol.sermon.view.main.SermonCellViewModel

class SermonViewHolder(private val sermonCellBinding: SermonCellBinding) :
    RecyclerView.ViewHolder(sermonCellBinding.itemSermon) {
    var view: View? = null
    fun bindSermon(sermon: Sermon, isLocked: Boolean, onLockedClick: (() -> Unit)?) {
        if (sermonCellBinding.sermonCellViewModel == null) {
            sermonCellBinding.sermonCellViewModel = SermonCellViewModel(sermon)
        } else {
            sermonCellBinding.sermonCellViewModel!!.setSermonData(sermon)
        }
        sermonCellBinding.sermonCellViewModel!!.isLocked = isLocked
        sermonCellBinding.sermonCellViewModel!!.onLockedClick = onLockedClick

        if (sermon.isNew == true) {
            sermonCellBinding.newAlert.visibility = View.VISIBLE
        } else {
            sermonCellBinding.newAlert.visibility = View.GONE
        }
    }
}