package util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pranav & Sarfaraj on 25/06/16.
 */
public class AppPreference {

    public static final String PREF_SENT_EMAIL = "PrefSentEmail";
    public static final String PREF_IS_LOGIN = "prefIsLogin";
    public static final String PREF_USERID = "prefUserId";
    public static final String PREF_NAME = "BankSoftware";
    public static final String PREF_USERNAME = "prefuserName";
    public static final String PREF_NAME_MAIN_CATEGORY_SELECTED = "prefcategorySelcted";
    public static final String PREF_NAME_SUB_CATEGORY_SELECTED = "prefcategorySelctedSub";

    public static final class PREF_KEY {
        public static final String NEW_EMAIL = "email";
        public static final String LOGIN_STATUS = "loginstatus";
        public static final String USERID = "userid";
        public static final String USERNAME = "username";
        public static final String REGID = "regId";
    }

    public static final void setStringPref(Context context, String prefKey, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(prefKey, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static final String getStringPref(Context context, String prefName, String key) {
        SharedPreferences sp = context.getSharedPreferences(prefName, 0);
        return sp.getString(key, "");
    }

    public static final String getPrefCategoryPos(Context context, String prefName,
                                                  String key) {
        SharedPreferences sp = context.getSharedPreferences(prefName, 0);
        return sp.getString(key, "0");
    }

    public static final void setPrefCategoryPos(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_MAIN_CATEGORY_SELECTED, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static final void setPrefCategoryPosSub(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME_SUB_CATEGORY_SELECTED, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }
}
