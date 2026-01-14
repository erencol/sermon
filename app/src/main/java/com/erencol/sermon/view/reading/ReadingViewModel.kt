package com.erencol.sermon.view.reading

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.erencol.sermon.R
import com.erencol.sermon.model.Sermon

class ReadingViewModel(private val sermon: Sermon) : ViewModel() {

    val pictureSermon: String?
        get() = sermon.imageUrl

    val sermonText: String?
        get() = sermon.text

    fun share(view: View) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = sermon.text
        sharingIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            view.context.resources.getString(R.string.app_name) + " uygulaması ile paylaşıyorum."
        )
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        view.context.startActivity(Intent.createChooser(sharingIntent, "Şununla paylaş"))
    }
}