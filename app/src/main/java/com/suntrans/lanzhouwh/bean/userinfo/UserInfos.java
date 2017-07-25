package com.suntrans.lanzhouwh.bean.userinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2017/5/3.
 */

public class UserInfos implements Parcelable {
    private String ruid;
    private String rusername;
    private String email;
    private String mobile;
    private String rusergid;
    private String nickname;
    private String deptidlist;
    private String puid;
    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }

    public String getRusername() {
        return rusername;
    }

    public void setRusername(String rusername) {
        this.rusername = rusername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRusergid() {
        return rusergid;
    }

    public void setRusergid(String rusergid) {
        this.rusergid = rusergid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDeptidlist() {
        return deptidlist;
    }

    public void setDeptidlist(String deptidlist) {
        this.deptidlist = deptidlist;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ruid);
        dest.writeString(this.rusername);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.rusergid);
        dest.writeString(this.nickname);
        dest.writeString(this.deptidlist);
        dest.writeString(this.puid);
        dest.writeString(this.Message);
    }

    public UserInfos() {
    }

    protected UserInfos(Parcel in) {
        this.ruid = in.readString();
        this.rusername = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.rusergid = in.readString();
        this.nickname = in.readString();
        this.deptidlist = in.readString();
        this.puid = in.readString();
        this.Message = in.readString();
    }

    public static final Parcelable.Creator<UserInfos> CREATOR = new Parcelable.Creator<UserInfos>() {
        @Override
        public UserInfos createFromParcel(Parcel source) {
            return new UserInfos(source);
        }

        @Override
        public UserInfos[] newArray(int size) {
            return new UserInfos[size];
        }
    };

    @Override
    public String toString() {
        return "UserInfos{" +
                "ruid='" + ruid + '\'' +
                ", rusername='" + rusername + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", rusergid='" + rusergid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", deptidlist='" + deptidlist + '\'' +
                ", puid='" + puid + '\'' +
                ", Message='" + Message + '\'' +
                '}';
    }
}
