package com.suntrans.lanzhouwh.bean.userinfo;

/**
 * Created by Looney on 2017/1/11.
 */

public class Channel {

    /**
     *   "id": 27,
     "devId": 8,
     "number": "02",
     "name": "走廊筒灯",
     "vtype": 1,
     "groupId": null,
     "state": 1,
     "openCommand": "aa6800010001060302000122a90d0a",
     "closeCommand": "aa68000100010603020000e3690d0a",
     "pivot": {
     "user_id": 1,
     "channel_id": 27
     */

    public String id;
    public String devId;
    public String number;
    public String name;
    public String vtype;
    public String group_id;
    public String state;
    public String openCommand;
    public String closeCommand;
    public String status;
//    public Pivot pivot;

    public void setId(String id) {
        this.id = id;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public void setGroupId(String groupId) {
        this.group_id = groupId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setOpenCommand(String openCommand) {
        this.openCommand = openCommand;
    }

    public void setCloseCommand(String closeCommand) {
        this.closeCommand = closeCommand;
    }


    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", devId='" + devId + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", vtype='" + vtype + '\'' +
                ", groupId='" + group_id + '\'' +
                ", state='" + state + '\'' +
                ", openCommand='" + openCommand + '\'' +
                ", closeCommand='" + closeCommand + '\'' +
                '}';
    }
}
