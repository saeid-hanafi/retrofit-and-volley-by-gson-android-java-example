package com.example.connecttoservertest;

import com.google.gson.JsonObject;


import java.io.IOException;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiService {
    private static final String STUDENT_API_URL = "https://reqres.in/";
    private Retrofit retrofit;
    private RetrofitApiStudents retrofitApiStudents;

    public RetrofitApiService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request oldRequest = chain.request();
                        Request.Builder newRequestBuilder = oldRequest.newBuilder();
                        newRequestBuilder.addHeader("Accept", "application/json");
                        return chain.proceed(newRequestBuilder.build());
                    }
                }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(STUDENT_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        retrofitApiStudents = retrofit.create(RetrofitApiStudents.class);
    }

    public Single<JsonObject> getAllStudents() {
        return retrofitApiStudents.getStudents(1);
    }

    public Single<Student> saveStudent(String firstName, String lastName, String email, String avatar) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("first_name", firstName);
        jsonObject.addProperty("last_name", lastName);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("avatar", avatar);
        return retrofitApiStudents.saveStudent(jsonObject);
    }
}
