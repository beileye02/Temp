package com.ssx.hepingapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ssx.hepingapp.R;

public class UserActivity extends BaseActivity {

    private ImageView avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initViews();
    }

    private void initViews() {
        avatar = findViewById(R.id.avatar);
        Glide.with(this).load("https://hpcg.zhihuidangjian.com/upload/201903/26/201903260909474063.jpg").into(avatar);
    }
}
