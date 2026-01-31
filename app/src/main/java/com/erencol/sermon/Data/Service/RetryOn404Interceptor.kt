package com.erencol.sermon.data.service

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.regex.Pattern

class RetryOn404Interceptor : Interceptor {
    
    companion object {
        private val DATE_PATTERN = Pattern.compile("/(\\d{4})/(\\d{2})/")
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code() == 404) {
            val url = request.url()
            val path = url.encodedPath()

            val matcher = DATE_PATTERN.matcher(path)
            if (matcher.find()) {
                try {
                    var year = matcher.group(1)?.toInt() ?: return response
                    var month = matcher.group(2)?.toInt() ?: return response

                    month--
                    if (month <= 0) {
                        month = 12
                        year--
                    }

                    val newMonthStr = if (month < 10) "0$month" else month.toString()
                    val newYearStr = year.toString()

                    val replacement = "/$newYearStr/$newMonthStr/"
                    val newPath = matcher.replaceFirst(replacement)

                    val newUrl = url.newBuilder()
                        .encodedPath(newPath)
                        .build()

                    val newRequest = request.newBuilder()
                        .url(newUrl)
                        .build()

                    response.close()
                    return chain.proceed(newRequest)

                } catch (e: NumberFormatException) {
                    // Ignore
                }
            }
        }
        return response
    }
}
