package com.sankets.penit;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String SharedPreference_name = "my_SharedPreference";

    private static SharedPref instance;
    private Context context;

    public SharedPref(Context context) {
        this.context = context;
    }

    public static synchronized SharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPref(context);

        }
        return instance;
    }


    public void save_flag(int flag){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("flag", flag);
        editor.apply();

    }
    public int get_flag(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);

        return sharedPreferences.getInt("flag", 0); // 0  linear ;  1 staggered
    }

    public void save_mode(int mode){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mode", mode);
        editor.apply();

    }
    public int get_mode(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);

        return sharedPreferences.getInt("mode", 0); //day : 0,  night : 1
    }



    public boolean is_first_time(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("first", true);
    }

    public void save_first(boolean flag){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first", flag);
        editor.apply();

    }

}
