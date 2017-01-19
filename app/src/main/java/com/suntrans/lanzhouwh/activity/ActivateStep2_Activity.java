package com.suntrans.lanzhouwh.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.lanzhouwh.R;
import com.suntrans.lanzhouwh.activity.base.BasedActivity;
import com.suntrans.lanzhouwh.utils.Converts;
import com.suntrans.lanzhouwh.utils.UiUtils;
import com.suntrans.lanzhouwh.views.EditView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Looney on 2016/12/8.
 */

public class ActivateStep2_Activity extends BasedActivity {
    ImageView leftIcon;
    TextView titleName;


    @BindView(R.id.password)
    EditView password;
    @BindView(R.id.repassword)
    EditView repassword;
    @BindView(R.id.password_check)
    TextView passwordCheck;
    private String account;
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate2);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initView() {
        leftIcon = (ImageView) findViewById(R.id.left_image);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("激活账号(2/2)");
        leftIcon.setImageResource(android.support.design.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        account = getIntent().getStringExtra("account");
        mobile = getIntent().getStringExtra("phone");
    }


    public void activate(View view){
        String pw = password.getText().toString();
        pw = Converts.md5(pw);
        String repw = repassword.getText().toString();
        if (pw==null||pw.equals("")||repw==null||repw.equals("")){
            UiUtils.showToast("密码为空");
            return;
        }
        if (!pw.equals(repw)){
            UiUtils.showToast("两次密码不一致");
            return;
        }
        Random random = new Random();
        int i = 100000+random.nextInt(899999);
        pw=pw.replace(" ","");
        String salt = String.valueOf(i);
//        RetrofitHelper.getApi().activeAcount(account,mobile,pw,salt)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<ActiveResult>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        UiUtils.showToast("激活失败");
//                    }
//
//                    @Override
//                    public void onNext(ActiveResult activeResult) {
//                        if (activeResult.result.equals("ok")){
//                            UiUtils.showToast("激活成功");
//                            setResult(200);
//                            finish();
//                        }else {
//                            UiUtils.showToast(activeResult.reason);
//                        }
//                    }
//                });
    }
}
