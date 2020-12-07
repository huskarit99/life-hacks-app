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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lifehack.activities.Hack;
import com.example.lifehack.models.Category;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
        init();
        
//        printKeyHash();

        customAdapter = new MainActivity.CustomAdapter();
        listCategories.setAdapter(customAdapter);
        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Hack.class);
                intent.putExtra("LISTHACKS", (ArrayList) hacks);
                startActivity(intent);
            }
        });

        getDataFromGoogleSheets();
    }

    private void getDataFromGoogleSheets() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://spreadsheets.google.com/tq?key=1s6p8sWRznMsy0ggI-gd1kPRFPHuX5GB6FwfiOApuw8E";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
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

    public void init() {
        hacks.add("Forgot your computer password? Boot up in safe mode (F8 during startup), log in as administrator and then change your password.");
        hacks.add("Save your keyboard from a coffee spill -\n" +
                "We’ve all been there. You’re being very careful, drinking coffee or another acidic or sugary beverage while working on your computer. Then, before you know what’s happening, bam. Your keyboard is flooded.\n" +
                "\n" +
                "Don’t panic in this scenario. Instead, your first step should be to turn off your computer and unplug it from any power sources or external hard drives, advises New York Times Personal Tech columnist J.D. Biersdorfer. Take out the device's battery, if your computer is designed to allow this.\n" +
                "\n" +
                "Ignore the instinct to wipe down your laptop and, instead, blot the spill with a clean towel. If you use a wiping motion, you might push the liquid further into the machine.\n" +
                "\n" +
                "If you’re able to, turn the laptop upside down and leave it off to allow as much of the liquid to drain out as possible. If a ton of liquid (something other than water) has spilled onto it, and you’re able to take it in to a repair shop right then, do so.\n" +
                "\n" +
                "Luckily, this sort of hazard is preventable: Use a plastic keyboard cover, or drink from a mug with a lid that locks.");
        hacks.add("Rest your eyes -\n" +
                "Even those of us who complain of having to stare at a computer screen all day don’t always take the necessary precautions to minimize eye strain.\n" +
                "\n" +
                "First, make a point of looking away from the screen at regular intervals. One widely accepted, easy-to-remember strategy is, every 20 minutes, look at an object at least 20 feet away for at least 20 seconds. The Canadian Association of Optometrists refers to this as the 20-20-20 rule.\n" +
                "\n" +
                "\n" +
                "If you’re able to shift the angle of your computer screen or monitor so overhead lights don’t create painful glare, do so. Another option is to download an add-on such as Flux, which will filter out some of the blue light from your display, leaving your screen looking yellowish. You can use this setting round the clock, but its developers otherwise program it to kick in around sunset. A less blue and dimmer screen is easier on the eyes, and it’s designed to help you sleep better at night.\n" +
                "\n" +
                "For Mac users, Apple released a similar tool within its software in early 2017, called Night Shift. The support page for the feature states, “Studies have shown that exposure to bright blue light in the evening can affect your circadian rhythms and make it harder to fall asleep.”");
        hacks.add("Try these office grooming hacks -\n" +
                "Even a day at the office can take its toll. Over the course of a day, even if you’re sitting at a desk for most of it, you can become sweaty, your breath may start to stink, your makeup may need a touch-up and more.\n" +
                "\n" +
                "Rather than taking a bunch of toiletries to work, look around the office and improvise with the supplies available. For instance, you can use coffee filters to blot the oil from your face or shine your shoes with a banana peel.\n" +
                "\n" +
                "Another trick you can try, if you notice your body odor is becoming a bit strong, is to take a spray bottle, fill it with some vodka (someone in your office probably has a bottle, right?) and spritz it on your clothes.");
        categories = new ArrayList<Category>();
        categories.add(new Category("Technology",
                "https://drive.google.com/uc?id=1PFda-GIHF-7bbhXZTfHwYaQP6r-x8c51",
                hacks));
        categories.add(new Category("Foods & Drinks",
                "https://drive.google.com/uc?id=1JkqMUgGtGHJbKpvEOnvrGzjkglgaK2l3",
                hacks));
        categories.add(new Category("Health & Fitness",
                "https://drive.google.com/uc?id=15BzJLyFyArmgBV0QrenANWUaRz0zYvJI",
                hacks));
        categories.add(new Category("Life",
                "https://drive.google.com/uc?id=1RSncDmY3DhIA_QvKoAhqWQl6AKxN3J7L",
                hacks));
        categories.add(new Category("Money Saver",
                "https://drive.google.com/uc?id=14i8qydxVDH9P04DVqLGz_78RMEwDqq_-",
                hacks));
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