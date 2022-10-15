package com.example.connecttoservertest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiService {
    private static final String TAG = "ApiServices";
    private static final String STUDENT_API_URL = "https://reqres.in/";
    private Retrofit retrofit;
    private Gson gson = new Gson();
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
                .client(okHttpClient)
                .build();

        retrofitApiStudents = retrofit.create(RetrofitApiStudents.class);
    }

    public void getAllStudents(RetrofitApiService.getAllStudentsCallback retrofitCallback) {
        retrofitApiStudents.getStudents(1).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.has("total") && jsonObject.has("data")) {
                        JsonArray studentsData = jsonObject.getAsJsonArray("data");
                        if (studentsData.size() > 0) {
                            List<Student> students = gson.fromJson(studentsData.toString(), new TypeToken<List<Student>>() {}.getType());
                            retrofitCallback.onRetrofitSuccess(students);
                        }else{
                            retrofitCallback.onRetrofitError(new Exception("Total Or Student Data Is Zero!"));
                        }
                    }else{
                        retrofitCallback.onRetrofitError(new Exception("Total Or Student Data Not Fount!"));
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                retrofitCallback.onRetrofitError(new Exception(t));
            }
        });
    }

    public void saveStudent(String firstName, String lastName, String email, String avatar, RetrofitApiService.saveStudentCallback retrofitCallback) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("first_name", firstName);
        jsonObject.addProperty("last_name", lastName);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("avatar", avatar);
        retrofitApiStudents.saveStudent(jsonObject).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                retrofitCallback.onRetrofitSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                retrofitCallback.onRetrofitError(new Exception(t));
            }
        });
    }

    public interface saveStudentCallback {
        void onRetrofitSuccess(Student student);
        void onRetrofitError(Exception error);
    }

    public interface getAllStudentsCallback {
        void onRetrofitSuccess(List<Student> students);
        void onRetrofitError(Exception error);
    }
}
