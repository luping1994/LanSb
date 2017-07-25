package com.suntrans.lanzhouwh.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.bean.genitem.FloorDetailItem;
import com.suntrans.lanzhouwh.views.Switch;
import com.suntrans.lanzhouwh.views.SwitchButton;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import retrofit2.http.Headers;

/**
 * Created by Looney on 2017/1/11.
 */

public class HardwareFragment_adapter extends RecyclerView.Adapter {
    private Context context;
    private CopyOnWriteArrayList<FloorDetailItem.Channel> datas;

    public HardwareFragment_adapter(Context context, CopyOnWriteArrayList<FloorDetailItem.Channel> datas) {
        this.context = context;
        this.datas = datas;
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

        if (!payloads.isEmpty()) {
            ((ViewHolder1) (holder)).aSwitch.setState(datas.get(position - 2).state.equals("1") ? true : false);
        } else {
            if (holder instanceof ViewHolder1) {
                ((ViewHolder1) (holder)).setData(position);
            } else if (holder instanceof ViewHolder2) {
                ((ViewHolder2) (holder)).setData(position);
            }else if (holder instanceof  ViewHolder3){
                ((ViewHolder3) (holder)).setData(position);

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
        RelativeLayout rl_content;
        RelativeLayout rlControl;
        SwitchButton switchButton ;

        public ViewHolder1(View itemView) {
            super(itemView);
//            aSwitch = (Switch) itemView.findViewById(R.id._switch);
            name = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            imageView = (ImageView) itemView.findViewById(R.id.iv_icon);
            rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            rlControl = (RelativeLayout) itemView.findViewById(R.id.rlControl);
            switchButton = (SwitchButton) itemView.findViewById(R.id.switchButton);
            switchButton.setOnCheckedChangeListener(null);
            rlControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSwitchRootClick(getAdapterPosition()-2);
//                    listener.onSwitchClick((SwitchButton) v.findViewById(R.id.switchButton), getAdapterPosition() - 2, ((SwitchButton) v.findViewById(R.id.switchButton)).isChecked());

                }
            });
//            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    listener.onSwitchClick((SwitchButton) buttonView, getAdapterPosition() - 2, isChecked);
//
//                }
//            });
//            aSwitch.setOnChangeListener(new Switch.OnSwitchChangedListener() {
//                @Override
//                public void onSwitchChange(Switch switchView, boolean isChecked) {
//                }
//            });
            rl_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContentClick(getAdapterPosition() - 2);
                }
            });
        }

        public void setData(int position) {

            if (datas.get(position - 2).state != null)
                switchButton.setCheckedImmediately(datas.get(position - 2).state.equals("1") ? true : false);
            String a = datas.get(position - 2).devname;
            String types = datas.get(position - 2).idusetype;
            name.setText(a == null ? "未定义名称" : a);
            type.setText(types == null ? "未定义类型" : types);
            Glide.with(context)
                    .load(datas.get(position-2).img)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_bulb_off)
                    .into(imageView);

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
//            header = (TextView) itemView.findViewById(R.id.header);
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
        TextView header;

        public ViewHolder3(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);
        }

        public void setData(int position) {
            int open = 0;
            for (int i=0;i<datas.size();i++){
                if (datas.get(i).state.equals("1")){
                    open++;
                }
            }
            header.setText("全部设备共"+datas.size()+"个,"+open+"个开启");

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

        void onSwitchClick(SwitchButton aswitch, int posttion, boolean state);
        void onSwitchRootClick(int posttion);

        void onContentClick(int position);
    }

}
