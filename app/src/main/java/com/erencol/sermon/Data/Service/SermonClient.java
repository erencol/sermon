package com.erencol.sermon.Data.Service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SermonClient {
    private static SermonClient instance;

    public static ISermons create() {
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

    public static SermonClient getInstance(){
        if (instance == null)
            instance = new SermonClient();
        return instance;
    }


}
