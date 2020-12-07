package com.example.lifehack.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

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
                Intent intent = new Intent(Hack.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Target target = new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        SharePhoto sharePhoto = new SharePhoto.Builder()
//                                .setBitmap(bitmap)
//                                .build();
//                        if (ShareDialog.canShow(SharePhotoContent.class)) {
//                            SharePhotoContent content = new SharePhotoContent.Builder()
//                                    .addPhoto(sharePhoto)
//                                    .build();
//                            shareDialog.show(content);
//                        }
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//                    }
//                };
//                Picasso.get()
//                        .load("https://drive.google.com/uc?id=1PFda-GIHF-7bbhXZTfHwYaQP6r-x8c51")
//                        .into(target);

//                ShareMessengerURLActionButton actionButton =
//                        new ShareMessengerURLActionButton.Builder()
//                                .setTitle("Visit Facebook")
//                                .setUrl(Uri.parse("https://www.facebook.com"))
//                                .build();
//                ShareMessengerGenericTemplateElement genericTemplateElement =
//                        new ShareMessengerGenericTemplateElement.Builder()
//                                .setTitle("Life Hack")
//                                .setSubtitle("Visit Messenger")
//                                .setImageUrl(Uri.parse("https://drive.google.com/uc?id=1PFda-GIHF-7bbhXZTfHwYaQP6r-x8c51"))
//                                .setButton(actionButton)
//                                .build();
//                ShareMessengerGenericTemplateContent genericTemplateContent =
//                        new ShareMessengerGenericTemplateContent.Builder()
//                                .setPageId("840673686782363") // Your page ID, required
//                                .setGenericTemplateElement(genericTemplateElement)
//                                .build();
//                    MessageDialog.show(Hack.this, genericTemplateContent);
//            }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent
                        .putExtra(Intent.EXTRA_TEXT,
                                "I found a life tip: " + '"' + hacks.get(index_of_Hacks) + '"');
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Hack.this, "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                }
            }
        });
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
