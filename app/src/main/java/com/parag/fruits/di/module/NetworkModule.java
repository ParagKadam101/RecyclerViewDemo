package com.parag.fruits.di.module;

import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Module
public class NetworkModule {

    private String mUrl;
    public NetworkModule(String url)
    {
        mUrl = url;
    }

    @Singleton
    @Provides
    public OkHttpClient okHttpClient()
    {
        return new OkHttpClient();
    }

    @Singleton
    @Provides
    public Request request(){
        return new Request.Builder().url(mUrl).build();
    }

    @Singleton
    @Provides
    public Moshi moshi()
    {
        return new Moshi.Builder().build();
    }


}
