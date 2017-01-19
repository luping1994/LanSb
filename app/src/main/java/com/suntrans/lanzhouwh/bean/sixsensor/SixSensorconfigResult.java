package com.suntrans.lanzhouwh.bean.sixsensor;

/**
 * Created by Looney on 2017/1/14.
 */

public class SixSensorconfigResult {

    /**
     *
     * {
     "status_code": 1,
     "info": {
     "id": 1,
     "sensus_id": 1,
     "temperature": "45",
     "smog": "534",
     "methanal": "0.2",
     "pm25": "100",
     "vibrancy": "0.3",
     "command": "0011010",
     "status": 1,
     "updated_at": "2017-01-14 04:55:01"
     }
     }
     */

    public String status_code;
    public ConfigInfo info;
}
