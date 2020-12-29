package com.example.lifehack.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifehack.R;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Detail extends AppCompatActivity {
    TextView tvContentHack, tvTitle;
    ImageView ivBackLeft, ivBackRight, ivArrowBack,
            ivSetting, ivSharingFb, ivSharingTwitter,
            ivSharingMail, ivMoreOption, ivNavigationBar;
    ArrayList<String> hacks;
    ShareDialog shareDialog = new ShareDialog(this);
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
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color1));
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
                moveTaskToBack(true);
            }
        });

        ivSharingFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnFaceBook();
            }
        });

        ivSharingTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnTwitter();
            }
        });

        ivSharingMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnMail();
            }
        });

        ivMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnOtherApp();
            }
        });
    }

    private void shareOnTwitter() {
        String message = "I found a life tip: " + '"' + hacks.get(index_of_Hacks) + '"';
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "UTF-8 should always be supported", Toast.LENGTH_LONG).show();
            return "";
        }
    }

    private void shareOnFaceBook() {
        try {
            View v = getWindow().getDecorView().getRootView();
            Bitmap bitmap = Bitmap.createBitmap(
                    v.getWidth(),
                    v.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .setCaption("Testing")
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();
            shareDialog.show(content);
        } catch(Exception e) {
            Toast.makeText(this, "Please intall the Facebook", Toast.LENGTH_LONG).show();
        }
    }

    private void shareOnMail() {
        String message = "I found a life tip: " + '"' + hacks.get(index_of_Hacks) + '"';
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");
        startActivity(email);
    }

    private void shareOnOtherApp() {
        String message = "I found a life tip: " + '"' + hacks.get(index_of_Hacks) + '"';
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(emailIntent, "Sharing Options"));
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
