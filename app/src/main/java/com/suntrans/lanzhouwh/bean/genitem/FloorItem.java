package com.suntrans.lanzhouwh.bean.genitem;

/**
 * Created by Looney on 2017/3/9.
 */

public class FloorItem {
    public String did;
    public String floor;
    public String zmon;
    public String zmoff;
    public String czon;
    public String czoff;
    public String allstate;
    public String online;

    @Override
    public String toString() {
        return "FloorItem{" +
                "did='" + did + '\'' +
                ", floor='" + floor + '\'' +
                ", zmon='" + zmon + '\'' +
                ", zmoff='" + zmoff + '\'' +
                ", czon='" + czon + '\'' +
                ", czoff='" + czoff + '\'' +
                ", allstate='" + allstate + '\'' +
                ", online='" + online + '\'' +
                '}';
    }
}
