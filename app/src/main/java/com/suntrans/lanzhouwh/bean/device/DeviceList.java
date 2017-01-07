package com.suntrans.lanzhouwh.bean.device;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2017/1/4.
 */
public class DeviceList {
    @SerializedName("ds")
   public List<DeviceInfo> ds;
    @Override
    public String toString() {
        String str = "DATA==>{";
        if (ds==null){
            return "Data为空";
        }
        for (int i=0;i<ds.size();i++){
            str = str+ds.get(i).toString();
        }
        str +="}";
        return str;
    }
}
