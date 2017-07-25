package com.suntrans.lanzhouwh.bean.genitem;

import java.util.List;

/**
 * Created by Looney on 2017/4/8.
 */

public class MeterItem {

    public List<MeterInfo> meterlist;
    public List<ThreeMeter> threemeterlist;
   public static class MeterInfo{
       public String ssid;
       public String did;
       public String devname;
       public String didname;
       public String addr;
       public String keyword;
       public String raid;
       public String roomacount;
       public String balance;
       public String gettime;
       public String state;
       public String dianya;
       public String dianliu;
       public String yougonggonglv;
       public String gonglvyinshu;
       public String eletricity;
   }

    public static class ThreeMeter extends MeterInfo{
        public String vola;
        public String volb;
        public String volc;
        public String ia;
        public String ib;
        public String ic;

        public String activepower;
        public String powerrate;
        public String reactivePower;
    }
}
