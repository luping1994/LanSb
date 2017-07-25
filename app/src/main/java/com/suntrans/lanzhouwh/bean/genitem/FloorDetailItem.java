package com.suntrans.lanzhouwh.bean.genitem;

import java.util.List;

/**
 * Created by Looney on 2017/5/3.
 */

public class FloorDetailItem {
    public String ondev;
    public String offdev;
    public String alldev;
    public String tenchannels;
    public String floor;
    public List<Channel> devlist;

    public static class Channel{
        public String ssid;
        public String state;
        public String idusetype;
        public String img;
        public String devname;

        @Override
        public String toString() {
            return "Channel{" +
                    "ssid='" + ssid + '\'' +
                    ", state='" + state + '\'' +
                    ", idusetype='" + idusetype + '\'' +
                    ", img='" + img + '\'' +
                    ", devname='" + devname + '\'' +
                    '}';
        }
    }
}
