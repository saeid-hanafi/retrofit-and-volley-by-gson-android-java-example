package com.example.connecttoservertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNewStudentActivity extends AppCompatActivity {
    private static final String TAG = "AddNewStudentActivity";
    private ApiServices apiServices;
    private RetrofitApiService retrofitApiService;
    private Disposable disposable;
    private LottieAnimationView lottieAnimationView;
    private NestedScrollView nestedScrollView;
    private ExtendedFloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        Toolbar toolbar = findViewById(R.id.add_new_student_toolbar);
        setSupportActionBar(toolbar);

        apiServices = new ApiServices(this);
        retrofitApiService = new RetrofitApiService();
        lottieAnimationView = findViewById(R.id.progress_bar_add_new_student);
        nestedScrollView = findViewById(R.id.add_new_student_scroll);
        fabSave = findViewById(R.id.fab_addNewStudent_save);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        TextInputEditText firstNameEt = findViewById(R.id.add_new_student_firstname_text);
        TextInputEditText lastNameEt = findViewById(R.id.add_new_student_lastname_text);
        TextInputEditText emailEt = findViewById(R.id.add_new_student_email_text);
        TextInputEditText avatarEt = findViewById(R.id.add_new_student_avatar_text);

        View fabAddNewStudentSave = findViewById(R.id.fab_addNewStudent_save);
        fabAddNewStudentSave.setOnClickListener(v -> {
            lottieAnimationView.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            fabSave.setVisibility(View.GONE);

            if (firstNameEt != null && lastNameEt != null &&
                emailEt != null && avatarEt != null) {
                if (firstNameEt.length() > 0 && lastNameEt.length() > 0 &&
                    emailEt.length() > 0 && avatarEt.length() > 0) {
                    String firstName = firstNameEt.getText().toString();
                    String lastName = lastNameEt.getText().toString();
                    String email = emailEt.getText().toString();
                    String avatar = avatarEt.getText().toString();

                    /*
                    Use Volley Library For Connect To API
                    */
//                        apiServices.saveStudent(firstName, lastName, email, avatar, new ApiServices.saveStudentCallback() {
//                            @Override
//                            public void onSuccess(Student student) {
//                                Intent intent = new Intent();
//                                intent.putExtra("student", student);
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
//
//                            @Override
//                            public void onError(VolleyError error) {
//                                Log.e(TAG, "onError: "+error.getMessage());
//                            }
//                        });

                    /*
                    Use Retrofit Library For Connect To API
                    */
                    retrofitApiService.saveStudent(firstName, lastName, email, avatar)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Student>() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                    disposable = d;
                                }

                                @Override
                                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Student student) {
                                    Intent intent = new Intent();
                                    intent.putExtra("student", student);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    nestedScrollView.setVisibility(View.VISIBLE);
                                    fabSave.setVisibility(View.VISIBLE);
                                    lottieAnimationView.setVisibility(View.GONE);
                                    Log.e(TAG, "onError: "+e.getMessage());
                                }
                            });
                }else{
                    nestedScrollView.setVisibility(View.VISIBLE);
                    fabSave.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                    Snackbar.make(fabAddNewStudentSave, "All Fields Is Require2!", Snackbar.LENGTH_SHORT).show();
                }
            }else{
                nestedScrollView.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(View.GONE);
                Snackbar.make(fabAddNewStudentSave, "All Fields Is Require1!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}