package com.example.connecttoservertest.main;

import com.example.connecttoservertest.model.RetrofitApiService;
import com.google.gson.JsonObject;


import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MainViewModel {
    private RetrofitApiService retrofitApiService;
    private BehaviorSubject<Boolean> progressBar = BehaviorSubject.create();

    public MainViewModel(RetrofitApiService retrofitApiService) {
        this.retrofitApiService = retrofitApiService;
    }

    public Single<JsonObject> students() {
        progressBar.onNext(true);
        return retrofitApiService.getAllStudents()
                .doFinally(() -> progressBar.onNext(false));
    }

    public BehaviorSubject<Boolean> getProgressBar() {
        return progressBar;
    }
}
