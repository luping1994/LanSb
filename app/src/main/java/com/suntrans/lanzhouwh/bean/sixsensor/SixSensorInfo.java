package com.suntrans.lanzhouwh.bean.sixsensor;

/**
 * Created by Looney on 2017/1/13.
 */

public class SixSensorInfo {
    public String id;
    public String created_at;
    public String sensus_id;
    public String state;
    public String pm1;
    public String pm10;
    public String pm25;
    public String jiaquan;
    public String yanwu;
    public String wendu;
    public String shidu;
    public String guanxiqd;
    public String renyuanxx;
    public String xzhouqxj;
    public String yzhouqxj;
    public String pingmianqxj;
    public String zhendongqd;
    public String daqiya;

    @Override
    public String toString() {
        return "SixSensorInfo{" +
                "id='" + id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", sensus_id='" + sensus_id + '\'' +
                ", state='" + state + '\'' +
                ", pm1='" + pm1 + '\'' +
                ", pm10='" + pm10 + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", jiaquan='" + jiaquan + '\'' +
                ", yanwu='" + yanwu + '\'' +
                ", wendu='" + wendu + '\'' +
                ", shidu='" + shidu + '\'' +
                ", guanxiqd='" + guanxiqd + '\'' +
                ", renyuanxx='" + renyuanxx + '\'' +
                ", xzhouqxj='" + xzhouqxj + '\'' +
                ", yzhouqxj='" + yzhouqxj + '\'' +
                ", pingmianqxj='" + pingmianqxj + '\'' +
                ", zhendongqd='" + zhendongqd + '\'' +
                '}';
    }
    /**
     * "id": 33766,
     "created_at": "2017-01-13 09:08:37",
     "sensus_id": 9,
     "state": 0,
     "pm1": "74.00",
     "pm10": "87.00",
     "pm25": "81.00",
     "jiaquan": "0.00",
     "yanwu": "0.00",
     "wendu": "10.40",
     "shidu": "419.00",
     "guanxiqd": "3.00",
     "renyuanxx": "0.00",
     "xzhouqxj": "87.00",
     "yzhouqxj": "88.00",
     "pingmianqxj": "3.00",
     "zhendongqd": "0.20"
     */
}
