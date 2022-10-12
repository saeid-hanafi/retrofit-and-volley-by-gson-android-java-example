package com.example.connecttoservertest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiServices {
    private static final String TAG = "ApiServices";
    private static final String STUDENT_API_URL = "https://reqres.in/api/users";
    private static RequestQueue requestQueue;

    public ApiServices(Context context){
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
                            Student student = new Student();
                            try {
                                student.setId(response.getInt("id"));
                                student.setFirstName(response.getString("first_name"));
                                student.setLastName(response.getString("last_name"));
                                student.setEmail(response.getString("email"));
                                student.setAvatar(response.getString("avatar"));

                                callback.onSuccess(student);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                        List<Student> studentsList = new ArrayList<>();
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray studentsData = data.getJSONArray("data");
                            int total = data.getInt("total");
                            if (studentsData.length() > 0 && total > 0) {
                                for (int i = 0; i < studentsData.length(); i++) {
                                    JSONObject student = studentsData.getJSONObject(i);
                                    Student students = new Student();
                                    students.setId(student.getInt("id"));
                                    students.setEmail(student.getString("email"));
                                    students.setFirstName(student.getString("first_name"));
                                    students.setLastName(student.getString("last_name"));
                                    students.setAvatar(student.getString("avatar"));

                                    studentsList.add(students);
                                }
                            }else{
                                Log.i(TAG, "onResponse: Students Not Found!");
                            }

                            callback.onSuccess(studentsList);

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
