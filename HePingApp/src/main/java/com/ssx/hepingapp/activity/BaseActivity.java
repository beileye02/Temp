package com.ssx.hepingapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ssx.hepingapp.utils.MyPreferenceManager;
import com.ssx.hepingapp.utils.RetrofitClient;

public class BaseActivity extends AppCompatActivity {

    protected MyPreferenceManager preferenceManager;

    protected RetrofitClient retrofitClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = MyPreferenceManager.getInstance(this);
        retrofitClient = RetrofitClient.getInstance();
    }


}
