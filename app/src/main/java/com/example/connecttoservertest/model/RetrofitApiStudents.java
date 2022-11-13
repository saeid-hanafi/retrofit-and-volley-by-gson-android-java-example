package com.example.connecttoservertest.model;

import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiStudents {
    @GET("api/users/")
//    @Headers("Accept:application/json")
    Single<JsonObject> getStudents(@Query("page") int page);

    @POST("api/users/")
    Single<Student> saveStudent(@Body JsonObject body);
}
