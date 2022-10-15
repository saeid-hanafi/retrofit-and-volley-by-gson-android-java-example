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

import java.util.ArrayList;
import java.util.List;

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

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    STUDENT_API_URL,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "onResponse: "+ response);
                            Student student = gson.fromJson(response.toString(), Student.class);
                            callback.onSuccess(student);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: "+error.getMessage());
                            callback.onError(error);
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllStudents(getAllStudentsCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, STUDENT_API_URL+"?page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray studentsData = data.getJSONArray("data");
                            int total = data.getInt("total");
                            if (studentsData.length() > 0 && total > 0) {
                                List<Student> studentsList = gson.fromJson(studentsData.toString(), new TypeToken<List<Student>>() {}.getType());
                                callback.onSuccess(studentsList);
                            }else{
                                Log.i(TAG, "onResponse: Students Not Found!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getMessage());
                callback.onError(error);
            }
        });

        requestQueue.add(stringRequest);
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
