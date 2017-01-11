package com.suntrans.lanzhouwh.bean.device;

/**
 * Created by Looney on 2017/1/6.
 */

public class DeviceInfo {

    /**
     * "ssid": 2,
     "devname": "1#电表",
     "biename": "1#电表",
     "addr": "201412000013",
     "ziaddr": "",
     "iddevtype": 7,
     "pcdiid": 3,
     "displayorder": 0,
     "remark": "一楼大厅",
     "keyword": "threephasemeter_1",
     "pssid": 0,
     "islock": 0,
     "idusetype": 0,
     "img": "dsi_1612221153239766891.png",
     "idcatetype": 4,
     "idtotorbran": 1,
     "selfreport": 0
     */

    public String ssid;
    public String devname;
    public String biename;
    public String addr;
    public String ziaddr;
    public String iddevtype;
    public String pcdiid;
    public String displayorder;
    public String remark;
    public String keyword;
    public String pssid;
    public String islock;
    public String idusetype;
    public String img;
    public String idcatetype;
    public String idtotorbran;
    public String selfreport;


    @Override
    public String toString() {
        return "DeviceInfo{" +
                "ssid='" + ssid + '\'' +
                ", devname='" + devname + '\'' +
                ", biename='" + biename + '\'' +
                ", addr='" + addr + '\'' +
                ", ziaddr='" + ziaddr + '\'' +
                ", iddevtype='" + iddevtype + '\'' +
                ", pcdiid='" + pcdiid + '\'' +
                ", displayorder='" + displayorder + '\'' +
                ", remark='" + remark + '\'' +
                ", keyword='" + keyword + '\'' +
                ", pssid='" + pssid + '\'' +
                ", islock='" + islock + '\'' +
                ", idusetype='" + idusetype + '\'' +
                ", img='" + img + '\'' +
                ", idcatetype='" + idcatetype + '\'' +
                ", idtotorbran='" + idtotorbran + '\'' +
                ", selfreport='" + selfreport + '\'' +
                '}';
    }
}
