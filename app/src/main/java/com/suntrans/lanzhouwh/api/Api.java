package com.suntrans.lanzhouwh.api;

import com.suntrans.lanzhouwh.bean.LoginResult.LoginResult;
import com.suntrans.lanzhouwh.bean.genitem.BillResult;
import com.suntrans.lanzhouwh.bean.genitem.FloorDetailItem;
import com.suntrans.lanzhouwh.bean.genitem.FloorItem;
import com.suntrans.lanzhouwh.bean.genitem.MeterDetailItem;
import com.suntrans.lanzhouwh.bean.genitem.MeterItem;
import com.suntrans.lanzhouwh.bean.sixsensor.EnvironmentResult;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorDetailResult;
import com.suntrans.lanzhouwh.bean.sixsensor.SixSensorconfigResult;
import com.suntrans.lanzhouwh.bean.sixsensor.UserSixseneorList;
import com.suntrans.lanzhouwh.bean.switchs.ModifyChannel;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfoResult;
import com.suntrans.lanzhouwh.bean.userinfo.UserInfos;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
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


    @POST("/api/users/sensus")
    Observable<UserSixseneorList> getSixSensorlist();

    @FormUrlEncoded
    @POST("/api/users/sensus/current")
    Observable<SixSensorDetailResult> getSixSensorDetailInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("/api/users/sensus/config")
    Observable<SixSensorconfigResult> getSixSensorConfig(@Field("id") String id);

    @FormUrlEncoded
    @POST("/api/users/channel/change")
    Observable<ModifyChannel> modifyChannel(@Field("id") String id, @Field("name") String name, @Field("vtype") String vtype);


    @FormUrlEncoded
    @POST("/token")
    Observable<LoginResult> login2(@Field("grant_type") String grant_type,
                                   @Field("client_id") String client_id,
                                   @Field("client_secret") String client_secret,
                                   @Field("username") String username,
                                   @Field("password") String password);

    /**
     * 获取用户信息api
     *
     * @return
     */
    @GET("/api/wh/UserInfo")
    Observable<UserInfos> getUserInfo();

    @POST("/api/users/sensus")
    Observable<List<EnvironmentResult>> getEnvironment();



    @GET("/api/wh/Device_Admin")
    Observable<List<FloorItem>> getAdminDevice();

    @GET("/api/wh/Meter_Admin")
    Observable<MeterItem> getAdminMeter();

    @GET("/api/wh/Meter_Rent")
    Observable<MeterItem> getRentMeter();

    @GET("/api/wh/FloorDevice_Admin")
    Observable<FloorDetailItem> getFloorDeviceAdmin(@Query("did") String did);

    @GET("/api/wh/FloorDeviceSearch_Admin")
    Observable<FloorDetailItem> searchDeviceAdmin(@Query("did") String did,@Query("key") String key);

    @GET("/api/wh/FloorDevice_Rent")
    Observable<FloorDetailItem> getFloorDeviceRent();

    @GET("/api/wh/FloorDeviceSearch_Rent")
    Observable<FloorDetailItem> searchDeviceRent(@Query("key") String key);





    @GET("/api/wh/MeterDetail")
    Observable<MeterDetailItem> getMeterDetail(@Query("ssid") String did);

    @GET("/api/wh/MeterBill")
    Observable<BillResult> getMeterBill(@Query("ssid") String did);



}
