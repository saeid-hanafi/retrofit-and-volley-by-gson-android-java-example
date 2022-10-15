package com.example.connecttoservertest;

import androidx.annotation.Nullable;
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
    private static final int ADD_STUDENT_RESPONSE = 1001;
    private StudentAdapter studentAdapter;
    private RecyclerView recyclerView;
    private ApiServices apiServices;
    private RetrofitApiService retrofitApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Student> studentsInitial = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentsInitial);
        apiServices = new ApiServices(this);
        retrofitApiService = new RetrofitApiService();

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ExtendedFloatingActionButton fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddNewStudentActivity.class), ADD_STUDENT_RESPONSE);
            }
        });

        /*
        Use Volley Library For Connect To API
         */
//        apiServices.getAllStudents(new ApiServices.getAllStudentsCallback() {
//            @Override
//            public void onSuccess(List<Student> students) {
//                recyclerView = findViewById(R.id.rv_main);
//                recyclerView.setLayoutManager(new LinearLayoutManager(
//                        MainActivity.this,
//                        RecyclerView.VERTICAL,
//                        false
//                ));
//                studentAdapter = new StudentAdapter(students);
//                recyclerView.setAdapter(studentAdapter);
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//                Log.e(TAG, "onError: "+error.getMessage());
//            }
//        });

        /*
        Use Retrofit Library For Connect To API
         */
        retrofitApiService.getAllStudents(new RetrofitApiService.getAllStudentsCallback() {
            @Override
            public void onRetrofitSuccess(List<Student> students) {
                recyclerView = findViewById(R.id.rv_main);
                recyclerView.setLayoutManager(new LinearLayoutManager(
                        MainActivity.this,
                        RecyclerView.VERTICAL,
                        false
                ));
                studentAdapter = new StudentAdapter(students);
                recyclerView.setAdapter(studentAdapter);
            }

            @Override
            public void onRetrofitError(Exception error) {
                Log.e(TAG, "onError: "+error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_STUDENT_RESPONSE && resultCode == RESULT_OK && data != null) {
            Student student = data.getParcelableExtra("student");
            if (student != null) {
                studentAdapter.addStudent(student);
                recyclerView.smoothScrollToPosition(0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}