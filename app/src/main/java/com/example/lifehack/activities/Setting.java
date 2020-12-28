package com.example.lifehack.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lifehack.MainActivity;
import com.example.lifehack.R;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {
    ImageView ivArrowBack, ivNotify, ivUpgradeVip, ivNavigationBar;

    boolean turnOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        configActionBarAndNavigationBar();

        mapping();
        eventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configActionBarAndNavigationBar();
    }

    private void configActionBarAndNavigationBar() {
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.parseColor("#F9FCFD"));
        View decorView = getWindow().getDecorView();
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(flags);
    }


    private void eventListener() {
        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turnOn == false) {
                    ivNotify.setImageResource(R.drawable.btn_notify_on);
                    turnOn = true;
                }
                else {
                    ivNotify.setImageResource(R.drawable.btn_notify);
                    turnOn = false;
                }
            }
        });

        ivUpgradeVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, Premium.class);
                startActivity(intent);
            }
        });

        ivNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void mapping() {
        ivArrowBack = findViewById(R.id.ivArrowBack);
        ivNotify = findViewById(R.id.ivNotify);
        ivUpgradeVip = findViewById(R.id.ivUpgradeVip);
        ivNavigationBar = findViewById(R.id.ivNavigationBar);
    }
}
