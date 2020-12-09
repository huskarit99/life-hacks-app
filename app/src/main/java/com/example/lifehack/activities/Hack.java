package com.example.lifehack.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lifehack.MainActivity;
import com.example.lifehack.R;
import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMessengerGenericTemplateContent;
import com.facebook.share.model.ShareMessengerGenericTemplateElement;
import com.facebook.share.model.ShareMessengerURLActionButton;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class Hack extends AppCompatActivity {
    ImageView ivButtonRight, ivButtonLeft, ivButtonBack, ivButtonShare;
    TextView tvHack;
    ArrayList<String> hacks;
    ShareDialog shareDialog;
    int index_of_Hacks = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hack);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        init();
        mapping();
        shareDialog = new ShareDialog(this);
        if (hacks.size() != 0) {
            tvHack.setText(hacks.get(index_of_Hacks));
        }

        ivButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_of_Hacks += 1;
                if (hacks.size() != 0) {
                    if (index_of_Hacks == hacks.size()) {
                        index_of_Hacks = 0;
                    }
                    tvHack.setText(hacks.get(index_of_Hacks));
                }
            }
        });

        ivButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_of_Hacks -= 1;
                if (hacks.size() != 0) {
                    if (index_of_Hacks == -1) {
                        index_of_Hacks = hacks.size() - 1;
                    }
                    tvHack.setText(hacks.get(index_of_Hacks));
                }
            }
        });

        ivButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] choices = {"Facebook", "Messenger"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Hack.this);
                builder.setTitle("Sharing with apps");
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choices[which] == "Facebook") {
                            sharingOnTimeline();
                        } else {
                            sharingOnMessenger();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void sharingOnMessenger() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent
                .putExtra(Intent.EXTRA_TEXT,
                        "I found a life tip: " + '"' + hacks.get(index_of_Hacks) + '"');
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
//        sendIntent.setPackage("com.facebook.katana");
        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Hack.this, "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
        }
    }

    private void sharingOnTimeline() {
        try {
            View v = getWindow().getDecorView().getRootView();
            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),
                    v.getHeight(), Bitmap.Config.ARGB_8888);
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
        } catch (Throwable e) {
            Toast.makeText(Hack.this, "Please Install Facebook", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        Intent intent = getIntent();
        hacks = new ArrayList<>((ArrayList)intent.getSerializableExtra("LISTHACKS"));
    }

    private void mapping() {
        ivButtonLeft = findViewById(R.id.ivButtonLeft);
        ivButtonRight = findViewById(R.id.ivButtonRight);
        ivButtonBack = findViewById(R.id.ivButtonBack);
        ivButtonShare = findViewById(R.id.ivButtonShare);
        tvHack = findViewById(R.id.tvHack);
    }
}