package com.babacit.alarm.ui.activity;

import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.babacit.alarm.R;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.profile.ProfileTool;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeActivity extends BaseActivity {
    private static final int READ_PHONE_STATE = 100100;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView img = new ImageView(this);
        if(Constant.CAT_OR_BABY==0){
            img.setBackgroundResource(R.drawable.iv_welcome);
        }else {
            img.setBackgroundResource(R.drawable.modification_iv_welcome);
        }
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addContentView(img, params);
        requestP();

    }

    /**
     * 申请READ_PHONE_STATE
     */
    private void requestP() {

        //23以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {  //没有权限
                requestPermissions(permissions, READ_PHONE_STATE);
            } else {
                ProfileTool.getInstance().init(this);

                toMainActivity();
            }

        } else {
            ProfileTool.getInstance().init(this);
            toMainActivity();
        }

    }

    private void toMainActivity() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ProfileTool.getInstance().init(this);
                toMainActivity();
            } else {
                Toast.makeText(this, R.string.permission_reminder, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
