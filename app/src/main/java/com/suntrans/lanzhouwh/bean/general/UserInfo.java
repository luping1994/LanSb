package com.suntrans.lanzhouwh.bean.general;

import java.io.Serializable;

/**
 * Created by Looney on 2017/1/4.
 */
public class UserInfo implements Serializable{

    public String ruid;
    public String rusername;
    public String email;
    public String mobile;
    public String rusergid;//1是业主,2是员工
    public String nickname;//昵称
    public String verifyemail;
    public String verifymobile;
    public String issystem;
    public String isqiyong;
    public String avatar;
    public String deptidlist;

    @Override
    public String toString() {
        return "UserInfo{" +
                "ruid='" + ruid + '\'' +
                ", rusername='" + rusername + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", rusergid='" + rusergid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", verifyemail='" + verifyemail + '\'' +
                ", verifymobile='" + verifymobile + '\'' +
                ", issystem='" + issystem + '\'' +
                ", isqiyong='" + isqiyong + '\'' +
                ", avatar='" + avatar + '\'' +
                ", deptidlist='" + deptidlist + '\'' +
                '}';
    }

    /*
    "ruid": 9,
                "rusername": "owner",
                "email": "",
                "mobile": "13202041574",
                "rusergid": 1,
                "nickname": "nima",
                "verifyemail": 0,
                "verifymobile": 1,
                "issystem": 0,
                "isqiyong": 1,
                "avatar": "",
                "deptidlist": "6"
     */
}
