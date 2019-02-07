package com.ziffytech.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by devloper on 21/9/17.
 */

public class SaveSharedPreference{


    static final String PREF_REF_CODE= "referral";





    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setPrefRefCode(Context ctx, String ref_code) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_REF_CODE, ref_code);
        editor.commit();
    }

    public static String getPrefRefCode(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_REF_CODE, "");
    }





    public static void clearUserSession(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }


}