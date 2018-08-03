package com.zzti.lsy.ninetingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.zzti.lsy.ninetingapp.utils.MigrationManager;

/**
 * author：anxin on 2018/8/3 11:19
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();// 初始化界面
        openHome();// 进入登录界面
    }


    private void openHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                into();
            }
        }, 2000);
    }


    private void initView() {
        setContentView(R.layout.activity_splash);
        verifyStoragePermissions();
        MigrationManager.migrate(this);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void into() {
//        if (!StringUtil.isNullOrEmpty(SpUtils.getInstance().getString(SpUtils.LOGINVENUEID, ""))) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        } else {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        }
        finish();
    }
}
