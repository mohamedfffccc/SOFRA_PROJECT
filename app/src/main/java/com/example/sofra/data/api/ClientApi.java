package com.example.sofra.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {

    private static Retrofit retrofit = null;
    public static String BASE_URL = "http://ipda3-tech.com/sofra-v2/api/v2/";
    public static Retrofit GetClient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
