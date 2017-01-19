package com.suntrans.lanzhouwh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.suntrans.lanzhouwh.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.smssdk.SMSSDK;
import ren.solid.skinloader.base.SkinBaseApplication;

/**
 * Created by Looney on 2016/8/11.
 * Des:代表当前应用.
 */
public class App extends SkinBaseApplication {
    private static App application;
    private static int mainTid;
    private static Handler mHandler;
    private static SharedPreferences msharedPreferences;
    private static String appKey ="1a8d64bede614";
    private static String APPSECRET ="65a632d603ab6f02b85dbbeaa3d11dc5";
    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        CrashReport.initCrashReport(getApplicationContext(), "900055973", true);//初始化腾讯bug分析工具
        application=this;
        mainTid=android.os.Process.myTid();
        mHandler=new Handler();
        try {
            CopySkinFromAssets("skin_black.skin");
          LogUtil.i("app:"+ CopySkinFromAssets("skin_brown.skin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        boolean frist = getSharedPreferences().getBoolean("isFristCome",true);
//        //假如是第一次启动app则保存ip地址,首次穷默认为内网ip
//        if (frist){
//            getSharedPreferences().edit().putBoolean("isFristCome",false).commit();
//            getSharedPreferences().edit().putString("sixIpAddress","192.168.1.15").commit();
//            getSharedPreferences().edit().putInt("sixPort",2000).commit();
//
//            getSharedPreferences().edit().putString("chunkouIpAddress","192.168.1.213").commit();
//            getSharedPreferences().edit().putInt("chunkouPort",8000).commit();
//        }


    }
    public static SharedPreferences getSharedPreferences(){
        if (msharedPreferences==null){
            msharedPreferences = getApplication().getSharedPreferences("config",Context.MODE_PRIVATE);
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
     * @return 存储数据库的地址
     */

  // 复制和加载区域数据库中的数据
    public  String  CopySkinFromAssets(String SqliteFileName) throws IOException {

        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

        File dir = new File("data/data/" + App.getApplication().getPackageName() + "/skin");
        LogUtil.i("!dir.exists()=" + !dir.exists());
        LogUtil.i("!dir.isDirectory()=" + !dir.isDirectory());

        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File file= new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream =null;
//        if (file.exists()){
//            file.delete();
//        }
        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();

                inputStream = App.getApplication().getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len ;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }


            } catch (IOException e) {
                e.printStackTrace();

            }

            finally {

                if (outputStream != null) {

                    outputStream.flush();
                    outputStream.close();

                }
                if (inputStream != null) {
                    inputStream.close();
                }

            }

        }

        return file.getPath();

    }


}
