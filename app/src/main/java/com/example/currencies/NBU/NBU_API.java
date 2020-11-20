package com.example.currencies.NBU;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NBU_API {

    @GET("exchange?json")
    Call<List<NBU_data>> getNBUData(@Query("date") String date);
}
