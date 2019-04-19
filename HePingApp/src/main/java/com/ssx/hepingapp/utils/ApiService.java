package com.ssx.hepingapp.utils;

import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    String PATH = "tools/chengguan.ashx";

    @POST(PATH)
    Observable<ResponseBody> login(@Query("txtUserName") String username,
                                        @Query("txtPassword") String password, @Query("action") String action);
}
