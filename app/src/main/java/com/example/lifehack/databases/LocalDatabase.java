package com.example.lifehack.databases;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.example.lifehack.R;
import com.example.lifehack.models.Category;
import com.example.lifehack.models.ObjectSerializer;

import java.util.ArrayList;

public class LocalDatabase {
    private static String TAG = LocalDatabase.class.getName();
    SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;

    public LocalDatabase(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.NAME_LOCAL_DATABASE), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setCategories(ArrayList<Category> categories){
        try {
            editor.putString(context.getString(R.string.CATEGORIES), ObjectSerializer.serialize(categories));
            editor.commit();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<Category> getCategories(){
        if (preferences.getString(context.getString(R.string.CATEGORIES), "") == "") {
            return new ArrayList<Category>();
        } else {
            try {
                return (ArrayList<Category>) ObjectSerializer.deserialize(preferences.getString(context.getString(R.string.CATEGORIES), ""));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new ArrayList<Category>();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}