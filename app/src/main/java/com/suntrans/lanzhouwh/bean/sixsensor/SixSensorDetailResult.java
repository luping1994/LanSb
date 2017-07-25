package com.suntrans.lanzhouwh.bean.sixsensor;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2017/1/13.
 */

public class SixSensorDetailResult {
    /**
     * {
     "status_code": 1,
     "info": {
     "id": 9,
     "name": "明珠园办公室",
     "addr": "00f3",
     "clientId": "7f00000117700000005f",
     "isOnline": 1,
     "updated_at": "2017-01-13 14:20:41"
     },
     "curdate": "2017-01-13 14:35:27",
     "row": {
     "id": 44537,
     "created_at": "2017-01-13 14:35:20",
     "sensus_id": 9,
     "state": 0,
     "pm1": "108.00",
     "pm10": "122.00",
     "pm25": "115.00",
     "jiaquan": "0.00",
     "yanwu": "0.00",
     "wendu": "10.30",
     "shidu": "416.00",
     "guanxiqd": "3.00",
     "renyuanxx": "0.00",
     "xzhouqxj": "87.00",
     "yzhouqxj": "88.00",
     "pingmianqxj": "3.00",
     "zhendongqd": "0.10"
     }
     }
     */

    public String status_code;

    @SerializedName("row")
    public SixSensorInfo row;

    public Info info;
}
