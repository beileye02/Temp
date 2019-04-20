package com.ssx.hepingapp.utils;

import com.ssx.hepingapp.BuildConfig;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitClient {
    private static final String URL = "http://gx.zhihuidangjian.com/";
    private static final String ACTION_LOGIN = "user_login";
    private static final String ACTION_LOGOUT = "go";
    private static final String ACTION_CHANGE_PASSWORD = "pass";
    private static final String ACTION_BANNER_LIST = "lunbo";
    private static final String ACTION_POLICY_LIST = "zhengcefagui";
    private static final String ACTION_POLICY_DETAIL = "zhengcefaguishow";
    private static final String ACTION_RECORD_LIST = "gongzuojishi";
    private static final String ACTION_RECORD_DETAIL = "gongzuojishishow";
    private static RetrofitClient self;
    private ApiService apiService;

    private Map<String, Subscription> subscriptionMap = new HashMap<String, Subscription>();

    public static RetrofitClient getInstance() {
        if (self == null) {
            synchronized (RetrofitClient.class) {
                if (self == null) {
                    self = new RetrofitClient();
                }
            }
        }
        return self;
    }

    private RetrofitClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            //Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            builder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 登录
     */
    public void login(Subscriber<ResponseBody> subscriber, String username, String password, String name) {
        Subscription subscription = apiService.login(username, password, ACTION_LOGIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    /**
     * 退出登录
     */
    public void logout(Subscriber<ResponseBody> subscriber, int id, String name) {
        Subscription subscription = apiService.logout(ACTION_LOGOUT, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    /**
     * 修改密码
     */
    public void changePassword(Subscriber<ResponseBody> subscriber, int id, String oldPassword, String newPassword, String name) {
        Subscription subscription = apiService.changePassword(id, oldPassword, newPassword, ACTION_CHANGE_PASSWORD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    /**
     * 获得首页轮播图列表
     */
    public void getBannerList(Subscriber<ResponseBody> subscriber, String name) {
        Subscription subscription = apiService.getBannerList(ACTION_BANNER_LIST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    /**
     * 获得政策法规列表
     */
    public void getPolicyList(Subscriber<ResponseBody> subscriber, String name) {
        Subscription subscription = apiService.getPolicyList(ACTION_POLICY_LIST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    public void getPolicyDetail(Subscriber<ResponseBody> subscriber, int id, String name) {
        Subscription subscription = apiService.getPolicyDetail(ACTION_POLICY_DETAIL, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    public void getRecordList(Subscriber<ResponseBody> subscriber, String name) {
        Subscription subscription = apiService.getPolicyList(ACTION_RECORD_LIST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    public void getRecordDetail(Subscriber<ResponseBody> subscriber, int id, String name) {
        Subscription subscription = apiService.getPolicyDetail(ACTION_RECORD_DETAIL, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        subscriptionMap.put(name, subscription);
    }

    public void unSubscribe(String name) {
        Subscription subscription = subscriptionMap.get(name);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscriptionMap.remove(name);
        }
    }
}
