package com.example.lifehack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lifehack.activities.Hack;
import com.example.lifehack.models.Category;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ArrayList<Category> categories;
    CustomAdapter customAdapter;
    ListView listCategories;
    ArrayList<String> hacks = new ArrayList<>();
    ArrayList listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mapping();
        categories = new ArrayList<Category>();
        getDataFromGoogleSheets();
//        printKeyHash();

        customAdapter = new MainActivity.CustomAdapter();
        listCategories.setAdapter(customAdapter);
        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Hack.class);
                intent.putExtra("LISTHACKS", (ArrayList) categories.get(position).getHacks());
                startActivity(intent);
            }
        });
    }

    private void getDataFromGoogleSheets() {
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
                                    System.out.println(categories.get(j).getHacks());
                                }
                                j++;
                            }
                        }
                    }
                    customAdapter = new MainActivity.CustomAdapter();
                    listCategories.setAdapter(customAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println(String.valueOf(error));
                    Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.lifehack", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT));
            }
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void mapping() {
        listCategories = findViewById(R.id.listCategories);
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.row_category, null);

            ImageView ivCategoryImage = view.findViewById(R.id.ivCategoryImage);
            TextView tvCategory_name = view.findViewById(R.id.tvCategory_name);
            TextView tvNumberOfHacks = view.findViewById(R.id.tvNumberOfHacks);

            tvCategory_name.setText(categories.get(position).getCategory_name());
            tvNumberOfHacks.setText(String.valueOf(categories.get(position).getNumber_of_hacks()) + " Hacks");
            Picasso.get().load(categories.get(position).getCategory_image()).into(ivCategoryImage);
            return view;
        }
    }
}