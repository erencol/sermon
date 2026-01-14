package com.erencol.sermon.Data.Service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SermonClient {
    public static ISermons createSermonClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(null)
                .addInterceptor(new RetryOn404Interceptor(Host.getRetryBaseUrl()))
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Host.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ISermons.class);
    }

    public static IReligious createReligiousClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(null)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Host.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(IReligious.class);
    }

}
