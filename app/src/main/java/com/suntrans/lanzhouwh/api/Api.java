package com.suntrans.lanzhouwh.api;

import com.suntrans.lanzhouwh.bean.LoginResult.LoginResult;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorDetailResult;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorconfigResult;
import com.suntrans.lanzhouwh.bean.sixsensor.UserSixseneorList;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfoResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Looney on 2017/1/4.
 */

public interface Api {
//
//    /**
//     * 激活账号
//     * @param rusername
//     * @param mobile
//     * @param password
//     * @param salt
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/webservice.asmx/Active_account")
//    Observable<ActiveResult> activeAcount(@Field("rusername") String rusername,
//                                          @Field("mobile") String mobile,
//                                          @Field("password") String password,
//                                          @Field("salt") String salt);
//
//    @FormUrlEncoded
//    @POST("/webservice.asmx/Check_account_exist")
//    Observable<ActiveResult> checkAccountExist(@Field("account") String account);
//
//    @FormUrlEncoded
//    @POST("/webservice.asmx/Add_staff_account")
//    Observable<Result> AddStaffAccount(@Field("rusername") String rusername,
//                                    @Field("nickname") String nickname,
//                                    @Field("deptidlist") String deptidlist,
//                                    @Field("puid") String puid);
//
//
//    @FormUrlEncoded
//    @POST("/webservice.asmx/Check_login")
//    Observable<Result> login(@Field("account") String account,
//                                    @Field("password") String password);
//
//    @FormUrlEncoded
//    @POST("/webservice.asmx/Query_account_info")
//    Observable<Result> getUserInfo(@Field("account") String account);
//
//    @GET("/webservice.asmx/Query_all_device_info")
//    Observable<DeviceResult> getDeviceList(@Query("did") String did);


    /**
     * 登录api
     *
     * @param grant_type    默认填password
     * @param client_id     默认填6
     * @param client_secret 默认填test
     * @param username      账号
     * @param password      密码
     * @return
     */
    @FormUrlEncoded
    @POST("/oauth/access_token")
    Observable<LoginResult> login(@Field("grant_type") String grant_type,
                                  @Field("client_id") String client_id,
                                  @Field("client_secret") String client_secret,
                                  @Field("username") String username,
                                  @Field("password") String password);


    //http://yunapp.egird.com/api/users/info

    /**
     * 获取用户信息api
     * @return
     */
    @POST("/api/users/info")
    Observable<UserInfoResult> getUserInfo();

    @POST("/api/users/sensus")
    Observable<UserSixseneorList> getSixSensorlist();

    @FormUrlEncoded
    @POST("/api/users/sensus/current")
    Observable<SixSensorDetailResult> getSixSensorDetailInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("/api/users/sensus/config")
    Observable<SixSensorconfigResult> getSixSensorConfig(@Field("id") String id);
}
