package com.merci.lifehack.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.merci.lifehack.R;
import com.merci.lifehack.activities.Detail;
import com.merci.lifehack.models.Category;
import com.merci.lifehack.wallet.activity.CheckoutActivity;

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
        if (categories.size() % 2 == 0) {
            size = categories.size() / 2;
        } else {
            size = (categories.size() + 1) / 2;
        }
        return size;
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
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_category, null);

        CircleImageView civLeft = view.findViewById(R.id.civLeft);
        CircleImageView civLockLeft = view.findViewById(R.id.civLockLeft);
        ImageView ivFrameCategoryLeft = view.findViewById(R.id.ivFrameCategoryLeft);
        TextView tvNameCategoryLeft = view.findViewById(R.id.tvNameCategoryLeft);

        CircleImageView civRight = view.findViewById(R.id.civRight);
        CircleImageView civLockRight = view.findViewById(R.id.civLockRight);
        ImageView ivFrameCategoryRight = view.findViewById(R.id.ivFrameCategoryRight);
        TextView tvNameCategoryRight = view.findViewById(R.id.tvNameCategoryRight);

        if (position * 2 < categories.size()) {
            categories.get(position * 2).setCategory_name(categories.get(position * 2).getCategory_name().replace('_', ' '));
            String title = (String) categories.get(2 * position).getCategory_name();
            if (title.charAt(0) == '*') {
                title = title.substring(1, title.length());
            } else {
                civLockLeft.setVisibility(View.GONE);
            }
            tvNameCategoryLeft.setText(title);
            Glide.with(ctx)
                    .load(categories.get(position * 2)
                            .getCategory_image())
                    .into(civLeft);
            civLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String title = (String) categories.get(2 * position).getCategory_name();
                    Intent intent = new Intent(ctx, Detail.class);

                    if (title.charAt(0) == '*') {
                        title = title.substring(1, title.length());
                        intent = new Intent(ctx, CheckoutActivity.class);
                    }
                    intent.putExtra("INDEX_OF_CURRENT_CATEGORY", (String) String.valueOf(2 * position));
                    intent.putExtra("LISTHACKS", (ArrayList) categories.get(2 * position).getHacks());
                    intent.putExtra("TITLE", title);
                    ctx.startActivity(intent);
                }
            });
        } else {
            ivFrameCategoryLeft.setVisibility(View.GONE);
            civLeft.setVisibility(View.GONE);
        }
        if (position * 2 + 1 < categories.size()) {
            categories.get(position * 2 + 1).setCategory_name(categories.get(position * 2 + 1).getCategory_name().replace('_', ' '));
            String title = (String) categories.get(2 * position + 1).getCategory_name();
            if (title.charAt(0) == '*') {
                title = title.substring(1, title.length());
            } else {
                civLockRight.setVisibility(View.GONE);
            }
            tvNameCategoryRight.setText(title);
            Glide.with(ctx)
                    .load(categories.get(position * 2 + 1)
                            .getCategory_image())
                    .into(civRight);
            civRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, Detail.class);
                    String title = (String) categories.get(2 * position + 1).getCategory_name();
                    if (title.charAt(0) == '*') {
                        title = title.substring(1, title.length());
                        intent = new Intent(ctx, CheckoutActivity.class);
                    }
                    intent.putExtra("INDEX_OF_CURRENT_CATEGORY", (String) String.valueOf(2 * position + 1));
                    intent.putExtra("LISTHACKS", (ArrayList) categories.get(2 * position + 1).getHacks());
                    intent.putExtra("TITLE", title);
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
