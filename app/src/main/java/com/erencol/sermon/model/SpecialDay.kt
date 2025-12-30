package com.erencol.sermon.model

import com.google.gson.annotations.SerializedName

data class ReligiousDays(
    @SerializedName("year")
    var year: Int,

    @SerializedName("religious_days")
    var religiousDays: List<SpecialDay>
)
data class SpecialDay(
    @SerializedName("name")
    var name: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("hicri")
    var hicri: Hicri,

    @SerializedName("miladi")
    var miladi: Miladi
)

data class Hicri(
    @SerializedName("day")
    var day:Int,

    @SerializedName("month")
    var month: String,

    @SerializedName("year")
    var year: Int,

    @SerializedName("weekday")
    var weekday: String
)

data class Miladi(
    @SerializedName("day")
    var day:Int,

    @SerializedName("month")
    var month: String,

    @SerializedName("year")
    var year: Int,

    @SerializedName("weekday")
    var weekday: String
)
