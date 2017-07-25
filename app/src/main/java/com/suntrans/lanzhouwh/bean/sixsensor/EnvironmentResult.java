package com.suntrans.lanzhouwh.bean.sixsensor;

import java.io.Serializable;

/**
 * Created by Looney on 2017/4/5.
 */

public class EnvironmentResult implements Serializable {

    public String ssid;
    public String devname;
    public String biename;
    public String remark;
    public String keyword;
    public String gettime;
    public String state;
    public String pm1;
    public String pm10;
    public String pm25;
    public String jiaquan;
    public String yanwu;
    public String wendu;
    public String shidu;
    public String guangxianqd;
    public String renyuanxx;
    public String xzhouqxj;
    public String yzhouqxj;
    public String pingmianqxj;
    public String zhendongqd;
    public String daqiya;


    public String pm25Eva;
    public String pm1Eva;
    public String pm10Eva;
    public String wenduEva;
    public String shiduEva;
    public String guanzhaoEva;
    public String zEva;
    public String yanwuEva;
    public String jiaquanEva;

    public void setEva() {
        if (pm25 != null) {

            float pm1F = 0;
            try {
                pm1F = Float.valueOf(pm25);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (pm1F <= 35) {
                this.pm25Eva = "优";
            } else if (pm1F <= 75) {
                this.pm25Eva = "良";
            } else if (pm1F <= 115) {
                this.pm25Eva = "轻度污染";
            } else if (pm1F <= 150) {
                this.pm25Eva = "中度污染";
            } else if (pm1F <= 250) {
                this.pm25Eva = "重度污染";
            } else {
                this.pm25Eva = "严重污染";
            }
        }

        if (pm1 != null) {
            float pm1F = 0.0f;
            try {
                pm1F = Float.valueOf(pm1);
            } catch (NumberFormatException e) {

            }
            if (pm1F <= 35) {
                this.pm1Eva = "优";
            } else if (pm1F <= 75) {
                this.pm1Eva = "良";
            } else if (pm1F <= 115) {
                this.pm1Eva = "轻度污染";
            } else if (pm1F <= 150) {
                this.pm1Eva = "中度污染";
            } else if (pm1F <= 250) {
                this.pm1Eva = "重度污染";
            } else {
                this.pm1Eva = "严重污染";
            }
        }

        if (pm10 != null) {
            float pm10F = 0;
            try {
                pm10F = Float.valueOf(pm10);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (pm10F <= 50) {
                this.pm10Eva = "优";
            } else if (pm10F <= 150) {
                this.pm10Eva = "良";
            } else if (pm10F <= 250) {
                this.pm10Eva = "轻度污染";
            } else if (pm10F <= 350) {
                this.pm10Eva = "中度污染";
            } else if (pm10F <= 420) {
                this.pm10Eva = "重度污染";
            } else {
                this.pm10Eva = "严重污染";
            }
        }

        if (yanwu != null) {
            float aFloat = 0;
            try {
                aFloat = Float.valueOf(yanwu);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (aFloat < 750) {
                yanwuEva = "清洁";
            } else {
                yanwuEva = "污染";
            }
        }
        if (jiaquan!=null){
            float jiaquanF = 0;
            try {
                jiaquanF = Float.valueOf(jiaquan);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (jiaquanF < 0.1) {
                this.jiaquanEva = "清洁";
            } else {
                this.jiaquanEva = "超标";
            }
        }

        if (wendu!=null){
            float wenduF = 0;
            try {
                wenduF = Float.valueOf(wendu);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (wenduF <= 10) {
                wenduEva = "极寒";
            } else if (wenduF <= 15) {
                wenduEva = "寒冷";
            } else if (wenduF <= 20) {
                wenduEva = "凉爽";
            } else if (wenduF <= 28) {
                wenduEva = "舒适";
            } else if (wenduF <= 34) {
                wenduEva = "闷热";
            } else {
                wenduEva = "极热";
            }
        }

        if (shidu!=null){
            float shiduF = Float.valueOf(shidu);
            if (shiduF <= 40) {
                shiduEva = "干燥";
            } else if (shiduF <= 70) {
                shiduEva = "舒适";
            } else {
                shiduEva = "潮湿";

            }
        }
        if (guangxianqd != null) {
            float guangxianqdF = 0;
            try {
                guangxianqdF = Float.valueOf(guangxianqd);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (guangxianqdF == 0) {
                guanzhaoEva = "极弱";
            } else if (guangxianqdF == 1) {
                guanzhaoEva = "适中";
            } else if (guangxianqdF == 2) {
                guanzhaoEva = "强";
            } else if (guangxianqdF == 3) {
                guanzhaoEva = "很强";
            } else {
                guanzhaoEva = "极强";
            }
        }

        if (pingmianqxj!=null){
            float qingxie = 0;
            try {
                qingxie = Float.valueOf(pingmianqxj);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (qingxie>10){
                zEva = "倾斜";
            }else {
                zEva="正常";
            }
        }


    }


    @Override
    public String toString() {
        return "sensorInfo{" +
                "devname='" + devname + '\'' +
                ", biename='" + biename + '\'' +
                ", remark='" + remark + '\'' +
                ", keyword='" + keyword + '\'' +
                ", gettime='" + gettime + '\'' +
                ", state='" + state + '\'' +
                ", pm1='" + pm1 + '\'' +
                ", pm10='" + pm10 + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", jiaquan='" + jiaquan + '\'' +
                ", yanwu='" + yanwu + '\'' +
                ", wendu='" + wendu + '\'' +
                ", shidu='" + shidu + '\'' +
                ", guangxianqd='" + guangxianqd + '\'' +
                ", renyuanxx='" + renyuanxx + '\'' +
                ", xzhouqxj='" + xzhouqxj + '\'' +
                ", yzhouqxj='" + yzhouqxj + '\'' +
                ", pingmianqxj='" + pingmianqxj + '\'' +
                ", zhendongqd='" + zhendongqd + '\'' +
                '}';
    }
}
