package com.suntrans.lanzhouwh.bean.LoginResult;

/**
 * Created by Looney on 2017/1/11.
 */

public class LoginResult {

    public String access_token;
    public String token_type;
    public String expires_in;
    public String error;
    public String error_description;

    @Override
    public String toString() {
        return "LoginResult{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", error='" + error + '\'' +
                ", error_description='" + error_description + '\'' +
                '}';
    }
}
