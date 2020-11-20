package com.example.currencies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Controller  {
    static final String BASE_URL_PB = "https://api.privatbank.ua/p24api/";
    static final String BASE_URL_NBU = "https://bank.gov.ua/NBUStatService/v1/statdirectory/";

    private static Retrofit retrofit_pb = null;
    private static Retrofit retrofit_nbu = null;

    private static Gson gson_pb = new GsonBuilder().create();
    private static Gson gson_nbu = new GsonBuilder().create();


    public static <T> T startPB(Class<T> serviceClass) {

        retrofit_pb = new Retrofit.Builder()
                .baseUrl(BASE_URL_PB)
                .addConverterFactory(GsonConverterFactory.create(gson_pb))
                .build();
        return retrofit_pb.create(serviceClass);
    }

    public static <T> T startNBU(Class<T> serviceClass) {
        retrofit_nbu = new Retrofit.Builder()
                .baseUrl(BASE_URL_NBU)
                .addConverterFactory(GsonConverterFactory.create(gson_nbu))
                .build();
        return retrofit_nbu.create(serviceClass);
    }



}
