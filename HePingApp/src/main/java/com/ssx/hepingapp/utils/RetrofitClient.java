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

    public void login(Subscriber<ResponseBody> subscriber, String username, String password, String name) {
        Subscription subscription = apiService.login(username, password, ACTION_LOGIN)
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
