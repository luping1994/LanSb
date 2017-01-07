package com.suntrans.lanzhouwh.bean.general;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2017/1/4.
 */

public class Result {
   public String result;
    public  String reason;

    @SerializedName("data")
    public  Data data;

    @Override
    public String toString() {
        return "Result{" +
                "result='" + result + '\'' +
                ", reason='" + reason + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
