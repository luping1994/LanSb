package com.suntrans.lanzhouwh.bean.genitem;

import java.util.List;

/**
 * Created by Looney on 2017/5/4.
 */

public class MeterDetailItem {
    public String addr;
    public String elecmonth;
    public String balance;

    public List<HisData> hisdata;

    public static class HisData {
        public String gettime;
        public String elec;
        public String charge;
    }
}
