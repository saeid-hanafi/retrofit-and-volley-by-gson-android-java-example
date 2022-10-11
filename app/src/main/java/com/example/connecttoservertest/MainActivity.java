package com.example.connecttoservertest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://reqres.in/api/users?page=1",
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

                            Log.i(TAG, "onResponse: "+studentsList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}