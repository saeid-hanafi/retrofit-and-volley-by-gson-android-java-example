package com.example.connecttoservertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

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

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ExtendedFloatingActionButton fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNewStudentActivity.class));
            }
        });

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

                            RecyclerView recyclerView = findViewById(R.id.rv_main);
                            recyclerView.setLayoutManager(new LinearLayoutManager(
                                    MainActivity.this,
                                    RecyclerView.VERTICAL,
                                    false
                            ));
                            StudentAdapter studentAdapter = new StudentAdapter(studentsList);
                            recyclerView.setAdapter(studentAdapter);

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