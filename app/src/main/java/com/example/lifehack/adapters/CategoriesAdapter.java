package com.example.lifehack.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lifehack.R;
import com.example.lifehack.activities.Detail;
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
        ImageView ivFrameCategoryLeft = view.findViewById(R.id.ivFrameCategoryLeft);
        TextView tvNameCategoryLeft = view.findViewById(R.id.tvNameCategoryLeft);

        CircleImageView civRight = view.findViewById(R.id.civRight);
        ImageView ivFrameCategoryRight = view.findViewById(R.id.ivFrameCategoryRight);
        TextView tvNameCategoryRight = view.findViewById(R.id.tvNameCategoryRight);

        if (position*2 < categories.size()) {
            categories.get(position*2).setCategory_name(categories.get(position*2).getCategory_name().replace('_', ' '));
            tvNameCategoryLeft.setText(categories.get(position*2).getCategory_name());
            Glide.with(ctx)
                    .load(categories.get(position * 2)
                            .getCategory_image())
                    .into(civLeft);
            civLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, Detail.class);
                    intent.putExtra("INDEX_OF_CURRENT_CATEGORY", (String) String.valueOf(2*position));
                    intent.putExtra("LISTHACKS", (ArrayList) categories.get(2*position).getHacks());
                    intent.putExtra("TITLE", (String) categories.get(2*position).getCategory_name());
                    ctx.startActivity(intent);
                }
            });
        } else {
            ivFrameCategoryLeft.setVisibility(View.GONE);
            civLeft.setVisibility(View.GONE);
        }
        if (position*2+1 < categories.size()) {
            categories.get(position*2+1).setCategory_name(categories.get(position*2+1).getCategory_name().replace('_', ' '));
            tvNameCategoryRight.setText(categories.get(position*2+1).getCategory_name());
            Glide.with(ctx)
                    .load(categories.get(position*2+1)
                            .getCategory_image())
                    .into(civRight);
            civRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, Detail.class);
                    intent.putExtra("INDEX_OF_CURRENT_CATEGORY", (String) String.valueOf(2*position+1));
                    intent.putExtra("LISTHACKS", (ArrayList) categories.get(2*position+1).getHacks());
                    intent.putExtra("TITLE", (String) categories.get(2*position+1).getCategory_name());
                    ctx.startActivity(intent);
                }
            });
        } else {
            ivFrameCategoryRight.setVisibility(View.GONE);
            civRight.setVisibility(View.GONE);
        }
        return view;
    }
}
