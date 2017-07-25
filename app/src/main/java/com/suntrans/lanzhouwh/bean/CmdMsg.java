package com.suntrans.lanzhouwh.bean;

/**
 * Created by Looney on 2017/6/19.
 */

public class CmdMsg {
    public static final int TIPS=0;
    public static final int SUCCESS=1;
    private String msg;
    private int status;

    public CmdMsg( int status,String msg) {
        this.msg = msg;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
