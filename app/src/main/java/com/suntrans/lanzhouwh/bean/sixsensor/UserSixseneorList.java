package com.suntrans.lanzhouwh.bean.sixsensor;

import com.google.gson.annotations.SerializedName;
import com.suntrans.lanzhouwh.bean.userinfo.ListInfo;

import java.util.List;

/**
 * Created by Looney on 2017/1/13.
 */

public class UserSixseneorList {

    /**
     *  "status_code": 1,
     "total": 9,
     "rows": [
     {
     "id": 1,
     "name": "一楼接待大厅东侧",
     "addr": "01c3",
     "clientId": "7f00000117700000010e",
     "isOnline": 1,
     "updated_at": "2017-01-13 09:14:09",
     "pivot": {
     "user_id": 1,
     "sensus_id": 1
     }
     },
     */
    public String status_code;
    public String total;

    @SerializedName("rows")
    public List<ListInfo> rows;

}
