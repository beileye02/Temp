package com.ssx.hepingapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.ssx.hepingapp.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class PolicyDetailActivity extends BaseActivity {
    private static final String TAG_POLICY_DETAIL = "policy_detail";
    private int id = -1;
    private TextView detail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_detail);

        detail = findViewById(R.id.detail);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", -1);
            getPolicyDetail();
        }
    }

    private void getPolicyDetail() {
        if (id != -1) {
            retrofitClient.getPolicyDetail(new Subscriber<ResponseBody>() {
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
                        CharSequence detail;
                        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            detail = Html.fromHtml(result, Html.FROM_HTML_MODE_LEGACY);
                        } else {
                            detail = Html.fromHtml(result);
                        }
                        PolicyDetailActivity.this.detail.setText(detail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, id, TAG_POLICY_DETAIL);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        retrofitClient.unSubscribe(TAG_POLICY_DETAIL);
    }
}
