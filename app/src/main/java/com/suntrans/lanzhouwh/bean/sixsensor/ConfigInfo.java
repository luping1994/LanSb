package com.suntrans.lanzhouwh.bean.sixsensor;

/**
 * Created by Looney on 2017/1/14.
 */

public class ConfigInfo {
    /**
     *  "id": 1,
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
     */

    public String id;
    public String sensus_id;
    public String temperature;
    public String smog;
    public String methanal;
    public String pm25;
    public String vibrancy;
    public String command;
    public String status;
    public String updated_at;

    @Override
    public String toString() {
        return "ConfigInfo{" +
                "id='" + id + '\'' +
                ", sensus_id='" + sensus_id + '\'' +
                ", temperature='" + temperature + '\'' +
                ", smog='" + smog + '\'' +
                ", methanal='" + methanal + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", vibrancy='" + vibrancy + '\'' +
                ", command='" + command + '\'' +
                ", status='" + status + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
