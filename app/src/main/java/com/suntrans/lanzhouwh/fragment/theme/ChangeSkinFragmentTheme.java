package com.suntrans.lanzhouwh.fragment.theme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.MainActivity;
import com.suntrans.lanzhouwh.utils.UiUtils;

import java.io.File;

import ren.solid.skinloader.config.SkinConfig;
import ren.solid.skinloader.listener.ILoaderListener;
import ren.solid.skinloader.load.SkinManager;

/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:44
 */
public class ChangeSkinFragmentTheme extends ThemeBaseFragment implements View.OnClickListener {
    private TextView titleName;
    LinearLayout leftIcon;
    private static String TAG = "ChangeSkinFragmentTheme";
    private RelativeLayout toobar;
    private int theme;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_chang_skin;
    }

    @Override
    protected void init() {
        getThemeType();
    }

    private int getThemeType() {
        String themepack = SkinConfig.getCustomSkinPath(getActivity());
        if (themepack.equals(SkinConfig.DEFALT_SKIN)){
            return 0;
        }else if (themepack.equals("/data/data/" + App.getApplication().getPackageName() + "/skin" + "/skin_brown.skin")){
            return 1;
        }else if (themepack.equals("/data/data/" + App.getApplication().getPackageName() + "/skin" + "/skin_black.skin")){
            return 2;
        }
        return -1;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        titleName = (TextView) view.findViewById(R.id.title_name);
        leftIcon = (LinearLayout) view.findViewById(R.id.left_icon);
        titleName.setText("选择主题颜色");
        toobar = (RelativeLayout) view.findViewById(R.id.toolbar);
        leftIcon.setOnClickListener(this);
    }

    @Override
    protected void setUpView() {
        $(R.id.ll_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SkinConfig.getCustomSkinPath(getActivity()).equals(SkinConfig.DEFALT_SKIN))
                    return;
                SkinManager.getInstance().restoreDefaultTheme();
            }
        });
        $(R.id.ll_brown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File skin = new File("data/data/" + App.getApplication().getPackageName() + "/skin" + "/skin_brown.skin");
                if (!skin.exists()) {
                  UiUtils.showToast("皮肤包不存在");
                    return;
                }

                if (SkinConfig.getCustomSkinPath(getActivity()).equals("/data/data/" + App.getApplication().getPackageName() + "/skin" + "/skin_brown.skin"))
                {
                    UiUtils.showToast("已经是当前皮肤了");
                    return;
                }
                SkinManager.getInstance().load(skin.getAbsolutePath(),
                        new ILoaderListener() {
                            @Override
                            public void onStart() {
                                Log.e(TAG, "loadSkinStart");
                            }

                            @Override
                            public void onSuccess() {
                                Log.i(TAG, "loadSkinSuccess");
                                UiUtils.showToast("切换成功");

//                                Toast.makeText(getMContext(), "切换成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed() {
                                Log.i(TAG, "loadSkinFail");
//                                Toast.makeText(getMContext(), "切换失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        $(R.id.ll_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File skin = new File("data/data/" + App.getApplication().getPackageName() + "/skin" + "/skin_black.skin");
                if (!skin.exists()) {
                    UiUtils.showToast("皮肤包不存在");
                    return;
                }
                if (SkinConfig.getCustomSkinPath(getActivity()).equals("/data/data/" + App.getApplication().getPackageName() + "/skin" + "/skin_black.skin"))
                {
                    UiUtils.showToast("已经是当前皮肤了");
                    return;
                }
                SkinManager.getInstance().load(skin.getAbsolutePath(),
                        new ILoaderListener() {
                            @Override
                            public void onStart() {
                                Log.e(TAG, "loadSkinStart");
                            }

                            @Override
                            public void onSuccess() {
                                UiUtils.showToast("切换成功");
                            }

                            @Override
                            public void onFailed() {
                                Log.e(TAG, "loadSkinFail");
                            }
                        });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_right:
//                startActivity(new Intent(getActivity(), AddStaffActivity.class));
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.left_icon:
                ((MainActivity) getActivity()).drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }
}
