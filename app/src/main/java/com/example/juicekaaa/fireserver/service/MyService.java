package com.example.juicekaaa.fireserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.juicekaaa.fireserver.db.DBManager;
import com.example.juicekaaa.fireserver.manager.FaceSDKManager;
import com.example.juicekaaa.fireserver.tcp.TCPManager;
import com.example.juicekaaa.fireserver.utils.GetMac;
import com.example.juicekaaa.fireserver.utils.GlobalSet;
import com.example.juicekaaa.fireserver.utils.PreferencesUtil;


public class MyService extends Service {
    public String SERVICE_IP = "101.132.139.37";//10.101.208.78   10.101.80.134 10.101.80.100 10.101.208.157 10.101.208.157 101.132.139.37
    public int SERVICE_PORT = 23303;//23303
    private String MAC = "";
    private final String TAG = "SERVICE";

    @Override
    public void onCreate() {
        super.onCreate();
        getMac();
        initSeceive();
        Log.e(TAG, MAC);
        PreferencesUtil.initPrefs(this);
        // 使用人脸1：n时使用
        DBManager.getInstance().init(this);
        //单目RGB活体, 请选用普通USB摄像头
        PreferencesUtil.putInt(GlobalSet.TYPE_LIVENSS, GlobalSet.TYPE_RGB_LIVENSS);
        //生活照
        PreferencesUtil.putInt(GlobalSet.TYPE_MODEL, GlobalSet.RECOGNIZE_LIVE);
        FaceSDKManager.getInstance().init(this, new FaceSDKManager.SdkInitListener() {
            @Override
            public void initStart() {
//                toast("开始初始化SDK");
            }

            @Override
            public void initSuccess() {
//                toast("SDK初始化成功");
            }

            @Override
            public void initFail(int errorCode, String msg) {
//                toast("SDK初始化失败:" + msg);
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TCPManager.getInstance().stopSocket();
    }

    /**
     * 初始化TCP通讯
     */
    private void initSeceive() {
        TCPManager.getInstance().initSocket(SERVICE_IP, String.valueOf(SERVICE_PORT)).setOnSocketStatusListener(new TCPManager.OnSocketStatusListener() {
            @Override
            public void onConnectSuccess() {
            }
        });
    }


    /**
     * 获取本地mac地址
     * 初始化socket
     */
    protected void getMac() {
        MAC = GetMac.getMacAddress().replaceAll(":", "");
        Log.i(TAG, MAC);
    }
}
