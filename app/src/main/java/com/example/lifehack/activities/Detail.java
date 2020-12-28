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

public class Detail extends AppCompatActivity {
    TextView tvContentHack, tvTitle;
    ImageView ivBackLeft, ivBackRight, ivArrowBack,
            ivSetting, ivSharingFb, ivSharingTwitter,
            ivSharingMail, ivMoreOption, ivNavigationBar;
    ArrayList<String> hacks;
    ShareDialog shareDialog;
    int index_of_Hacks = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        configActionBarAndNavigationBar();

        mapping();
        init();
        eventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configActionBarAndNavigationBar();
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

    private void init() {
        Intent intent = getIntent();
        hacks = new ArrayList<>((ArrayList)intent.getSerializableExtra("LISTHACKS"));
        tvTitle.setText((String) intent.getStringExtra("TITLE"));
    }

    private void eventListener() {
        if (hacks.size() != 0) {
            tvContentHack.setText(hacks.get(index_of_Hacks));
        }

        ivBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_of_Hacks += 1;
                if (hacks.size() != 0) {
                    if (index_of_Hacks == hacks.size()) {
                        index_of_Hacks = 0;
                    }
                    tvContentHack.setText(hacks.get(index_of_Hacks));
                }
            }
        });

        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_of_Hacks -= 1;
                if (hacks.size() != 0) {
                    if (index_of_Hacks == -1) {
                        index_of_Hacks = hacks.size() - 1;
                    }
                    tvContentHack.setText(hacks.get(index_of_Hacks));
                }
            }
        });

        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail.this, Setting.class);
                startActivity(intent);
            }
        });

        ivNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        ivButtonShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] choices = {"Facebook", "Messenger"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(Hack.this);
//                builder.setTitle("Sharing with apps");
//                builder.setItems(choices, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (choices[which] == "Facebook") {
//                            sharingOnTimeline();
//                        } else {
//                            sharingOnMessenger();
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });
    }

    private void mapping() {
        tvTitle = findViewById(R.id.tvTitle);
        tvContentHack = findViewById(R.id.tvContentHack);
        ivBackLeft = findViewById(R.id.ivBackLeft);
        ivBackRight = findViewById(R.id.ivBackRight);
        ivArrowBack = findViewById(R.id.ivArrowBack);
        ivSetting = findViewById(R.id.ivSetting);
        ivSharingFb = findViewById(R.id.ivSharingFb);
        ivSharingTwitter = findViewById(R.id.ivSharingTwitter);
        ivSharingMail = findViewById(R.id.ivSharingMail);
        ivMoreOption = findViewById(R.id.ivMoreOption);
        ivNavigationBar = findViewById(R.id.ivNavigationBar);
    }
}
