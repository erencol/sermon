package com.erencol.sermon.Data.Service;

import com.erencol.sermon.model.ReligiousDays;
import com.erencol.sermon.model.Sermon;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IReligious {
    @GET Observable<ReligiousDays> getReligiousDays(@Url String url);
}
