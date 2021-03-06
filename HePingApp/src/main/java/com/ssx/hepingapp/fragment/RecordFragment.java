package com.ssx.hepingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ssx.hepingapp.R;
import com.ssx.hepingapp.adapter.PolicyAdapter;
import com.ssx.hepingapp.adapter.RecordAdapter;
import com.ssx.hepingapp.data.PolicyData;
import com.ssx.hepingapp.data.RecordData;
import com.ssx.hepingapp.utils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class RecordFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String TAG_RECORD = "record";

    private ListView listView;
    private RelativeLayout loadingLayout;
    private List<RecordData> dataList = new ArrayList<RecordData>();
    private RetrofitClient retrofitClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        listView = view.findViewById(R.id.record_listView);
        loadingLayout = view.findViewById(R.id.loading_layout);
        listView.setOnItemClickListener(this);
        getRecordList();
        return view;
    }

    private void getRecordList() {
        retrofitClient = RetrofitClient.getInstance();
        retrofitClient.getRecordList(new Subscriber<ResponseBody>() {
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
        }, TAG_RECORD);
    }

    private void parseResult(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            int len = jsonArray.length();
            if (len > 0) {
                List<RecordData> dataList = new ArrayList<RecordData>();
                for (int i = 0; i < len; i++) {
                    RecordData recordData = new RecordData();
                    JSONObject object = jsonArray.getJSONObject(i);
                    recordData.setTitle(object.getString("title"));
                    recordData.setId(object.getInt("id"));
                    recordData.setAddTime(object.getString("addtime"));
                    recordData.setUrl(object.getString("img_url"));
                    dataList.add(recordData);
                }
                this.dataList = dataList;
                RecordAdapter adapter = new RecordAdapter(context, this.dataList);
                listView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        retrofitClient.unSubscribe(TAG_RECORD);
    }
}
