package com.example.lifehack.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.example.lifehack.R;
import com.example.lifehack.activities.Hack;
import com.example.lifehack.models.Category;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends BaseAdapter {
    ArrayList<Category> categories = new ArrayList<>();
    Context ctx;

    public CategoriesAdapter(Context context, ArrayList<Category> categories) {
        this.categories = new ArrayList<>(categories);
        this.ctx = context;
    }

    @Override
    public int getCount() {
        int size;
        if (categories.size() % 2 == 0){
            size = categories.size() / 2;
        } else {
            size = (categories.size() + 1) / 2;
        }
        return  size;
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
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_category, null);

        CircleImageView civLeft = view.findViewById(R.id.civLeft);
        ImageView ivBackgroundCircleImageLeft = view.findViewById(R.id.ivBackgroundCircleImageLeft);
        ImageView ivBackgroundShadowCircleImageLeft = view.findViewById(R.id.ivBackgroundShadowCircleImageRight);
        TextView tvNameCategoryLeft = view.findViewById(R.id.tvNameCategoryLeft);

        CircleImageView civRight = view.findViewById(R.id.civRight);
        ImageView ivBackgroundCircleImageRight = view.findViewById(R.id.ivBackgroundCircleImageRight);
        ImageView ivBackgroundShadowCircleImageRight = view.findViewById(R.id.ivBackgroundShadowCircleImageRight);
        TextView tvNameCategoryRight = view.findViewById(R.id.tvNameCategoryRight);

        if (position*2 < categories.size()) {
            tvNameCategoryLeft.setText(categories.get(position * 2).getCategory_name());
            Glide.with(ctx)
                    .load(categories.get(position * 2)
                            .getCategory_image())
                    .into(civLeft);
            civLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, Hack.class);
                    intent.putExtra("LISTHACKS", (ArrayList) categories.get(2*position).getHacks());
                    ctx.startActivity(intent);
                }
            });
        } else {
            ivBackgroundShadowCircleImageLeft.setVisibility(View.GONE);
            ivBackgroundCircleImageLeft.setVisibility(View.GONE);
            civLeft.setVisibility(View.GONE);
        }
        if (position*2+1 < categories.size()) {
            tvNameCategoryRight.setText(categories.get(position*2+1).getCategory_name());
            Glide.with(ctx)
                    .load(categories.get(position*2+1)
                            .getCategory_image())
                    .into(civRight);
            civRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, Hack.class);
                    intent.putExtra("LISTHACKS", (ArrayList) categories.get(2*position+1).getHacks());
                    ctx.startActivity(intent);
                }
            });
        } else {
            ivBackgroundShadowCircleImageRight.setVisibility(View.GONE);
            ivBackgroundCircleImageRight.setVisibility(View.GONE);
            civRight.setVisibility(View.GONE);
        }
        return view;
    }
}
