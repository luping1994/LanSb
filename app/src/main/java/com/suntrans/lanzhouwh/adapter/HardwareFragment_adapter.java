package com.suntrans.lanzhouwh.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.bean.userinfo.Channel;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.suntrans.lanzhouwh.views.Switch;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Looney on 2017/1/11.
 */

public class HardwareFragment_adapter extends RecyclerView.Adapter {
    private Context context;
    private CopyOnWriteArrayList<Channel> datas;
    private String[] typeName;
    private ListView listView;
    public HardwareFragment_adapter(Context context, CopyOnWriteArrayList<Channel> datas) {
        this.context = context;
        this.datas = datas;
        typeName = context.getApplicationContext().getResources().getStringArray(R.array.type);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == 0)
            holder = new ViewHolder1(LayoutInflater.from(context)
                    .inflate(R.layout.item_switchadapter, parent, false));
        else if (viewType == 1)
            holder = new ViewHolder2(LayoutInflater.from(context)
                    .inflate(R.layout.fragment_hardware_item1, parent, false));
        else
            holder = new ViewHolder3(LayoutInflater.from(context)
                    .inflate(R.layout.fragment_hardware_item2, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
//        super.onBindViewHolder(holder, position, payloads);

        if (!payloads.isEmpty()){
            ((ViewHolder1) (holder)).aSwitch.setState(datas.get(position - 2).state.equals("1") ? true : false);
        }else {
            if (holder instanceof ViewHolder1) {
                ((ViewHolder1) (holder)).setData(position);
            } else if (holder instanceof ViewHolder2) {
                ((ViewHolder2) (holder)).setData(position);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 1;
        if (position == 1)
            return 2;
        return 0;
    }

    @Override
    public int getItemCount() {
        return datas.size() + 2;
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        Switch aSwitch;
        TextView name;
        TextView type;
        ImageView imageView;

        public ViewHolder1(View itemView) {
            super(itemView);
            aSwitch = (Switch) itemView.findViewById(R.id._switch);
            name = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            imageView = (ImageView) itemView.findViewById(R.id.iv_icon);
            aSwitch.setOnChangeListener(new Switch.OnSwitchChangedListener() {
                @Override
                public void onSwitchChange(Switch switchView, boolean isChecked) {
                    listener.onSwitchClick(switchView, getAdapterPosition() - 2, isChecked);
                }
            });
        }

        public void setData(int position) {
            if (datas.get(position - 2).state != null)
                aSwitch.setState(datas.get(position - 2).state.equals("1") ? true : false);
            if (datas.get(position - 2).name != null)
                name.setText(datas.get(position - 2).name);
            if (datas.get(position - 2).vtype != null) {
                try {
                    int id = Integer.valueOf(datas.get(position - 2).vtype);
                    type.setText(typeName[id]);
                    if (id == 1) {
                        imageView.setImageResource(R.drawable.ic_light);
                    } else if (id == 2) {
                        imageView.setImageResource(R.drawable.ic_chazuo);

                    } else if (id == 3) {
                        imageView.setImageResource(R.drawable.ic_kongtiao);

                    } else {
                        imageView.setImageResource(R.drawable.ic_light);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        RelativeLayout shutdown;
        TextView num;
        ImageView imageView;

        public ViewHolder2(View itemView) {
            super(itemView);
            shutdown = (RelativeLayout) itemView.findViewById(R.id.shutdown);
            num = (TextView) itemView.findViewById(R.id.tx_devicenum);
            imageView = (ImageView) itemView.findViewById(R.id.iv_shutdown);
            shutdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShutdownClick();
                }
            });

        }

        public void setData(int position) {
            num.setText(datas.size() + "台设备使用中");
        }

        public void setListener(final int position) {

        }
    }

    class ViewHolder3 extends RecyclerView.ViewHolder {

        public ViewHolder3(View itemView) {
            super(itemView);

        }

        public void setData(int position) {

        }

        public void setListener(final int position) {

        }
    }


    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    private onItemClickListener listener;

    public interface onItemClickListener {
        void onShutdownClick();

        void onSwitchClick(Switch aswitch, int posttion, boolean state);
    }

}
