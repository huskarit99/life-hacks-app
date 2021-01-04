package com.example.lifehack.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.lifehack.R;
import com.example.lifehack.databases.LocalDatabase;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
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
    LocalDatabase localDatabase;
    ShareDialog shareDialog = new ShareDialog(this);
    int index_of_hack;
    int index_of_current_category;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        localDatabase = new LocalDatabase(this);
        configActionBarAndNavigationBar();

        mapping();
        init();
        eventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (index_of_hack == -1) {
            index_of_hack = 0;
        }
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
            Intent intent = getIntent();
            index_of_current_category = Integer.parseInt(intent.getStringExtra("INDEX_OF_CURRENT_CATEGORY"));
            index_of_hack = localDatabase.getCurrentPosition(index_of_current_category);
            tvContentHack.setText(hacks.get(index_of_hack));
        }

        ivBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_of_hack += 1;
                if (hacks.size() != 0) {
                    if (index_of_hack == hacks.size()) {
                        index_of_hack = 0;
                    }
                    localDatabase.setCurrentPosition(index_of_current_category, index_of_hack);
                    tvContentHack.setText(hacks.get(index_of_hack));
                }
            }
        });

        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_of_hack -= 1;
                if (hacks.size() != 0) {
                    if (index_of_hack == -1) {
                        index_of_hack = hacks.size() - 1;
                    }
                    localDatabase.setCurrentPosition(index_of_current_category, index_of_hack);
                    tvContentHack.setText(hacks.get(index_of_hack));
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
        String message = "I found a life tip: " + '"' + hacks.get(index_of_hack) + '"';
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
            String pathOfBitmapPhoto = Environment.getExternalStorageDirectory().toString() + "/" + "bikipsharing" + ".jpg";
            File imageFile = new File(pathOfBitmapPhoto);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Uri bmpUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            intent.setPackage("com.facebook.katana");
            startActivity(Intent.createChooser(intent, "Share via"));

        } catch(Exception e) {
//            System.out.println(e);
            Toast.makeText(this, "Please intall the Facebook", Toast.LENGTH_LONG).show();
        }
    }

    private void shareOnMail() {
        String message = "I found a life tip: " + '"' + hacks.get(index_of_hack) + '"';
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");
        startActivity(email);
    }

    private void shareOnOtherApp() {
        String message = "I found a life tip: " + '"' + hacks.get(index_of_hack) + '"';
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
