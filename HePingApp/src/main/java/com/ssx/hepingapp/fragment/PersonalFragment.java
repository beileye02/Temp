package com.ssx.hepingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ssx.hepingapp.R;
import com.ssx.hepingapp.activity.MainActivity;

public class PersonalFragment extends BaseFragment {
    private ImageView avatar;
    private TextView name;
    private TextView job;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        avatar = view.findViewById(R.id.avatar);
        name = view.findViewById(R.id.name);
        job = view.findViewById(R.id.job);

        init();
        return view;
    }

    private void init() {
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) this.context;
            int id = activity.getId();
            String avatar = activity.getAvatar();
            String name = activity.getName();
            String job = activity.getJob();
            Glide.with(context).load(avatar).placeholder(R.mipmap.user).into(this.avatar);
            this.name.setText(name);
            this.job.setText(job);
        }
    }
}
