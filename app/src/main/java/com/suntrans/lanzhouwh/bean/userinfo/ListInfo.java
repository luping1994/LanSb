package com.suntrans.lanzhouwh.bean.userinfo;

/**
 * Created by Looney on 2017/1/13.
 */

public class ListInfo {
    public String id;
    public String name;
    public String addr;
    public String clientId;
    public String isOnline;
    public String updated_at;

    @Override
    public String toString() {
        return "ListInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", clientId='" + clientId + '\'' +
                ", isOnline='" + isOnline + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
