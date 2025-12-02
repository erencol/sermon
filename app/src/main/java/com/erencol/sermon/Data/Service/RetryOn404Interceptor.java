package com.erencol.sermon.Data.Service;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryOn404Interceptor implements Interceptor {
    private final HttpUrl fallbackUrl;

    public RetryOn404Interceptor(String fallbackBaseUrl) {
        this.fallbackUrl = Objects.requireNonNull(HttpUrl.parse(fallbackBaseUrl));
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == 404) {
            response.close();

            // Orijinal request'in path'ini al
            String path = request.url().encodedPath();

            // Fallback base URL ile path'i birleştir
            HttpUrl newUrl = fallbackUrl.newBuilder()
                    .encodedPath(path)
                    .build();

            Request newRequest = request.newBuilder()
                    .url(newUrl)
                    .build();

            return chain.proceed(newRequest);
        }

        return response;
    }
}
