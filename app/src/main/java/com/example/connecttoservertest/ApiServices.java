package com.example.connecttoservertest;

import android.content.Context;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiServices {
    private static final String TAG = "ApiServices";
    private static final String STUDENT_API_URL = "https://reqres.in/api/users";
    private static RequestQueue requestQueue;
    private Gson gson;

    public ApiServices(Context context){
        this.gson = new Gson();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public void saveStudent(String firstName, String lastName, String email, String avatar, saveStudentCallback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("email", email);
            jsonObject.put("avatar", avatar);

            GsonRequests<Student> gsonRequests = new GsonRequests<>(Request.Method.POST,
                    Student.class,
                    STUDENT_API_URL,
                    jsonObject,
                    new Response.Listener<Student>() {
                        @Override
                        public void onResponse(Student response) {
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onError(error);
                        }
                    },
                    null);
            requestQueue.add(gsonRequests);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllStudents(getAllStudentsCallback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        GsonRequests<List<Student>> gsonRequests = new GsonRequests<>(Request.Method.GET,
                new TypeToken<List<Student>>() {
                }.getType(),
                STUDENT_API_URL + "?page=1",
                new Response.Listener<List<Student>>() {
                    @Override
                    public void onResponse(List<Student> response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                },headers);
        requestQueue.add(gsonRequests);
    }

    public interface saveStudentCallback {
        void onSuccess(Student student);
        void onError(VolleyError error);
    }

    public interface getAllStudentsCallback {
        void onSuccess(List<Student> students);
        void onError(VolleyError error);
    }
}
