package com.suntrans.lanzhouwh;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;
import com.suntrans.lanzhouwh.utils.LogUtil;
import com.tencent.bugly.Bugly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ren.solid.skinloader.load.SkinManager;

/**
 * Created by Looney on 2016/8/11.
 * Des:代表当前应用.
 */
public class App extends MultiDexApplication {
    private static App application;
    private static int mainTid;
    private static Handler mHandler;
    private static SharedPreferences msharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Bugly.init(this,"678ba11223",false);
        application = this;
        mainTid = android.os.Process.myTid();
        mHandler = new Handler();
        try {
            LogUtil.e("app:" + CopySkinFromAssets("skin_black.skin"));
            LogUtil.e("app:" + CopySkinFromAssets("skin_brown.skin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initSkinLoader();
    }

    public static SharedPreferences getSharedPreferences() {
        if (msharedPreferences == null) {
            msharedPreferences = getApplication().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return msharedPreferences;
    }

    public static Context getApplication() {
        return application;
    }

    public static int getMainTid() {
        return mainTid;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 见assets目录下的文件拷贝到sd上
     *
     * @return 存储数据库的地址
     */

    // 复制和加载区域数据库中的数据
    public String CopySkinFromAssets(String SqliteFileName) throws IOException {

        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

        File dir = new File("data/data/" + App.getApplication().getPackageName() + "/skin");
        LogUtil.i("!dir.exists()=" + !dir.exists());
        LogUtil.i("!dir.isDirectory()=" + !dir.isDirectory());

        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File file = new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
//        if (file.exists()){
//            file.delete();
//        }
        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();

            inputStream = App.getApplication().getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
            outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;

            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }


        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            if (outputStream != null) {

                outputStream.flush();
                outputStream.close();

            }
            if (inputStream != null) {
                inputStream.close();
            }

        }
        return file.getPath();
    }

    /**
     * Must call init first
     */
    private void initSkinLoader() {
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().load();
    }
    /** * 分割 Dex 支持 * @param base */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
