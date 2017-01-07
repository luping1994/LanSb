package com.suntrans.lanzhouwh.bean.device;

import com.google.gson.annotations.SerializedName;

/**
 *
 * 从网络获取的设备列表信息
 * Created by Looney on 2017/1/4.
 */

public class DeviceResult {
   public String result;
    public  String reason;

    @SerializedName("data")
    public  DeviceList data;

    @Override
    public String toString() {
        return "Result{" +
                "result='" + result + '\'' +
                ", reason='" + reason + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
