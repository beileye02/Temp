package com.ssx.hepingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPreferenceManager {

    private SharedPreferences sharedPreferences;
    private static MyPreferenceManager self;
    private boolean isLogin; //用户时候登录
    private int id; //用户id
    private String avatar; //用户头像地址
    private String name; //用户姓名
    private String job; //用户职务

    private MyPreferenceManager(Context context) {
        init(context);
    }

    public static MyPreferenceManager getInstance(Context context) {
        if (self == null) {
            synchronized (MyPreferenceManager.class) {
                if (self == null) {
                    self = new MyPreferenceManager(context);
                }
            }
        }
        return self;
    }

    private void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
        String result = sharedPreferences.getString("user_info", "");
        if (!"".equals(result)) {
            result = AESUtils.decrypt(result);
            if (result != null) {
                parseResult(result);
            }
        }
    }

    public boolean isLogin() {
        return isLogin;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    /**
     * 保存用户信息
     *
     * @param result 服务器获取的用户数据
     */
    public void save(String result) {
        if (TextUtils.isEmpty(result)) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", true);
        result = AESUtils.encrypt(result);
        if (result != null) {
            editor.putString("user_info", result);
        }
        editor.apply();
        isLogin = true;
//        parseResult(result);
    }

    public void logout() {
        isLogin = false;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", false);
        editor.putString("user_info", "");
        editor.apply();
    }

    public void parseResult(String result) {
        if (!"".equals(result)) {
            try {
                JSONObject object = new JSONObject(result);
                id = object.getInt("id");
                name = object.getString("name");
                avatar = object.getString("touxiang");
                job = object.getString("zhiwu");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
