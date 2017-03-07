package com.example.subh.popularmovies.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(null == retrofit){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
