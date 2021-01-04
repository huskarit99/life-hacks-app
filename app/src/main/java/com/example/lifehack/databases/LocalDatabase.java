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

    public void setCurrentPosition(int index_of_current_category, int index_of_hack) {
        ArrayList<Integer> currentPosition;
        try {
            if (preferences.getString(context.getString(R.string.CURRENT_OF_POSITION), "") == "") {
                currentPosition = new ArrayList<>();
                while (currentPosition.size() - 1 != index_of_current_category) {
                    currentPosition.add(0);
                }
            } else {
                currentPosition = (ArrayList<Integer>) ObjectSerializer.deserialize(preferences.getString(context.getString(R.string.CURRENT_OF_POSITION), null));
                if (index_of_current_category >= currentPosition.size()) {
                    while (currentPosition.size() - 1 != index_of_current_category) {
                        currentPosition.add(0);
                    }
                }
            }
            currentPosition.set(index_of_current_category, index_of_hack);
            editor.putString(context.getString(R.string.CURRENT_OF_POSITION), ObjectSerializer.serialize(currentPosition));
            editor.commit();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public int getCurrentPosition(int index_of_current_category) {
        if (preferences.getString(context.getString(R.string.CURRENT_OF_POSITION), "") == "") {
            return 0;
        } else {
            try {
                ArrayList<Integer> currentPosition =  (ArrayList<Integer>) ObjectSerializer.deserialize(preferences.getString(context.getString(R.string.CURRENT_OF_POSITION), null));
                if (index_of_current_category >= currentPosition.size()) {
                    return 0;
                }
                return currentPosition.get(index_of_current_category);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return -1;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}