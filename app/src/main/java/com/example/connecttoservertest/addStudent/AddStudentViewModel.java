package com.example.connecttoservertest.addStudent;

import com.example.connecttoservertest.model.RetrofitApiService;
import com.example.connecttoservertest.model.Student;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class AddStudentViewModel {
    private RetrofitApiService retrofitApiService;
    private BehaviorSubject<Boolean> addStudentProgressBar = BehaviorSubject.create();

    public AddStudentViewModel(RetrofitApiService retrofitApiService) {
        this.retrofitApiService = retrofitApiService;
    }

    public Single<Student> save(String firstName, String lastName, String email, String avatar) {
        addStudentProgressBar.onNext(true);
        return retrofitApiService.saveStudent(firstName, lastName, email, avatar)
                .doFinally(() -> addStudentProgressBar.onNext(false));
    }

    public BehaviorSubject<Boolean> getAddStudentProgressBar() {
        return addStudentProgressBar;
    }
}
