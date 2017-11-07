package com.geoverifi.geoverifi.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.geoverifi.geoverifi.model.User;

/**
 * Created by chriz on 6/14/2017.
 */

public class Manager {
    public static final String SESSION_PREF_NAME = "session";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_FIRSTNAME = "user_firstname";
    public static final String KEY_USER_LASTNAME = "user_lastname";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_PHONE = "user_phone";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_USER_ABOUT = "user_about";

    private static Context context;
    private static Manager mInstance;

    public Manager(Context ctx){
        this.context = ctx;
    }

    public static synchronized Manager getInstance(Context context){
        if (mInstance == null){
            mInstance = new Manager(context);
        }

        return mInstance;
    }

    public boolean storeUser(User user){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, String.valueOf(user.get_id()));
        editor.putString(KEY_USER_FIRSTNAME, user.get_firstname());
        editor.putString(KEY_USER_LASTNAME, user.get_lastname());
        editor.putString(KEY_USER_EMAIL, user.get_email());
        editor.putString(KEY_USER_IMAGE, user.get_photo());
        editor.putString(KEY_USER_TYPE, user.get_user_type());
        editor.putString(KEY_USER_ABOUT, user.get_user_about());
        editor.putString(KEY_USER_PHONE, user.get_phone());

        editor.apply();
        return true;
    }

    public User getUser(){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE);

        User user = new User();

        user.set_id(Integer.parseInt(sharedPreferences.getString(KEY_USER_ID, "0")));
        user.set_firstname(sharedPreferences.getString(KEY_USER_FIRSTNAME, null));
        user.set_lastname(sharedPreferences.getString(KEY_USER_LASTNAME, null));
        user.set_email(sharedPreferences.getString(KEY_USER_EMAIL, null));
        user.set_photo(sharedPreferences.getString(KEY_USER_IMAGE, null));
        user.set_user_type(sharedPreferences.getString(KEY_USER_TYPE, null));
        user.set_user_about(sharedPreferences.getString(KEY_USER_ABOUT, null));
        user.set_phone(sharedPreferences.getString(KEY_USER_PHONE, null));

        return user;
    }

    public void clear(){
        SharedPreferences preferences = this.context.getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
