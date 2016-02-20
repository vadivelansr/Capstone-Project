package com.akhooo.coofde.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vadivelansr on 1/6/2016.
 */
public class CoofdePrefManager {
    private static CoofdePrefManager instance;
    private SharedPreferences sharedPreferences;

    private static final String KEY_USERNAME = appendKeyPrefix("key_username");

    public static CoofdePrefManager getInstance(){
        try{
            if(instance == null)
                instance = new CoofdePrefManager();
            return instance;
        }finally {

        }
    }

    public void initialize(Context paramContext){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
    }

    private static String appendKeyPrefix(String paramString) {
        return "com.akhooo.coofde.app." + paramString;
    }

    public void setUsername(String username){
        this.sharedPreferences.edit().putString(KEY_USERNAME, username).commit();
    }
    public String getUsername(){
        return this.sharedPreferences.getString(KEY_USERNAME, "");
    }


}
