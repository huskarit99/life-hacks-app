package com.example.lifehack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lifehack.activities.Setting;
import com.example.lifehack.adapters.CategoriesAdapter;
import com.example.lifehack.databases.LocalDatabase;
import com.example.lifehack.models.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ImageView ivNavigationBar, ivSetting;

    CategoriesAdapter customAdapter;

    ListView listCategories;
    ArrayList<Category> categories = new ArrayList<Category>();

    LocalDatabase localDatabase;
    ArrayList<Category> categoriesOfCache = new ArrayList<Category>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localDatabase = new LocalDatabase(this);
        setContentView(R.layout.activity_waiting);
        configActionBarAndNavigationBar();

        loadDataFromCacheMemory();
        loadDataFromGoogleSheets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configActionBarAndNavigationBar();
    }

    private void configActionBarAndNavigationBar() {
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color2));
        View decorView = getWindow().getDecorView();
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(flags);
    }

    private void saveData() {
        localDatabase.setCategories(categories);
    }

    private void loadDataFromCacheMemory() {
        categoriesOfCache = localDatabase.getCategories();
    }

    private void loadDataFromGoogleSheets() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1s6p8sWRznMsy0ggI-gd1kPRFPHuX5GB6FwfiOApuw8E&sheet=Sheet1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject packet = new JSONObject(response);
                    JSONArray data = (JSONArray) packet.get("Sheet1");
                    for (int i = 0; i < data.length(); i++)  {
                        JSONObject json = (JSONObject) data.get(i);
                        Iterator<String> keys = json.keys();
                        if (i == 0) {
                            int j = 0;
                            while(keys.hasNext()) {
                                String key = keys.next();
                                categories.add(new Category());
                                categories.get(j).setCategory_name(key);
                                j++;
                            }
                        }
                        if (i == 0) {
                            keys = json.keys();
                            int j = 0;
                            String url = "https://drive.google.com/uc?id=";
                            while (keys.hasNext()) {
                                String key = keys.next();
                                categories.get(j).setCategory_image(url + String.valueOf(json.get(key)));
                                j++;
                            }
                        }
                        else {
                            keys = json.keys();
                            int j = 0;
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String hack = String.valueOf(json.get(key));
                                hack = hack.replace(" ", "");
                                if (!hack.equals("")) {
                                    categories.get(j).addOneHack(String.valueOf(json.get(key)));
                                }
                                j++;
                            }
                        }
                    }
                    saveData();
                    renderData(categories);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                    renderData(categoriesOfCache);
                } else {
                    Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    private void renderData(ArrayList<Category> categories) {
        setContentView(R.layout.activity_on_boarding);
        mapping();
        customAdapter = new CategoriesAdapter(MainActivity.this, categories);
        listCategories.setAdapter(customAdapter);
        eventListener();
    }

    private void eventListener() {
        ivNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });
    }

    private void mapping() {
        listCategories = findViewById(R.id.listCategories);
        ivNavigationBar = findViewById(R.id.ivNavigationBar);
        ivSetting = findViewById(R.id.ivSetting);
    }
 }