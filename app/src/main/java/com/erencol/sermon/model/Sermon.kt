package com.erencol.sermon.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sermon(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("title") var title: String? = null,
    @SerializedName("shortText") var shortText: String? = null,
    @SerializedName("image") var imageUrl: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("text") var text: String? = null,
    @SerializedName("new") var isNew: Boolean? = false
) : Serializable
