package com.suntrans.lanzhouwh.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.suntrans.lanzhouwh.BR;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Looney on 2017/4/27.
 */

public class SensusSettingResult {
    private String code;
    private Param param;
    private String command;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    @Override
    public String toString() {
        return "SensusSettingResult{" +
                "code='" + code + '\'' +
                ", param=" + param.toString() +
                ", command='" + command + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public static class Param extends BaseObservable {
        private String v1 = "0";
        private String v2 = "0";
        private String v3 = "0";
        private String v4 = "0";
        private String v5 = "0";

        @Bindable
        public String getV1() {
            return v1;
        }

        public void setV1(String v1) {
            this.v1 = v1;
            notifyPropertyChanged(BR.v1);
        }

        @Bindable
        public String getV2() {
            return v2;
        }

        public void setV2(String v2) {
            this.v2 = v2;
            notifyPropertyChanged(BR.v2);
        }

        @Bindable
        public String getV3() {
            return v3;
        }

        public void setV3(String v3) {
            this.v3 = v3;
            notifyPropertyChanged(BR.v3);

        }

        @Bindable
        public String getV4() {
            return v4;
        }

        public void setV4(String v4) {
            this.v4 = v4;
            notifyPropertyChanged(BR.v4);

        }

        @Bindable
        public String getV5() {
            return v5;
        }

        public void setV5(String v5) {
            this.v5 = v5;
            notifyPropertyChanged(BR.v5);
        }


        public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("v1", v1);
                jsonObject.put("v2", v2);
                jsonObject.put("v3", v3);
                jsonObject.put("v4", v4);
                jsonObject.put("v5", v5);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }
    }

    public static class Commmand extends BaseObservable{
        private String wendu;
        private String yanwu;
        private String zhengdong;
        private String pm25;
        private String jiaquan;
        private String renyuan;

        public void setWendu(boolean isCheck) {
            this.wendu = isCheck ? "1" : "0";
            notifyPropertyChanged(BR.wendu);
        }

        public void setYanwu(boolean isCheck) {
            this.yanwu = isCheck ? "1" : "0";
            notifyPropertyChanged(BR.yanwu);

        }

        public void setZhengdong(boolean isCheck) {
            this.zhengdong = isCheck ? "1" : "0";
            notifyPropertyChanged(BR.zhengdong);

        }


        public void setPm25(boolean isCheck) {
            this.pm25 = isCheck ? "1" : "0";
            notifyPropertyChanged(BR.pm25);

        }


        public void setJiaquan(boolean isCheck) {
            this.jiaquan = isCheck ? "1" : "0";
            notifyPropertyChanged(BR.jiaquan);

        }


        public void setRenyuan(boolean isCheck) {
            this.renyuan = isCheck ? "1" : "0";
            notifyPropertyChanged(BR.renyuan);

        }

        public Commmand(String s) {
            this.wendu = s.substring(0, 1);
            this.yanwu = s.substring(1, 2);
            this.zhengdong = s.substring(2, 3);
            this.pm25 = s.substring(3, 4);
            this.jiaquan = s.substring(4, 5);
            this.renyuan = s.substring(5, 6);
        }

        public void setCommmand(String s) {
            this.wendu = s.substring(0, 1);
            this.yanwu = s.substring(1, 2);
            this.zhengdong = s.substring(2, 3);
            this.pm25 = s.substring(3, 4);
            this.jiaquan = s.substring(4, 5);
            this.renyuan = s.substring(5, 6);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append(wendu)
                    .append(yanwu)
                    .append(zhengdong)
                    .append(pm25)
                    .append(jiaquan)
                    .append(renyuan)
                    .toString();
        }

        @Bindable
        public boolean getWendu() {
            return wendu.equals("1") ? true : false;
        }

        @Bindable
        public boolean getYanwu() {
            return yanwu.equals("1") ? true : false;
        }

        @Bindable
        public boolean getZhengdong() {
            return zhengdong.equals("1") ? true : false;
        }

        @Bindable

        public boolean getPm25() {
            return pm25.equals("1") ? true : false;
        }

        @Bindable
        public boolean getJiaquan() {
            return jiaquan.equals("1") ? true : false;
        }

        @Bindable
        public boolean getRenyuan() {
            return renyuan.equals("1") ? true : false;
        }
    }
}
