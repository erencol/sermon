package com.erencol.sermon.data.service

import com.erencol.sermon.model.ReligiousDays
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface IReligious {
    @GET
    fun getReligiousDays(@Url url: String): Observable<ReligiousDays>
}
