package com.erencol.sermon.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.erencol.sermon.R
import com.erencol.sermon.model.Sermon
import com.erencol.sermon.view.reading.ReadingActivity

class SermonCellViewModel(var sermon: Sermon) : BaseObservable() {

    val title: String?
        get() = sermon.title

    val shortText: String?
        get() = sermon.shortText

    val imageUrl: String?
        get() = sermon.imageUrl

    val date: String?
        get() = sermon.date

    val isNew: Int
        get() = if (sermon.isNew == true) View.VISIBLE else View.GONE

    var isLocked: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }

    val premiumBadgeVisibility: Int
        get() = if (isLocked) View.VISIBLE else View.GONE

    var onLockedClick: (() -> Unit)? = null

    fun onItemClick(view: View) {
        if (isLocked) {
             onLockedClick?.invoke()
        } else {
            val bundle = Bundle()
            bundle.putSerializable("sermon", sermon)
            val goToSermonDetail = Intent(view.context, ReadingActivity::class.java)
            goToSermonDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            goToSermonDetail.putExtras(bundle)
            view.context.startActivity(goToSermonDetail)
        }
    }

    fun setSermonData(sermon: Sermon) {
        this.sermon = sermon
        notifyChange()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, url: String?) {
            Glide.with(imageView.context).load(url)
                .transition(GenericTransitionOptions.with(R.anim.fade)).into(imageView)
        }
    }
}