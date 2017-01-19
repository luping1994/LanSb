package com.suntrans.lanzhouwh.bean.userinfo;

import java.util.List;

/**
 * Created by Looney on 2017/1/11.
 */

public class UserInfo {
    public  String id;
    public  String role;
    public  String name;
    public  String dept_name;
    public List<Channel> channels;
    public List<ChannelGroup> channel_group;
    public List<ChannelType> channel_type;

    @Override
    public String toString() {
        String s= "";
        for (int i = 0;i<channels.size();i++){
            s+=channels.get(i).toString();
        }
        return s;
    }
}
