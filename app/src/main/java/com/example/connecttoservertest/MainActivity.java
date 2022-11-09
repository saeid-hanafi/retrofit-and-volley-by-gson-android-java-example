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

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ADD_STUDENT_RESPONSE = 1001;
    private StudentAdapter studentAdapter;
    private RecyclerView recyclerView;
    private ApiServices apiServices;
    private RetrofitApiService retrofitApiService;
    private Disposable disposable;
    private Gson gson = new Gson();

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
        fab_main.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, AddNewStudentActivity.class), ADD_STUDENT_RESPONSE));

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
        retrofitApiService.getAllStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(@NonNull JsonObject jsonObject) {
                        try {
                            if (jsonObject.has("total") && jsonObject.has("data")) {
                                JsonArray studentsData = jsonObject.getAsJsonArray("data");
                                if (studentsData.size() > 0) {
                                    List<Student> students = gson.fromJson(studentsData.toString(), new TypeToken<List<Student>>() {}.getType());
                                    LottieAnimationView lottieAnimationView = findViewById(R.id.progress_bar_main);
                                    ExtendedFloatingActionButton fabAdd = findViewById(R.id.fab_main);
                                    recyclerView = findViewById(R.id.rv_main);
                                    lottieAnimationView.setVisibility(View.GONE);
                                    fabAdd.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    recyclerView.setLayoutManager(new LinearLayoutManager(
                                            MainActivity.this,
                                            RecyclerView.VERTICAL,
                                            false
                                    ));
                                    studentAdapter = new StudentAdapter(students);
                                    recyclerView.setAdapter(studentAdapter);
                                }else{
                                    Log.e(TAG, "onResponse: Total Or Student Data Is Zero!");
                                }
                            }else{
                                Log.e(TAG, "onResponse: Total Or Student Data Not Fount!");
                            }
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}