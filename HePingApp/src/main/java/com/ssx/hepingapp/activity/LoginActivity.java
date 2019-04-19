package com.ssx.hepingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssx.hepingapp.R;
import com.ssx.hepingapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private static final String TAG_LOGIN = "login";

    private EditText nameEt;
    private EditText passwordEt;
    private Button loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        nameEt = findViewById(R.id.et_name);
        passwordEt = findViewById(R.id.et_pwd);
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String username = nameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getString(R.string.tip_input_username), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.tip_input_password), Toast.LENGTH_SHORT).show();
            return;
        }
        //无网络连接时给出提示
        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.tip_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO 软键盘弹出时，布局未整体上移   点击登录按钮后可加入弹窗提示
        //开始登录 测试账号：13053330789 密码：123456
        retrofitClient.login(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    if (!TextUtils.isEmpty(result)) {
                        Log.e(TAG, result);
                        parseResult(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, username, password, TAG_LOGIN);
    }

    private void parseResult(String result) {
        if (TextUtils.isEmpty(result)) {
            return;
        }
        //TODO 可对用户信息加密然后再保存
        preferenceManager.save(result);
        int id = preferenceManager.getId();
        if (id == 0) { //登录失败
            String name = preferenceManager.getName();
            if ("0".equals(name)) { //账号或密码错误
                Toast.makeText(LoginActivity.this, getString(R.string.login_failure), Toast.LENGTH_SHORT).show();
            } else if ("1".equals(name)) { //在其他设备登录
                Toast.makeText(LoginActivity.this, getString(R.string.login_failure_other), Toast.LENGTH_SHORT).show();
            }
        } else { //登录成功
            Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        retrofitClient.unSubscribe(TAG_LOGIN);
    }
}
