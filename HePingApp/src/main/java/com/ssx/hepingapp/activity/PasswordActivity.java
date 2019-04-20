package com.ssx.hepingapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssx.hepingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class PasswordActivity extends BaseActivity {
    private static final String TAG_PASSWORD = "change_password";
    private EditText oldPwdEt;
    private EditText newPwdEt;
    private EditText confirmPwdEt;
    private Button confirmBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        initViews();
    }

    private void initViews() {
        oldPwdEt = findViewById(R.id.et_old_password);
        newPwdEt = findViewById(R.id.et_new_password);
        confirmPwdEt = findViewById(R.id.et_confirm_password);
        confirmBtn = findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String oldPassword = oldPwdEt.getText().toString().trim();
        String newPassword = newPwdEt.getText().toString().trim();
        String confirmPassword = confirmPwdEt.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(this, getString(R.string.input_old_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, getString(R.string.input_new_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, getString(R.string.tip_input_confirm_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.equals(newPassword, confirmPassword)) {
            Toast.makeText(this, getString(R.string.password_inconsistent), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.equals(oldPassword, newPassword)) {
            Toast.makeText(this, getString(R.string.password_same), Toast.LENGTH_SHORT).show();
            return;
        }

        retrofitClient.changePassword(new Subscriber<ResponseBody>() {
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
                        parseResult(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, preferenceManager.getId(), oldPassword, newPassword, TAG_PASSWORD);
    }

    private void parseResult(String result) {
        try {
            //TODO 返回的字符串包含小括号？
            result = result.replace("(", "").replace(")", "");
            JSONObject object = new JSONObject(result);
            int status = object.getInt("status");
            if (status == 1) {
                Toast.makeText(this, getString(R.string.msg_change_password_success), Toast.LENGTH_SHORT).show();
                finish();
            } else if (status == 0) {
                Toast.makeText(this, getString(R.string.msg_change_password_failure), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        retrofitClient.unSubscribe(TAG_PASSWORD);
    }
}
