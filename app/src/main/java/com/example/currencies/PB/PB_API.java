package com.example.currencies.PB;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PB_API {

    @GET("exchange_rates?json")
    Call<PB_jsondata> getData(@Query("date") String date);
}
