package com.ssx.hepingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ssx.hepingapp.R;
import com.ssx.hepingapp.adapter.PolicyAdapter;
import com.ssx.hepingapp.data.PolicyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class PolicyActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG_POLICY = "policy";

    private ListView listView;
    private RelativeLayout loadingLayout;
    private List<PolicyData> dataList = new ArrayList<PolicyData>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        listView = findViewById(R.id.listView);
        loadingLayout = findViewById(R.id.loading_layout);
        listView.setOnItemClickListener(this);
        getPolicyList();
    }

    private void getPolicyList() {
        retrofitClient.getPolicyList(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                loadingLayout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                loadingLayout.setVisibility(View.GONE);
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
        }, TAG_POLICY);
    }

    private void parseResult(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            int len = jsonArray.length();
            if (len > 0) {
                List<PolicyData> dataList = new ArrayList<PolicyData>();
                for (int i = 0; i < len; i++) {
                    PolicyData policyData = new PolicyData();
                    JSONObject object = jsonArray.getJSONObject(i);
                    policyData.setTitle(object.getString("title"));
                    policyData.setId(object.getInt("id"));
                    policyData.setAddTime(object.getString("add_time"));
                    policyData.setUrl(object.getString("img_url"));
                    dataList.add(policyData);
                }
                this.dataList = dataList;
                PolicyAdapter adapter = new PolicyAdapter(this, this.dataList);
                listView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        retrofitClient.unSubscribe(TAG_POLICY);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PolicyData policyData = dataList.get(position);
        Intent intent = new Intent(this, PolicyDetailActivity.class);
        intent.putExtra("id", policyData.getId());
        startActivity(intent);
    }
}
