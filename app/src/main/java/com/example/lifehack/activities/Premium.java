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

import com.example.lifehack.R;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

public class Premium extends AppCompatActivity {
    TextView tvLater;
    ImageView ivNavigationBar, ivArrowBack, ivSetting;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        configActionBarAndNavigationBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configActionBarAndNavigationBar();

        mapping();
        eventListener();
    }

    private void configActionBarAndNavigationBar() {
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.parseColor("#41999A"));
        View decorView = getWindow().getDecorView();
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(flags);
    }

    private void eventListener() {
        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Premium.this, Setting.class);
                startActivity(intent);
            }
        });
    }

    private void mapping() {
        tvLater = findViewById(R.id.tvLater);
        ivSetting = findViewById(R.id.ivSetting);
        ivArrowBack = findViewById(R.id.ivArrowBack);
        ivNavigationBar = findViewById(R.id.ivNavigationBar);
    }
}
