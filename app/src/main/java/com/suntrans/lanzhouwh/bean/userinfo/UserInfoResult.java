package com.suntrans.lanzhouwh.bean.userinfo;

/**
 * Created by Looney on 2017/1/11.
 */

public class UserInfoResult {
    public String status_code;
    public UserInfo user;

    @Override
    public String toString() {
        return "UserInfoResult{" +
                ", user=" + user.toString() +
                '}';
    }
}
