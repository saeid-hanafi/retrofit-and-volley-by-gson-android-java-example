package com.example.connecttoservertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewStudentActivity extends AppCompatActivity {
    private static final String TAG = "AddNewStudentActivity";
    private ApiServices apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        Toolbar toolbar = findViewById(R.id.add_new_student_toolbar);
        setSupportActionBar(toolbar);

        apiServices = new ApiServices(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        TextInputEditText firstNameEt = findViewById(R.id.add_new_student_firstname_text);
        TextInputEditText lastNameEt = findViewById(R.id.add_new_student_lastname_text);
        TextInputEditText emailEt = findViewById(R.id.add_new_student_email_text);
        TextInputEditText avatarEt = findViewById(R.id.add_new_student_avatar_text);

        View fabAddNewStudentSave = findViewById(R.id.fab_addNewStudent_save);
        fabAddNewStudentSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstNameEt != null && lastNameEt != null &&
                    emailEt != null && avatarEt != null) {
                    if (firstNameEt.length() > 0 && lastNameEt.length() > 0 &&
                        emailEt.length() > 0 && avatarEt.length() > 0) {
                        String firstName = firstNameEt.getText().toString();
                        String lastName = lastNameEt.getText().toString();
                        String email = emailEt.getText().toString();
                        String avatar = avatarEt.getText().toString();

                        apiServices.saveStudent(firstName, lastName, email, avatar, new ApiServices.saveStudentCallback() {
                            @Override
                            public void onSuccess(Student student) {
                                Intent intent = new Intent();
                                intent.putExtra("student", student);
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onError(VolleyError error) {
                                Log.e(TAG, "onError: "+error.getMessage());
                            }
                        });
                    }else{
                        Snackbar.make(fabAddNewStudentSave, "All Fields Is Require2!", Snackbar.LENGTH_SHORT).show();
                    }
                }else{
                    Snackbar.make(fabAddNewStudentSave, "All Fields Is Require1!", Snackbar.LENGTH_SHORT).show();
                }
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
}