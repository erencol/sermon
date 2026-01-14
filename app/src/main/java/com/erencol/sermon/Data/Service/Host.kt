package com.erencol.sermon.data.service

import java.time.LocalDate

object Host {
    private const val BASE_URL = "https://mobizoe.com/wp-content/uploads/"
    const val RELIGIOUS_DAYS_URL = "https://mobizoe.com/wp-content/uploads/2025/12/religiousdays.json"

    val baseUrl: String
        get() = BASE_URL + getYear() + "/" + getMonth() + "/"

    private fun getMonth(): String {
        val today = LocalDate.now()
        val month = today.monthValue
        return if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }
    }

    private fun getYear(): String {
        val today = LocalDate.now()
        val year = today.year
        return year.toString()
    }
    
    val sermonsEndpoint: String
         get() = "sermon.json?t=" + System.currentTimeMillis()
}
