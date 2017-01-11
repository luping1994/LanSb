package com.suntrans.lanzhouwh.api;

import com.suntrans.lanzhouwh.bean.device.DeviceResult;
import com.suntrans.lanzhouwh.bean.general.Result;
import com.suntrans.lanzhouwh.bean.activate.ActiveResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Looney on 2017/1/4.
 */

public interface Api {

    /**
     * 激活账号
     * @param rusername
     * @param mobile
     * @param password
     * @param salt
     * @return
     */
    @FormUrlEncoded
    @POST("/webservice.asmx/Active_account")
    Observable<ActiveResult> activeAcount(@Field("rusername") String rusername,
                                          @Field("mobile") String mobile,
                                          @Field("password") String password,
                                          @Field("salt") String salt);

    @FormUrlEncoded
    @POST("/webservice.asmx/Check_account_exist")
    Observable<ActiveResult> checkAccountExist(@Field("account") String account);

    @FormUrlEncoded
    @POST("/webservice.asmx/Add_staff_account")
    Observable<Result> AddStaffAccount(@Field("rusername") String rusername,
                                    @Field("nickname") String nickname,
                                    @Field("deptidlist") String deptidlist,
                                    @Field("puid") String puid);


    @FormUrlEncoded
    @POST("/webservice.asmx/Check_login")
    Observable<Result> login(@Field("account") String account,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("/webservice.asmx/Query_account_info")
    Observable<Result> getUserInfo(@Field("account") String account);

    @GET("/webservice.asmx/Query_all_device_info")
    Observable<DeviceResult> getDeviceList(@Query("did") String did);
}
