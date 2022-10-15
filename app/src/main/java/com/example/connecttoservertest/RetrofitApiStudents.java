package com.example.connecttoservertest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiStudents {
    @GET("api/users/")
//    @Headers("Accept:application/json")
    Call<JsonObject> getStudents(@Query("page") int page);

    @POST("api/users/")
    Call<Student> saveStudent(@Body JsonObject body);
}
