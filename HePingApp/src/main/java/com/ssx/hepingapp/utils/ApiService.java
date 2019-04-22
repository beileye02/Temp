package com.ssx.hepingapp.utils;

import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    String PATH = "tools/chengguan.ashx";
    String PATH_CLOCK_IN = "tools/chengguan_daka.ashx";

    @POST(PATH)
    Observable<ResponseBody> login(@Query("txtUserName") String username,
                                   @Query("txtPassword") String password, @Query("action") String action);

    @POST(PATH)
    Observable<ResponseBody> getBannerList(@Query("action") String action);

    @POST(PATH)
    Observable<ResponseBody> logout(@Query("action") String action, @Query("id") int id);

    @POST(PATH)
    Observable<ResponseBody> changePassword(@Query("id") int id,
                                            @Query("txtOldPassword") String oldPassword,
                                            @Query("txtPassword") String newPassword, @Query("action") String action);

    @POST(PATH)
    Observable<ResponseBody> getPolicyList(@Query("action") String action);

    @POST(PATH)
    Observable<ResponseBody> getPolicyDetail(@Query("action") String action, @Query("id") int id);

    @POST(PATH)
    Observable<ResponseBody> getRecordList(@Query("action") String action);

    @POST(PATH)
    Observable<ResponseBody> getRecordDetail(@Query("action") String action, @Query("id") int id);

    @POST(PATH_CLOCK_IN)
    Observable<ResponseBody> uploadLocationInfo(@Query("action") String action,
                                                @Query("name") int userId,
                                                @Query("xx") double longitude,
                                                @Query("yy") double latitude);

    @POST(PATH_CLOCK_IN)
    Observable<ResponseBody> changeStatus(@Query("action") String action,
                                          @Query("name") int userId,
                                          @Query("zhuangtai") int status,
                                          @Query("xx") double longitude,
                                          @Query("yy") double latitude);
}
