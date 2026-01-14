package com.erencol.sermon.data.service

import com.erencol.sermon.model.Sermon
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface ISermons {
    @GET
    fun getSermons(@Url url: String): Observable<List<Sermon>>
}
