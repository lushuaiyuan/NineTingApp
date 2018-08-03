package com.zzti.lsy.ninetingapp.network;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {

    /**
     * 在子线程执行
     * @param runnable
     */
    public static  void runOnBackThread(Runnable runnable){
//        new Thread(runnable).start();   // 线程池
        ThreadPoolManager.getInstance().createThreadPool().execute(runnable);
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 在主线程执行
     * @param runnable
     */
    public static  void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }
}
