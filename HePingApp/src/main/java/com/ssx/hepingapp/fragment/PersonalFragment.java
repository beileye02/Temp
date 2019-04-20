package com.ssx.hepingapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ssx.hepingapp.R;
import com.ssx.hepingapp.activity.MainActivity;
import com.ssx.hepingapp.activity.PasswordActivity;
import com.ssx.hepingapp.utils.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG_LOGOUT = "logout";
    private ImageView avatar;
    private TextView name;
    private TextView job;
    private Button logoutBtn;
    private Button changePwdBtn;
    private int id;
    private RetrofitClient retrofitClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        avatar = view.findViewById(R.id.avatar);
        name = view.findViewById(R.id.name);
        job = view.findViewById(R.id.job);
        logoutBtn = view.findViewById(R.id.logout_btn);
        changePwdBtn = view.findViewById(R.id.pwd_btn);
        logoutBtn.setOnClickListener(this);
        changePwdBtn.setOnClickListener(this);
        retrofitClient = RetrofitClient.getInstance();

        init();
        return view;
    }

    private void init() {
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) this.context;
            id = activity.getId();
            String avatar = activity.getAvatar();
            String name = activity.getName();
            String job = activity.getJob();
            Glide.with(context).load(avatar).placeholder(R.mipmap.user).into(this.avatar);
            this.name.setText(name);
            this.job.setText(job);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_btn:
                logout();
                break;
            case R.id.pwd_btn:
                Intent intent = new Intent(context, PasswordActivity.class);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        retrofitClient.unSubscribe(TAG_LOGOUT);
    }

    private void logout() {
        retrofitClient.logout(new Subscriber<ResponseBody>() {
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
        }, id, TAG_LOGOUT);
    }

    private void parseResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            int statusId = object.getInt("tuichu");
            if (statusId == 1) {
                ((MainActivity) context).logout();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
