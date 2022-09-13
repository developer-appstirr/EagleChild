package com.example.eagle_child.helpers.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;
    protected static final String KEY_LOGIN_STATUS = "is_login";
    protected static final String KEY_LOGIN_TYPE = "is_type";
    protected static final String KEY_USER = "user";
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String AUTHENTICATE_USER_TOKEN = "user_token";
    public static final String BIOMETRIC_USER_PHONE = "phone";
    public static final String AUTHENTICATE_USER_REFRESH_TOKEN = "user_refresh_token";
    private static final String FILENAME = "file_preferences";
    protected static final String KEY_DEVICE_SEND_STATUS = "is_device_info_send";
    protected static final String KEY_TYPE_FITNESS = "is_type_fitness";
    protected static final String  FIRST_ONBOARDING_STATUS = "isOnBoarding";
    protected static final String  ACCESSIBLITY = "isEnabled";

    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean isLogin) {
        putBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS, isLogin);
    }


    public void setLoginType(String isType) {
        putStringPreference(context, FILENAME, KEY_LOGIN_TYPE, isType);
    }

    public void setOnBoardingStatus(boolean isBoarding) {
        putBooleanPreference(context, FILENAME, FIRST_ONBOARDING_STATUS, isBoarding);
    }
    public boolean getOnBoardingStatus() {
        return getBooleanPreference(context, FILENAME, FIRST_ONBOARDING_STATUS);
    }


    public void setStringPrefrence(String key, String value) {
        putStringPreference(context, FILENAME, key, value);
    }

    public String getStringPrefrence(String key) {
        return getStringPreference(context, FILENAME, key);
    }


    public void setIntegerPrefrence(String key, int value) {
        putIntegerPreference(context, FILENAME, key, value);
    }

    public int getIntegerPrefrence(String key) {
        return getIntegerPreference(context, FILENAME, key);
    }


    public void setBooleanPrefrence(String Key, boolean value) {
        putBooleanPreference(context, FILENAME, Key, value);
    }


    public boolean getBooleanPrefrence(String Key) {
        return getBooleanPreference(context, FILENAME, Key);
    }


    public void setDeviceInfoSendStatus(boolean isDeviceInfoSentToServer) {
        putBooleanPreference(context, FILENAME, KEY_DEVICE_SEND_STATUS, isDeviceInfoSentToServer);
    }


    public boolean getDeviceInfoSendStatus() {
        return getBooleanPreference(context, FILENAME, KEY_DEVICE_SEND_STATUS);
    }

    public boolean getLoginStatus() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }

    public String getLoginType() {
        return getStringPrefrence(KEY_LOGIN_TYPE);
    }

    public void putDeviceToken(String token) {
        putStringPreference(context, FILENAME, KEY_DEVICE_TOKEN, token);
    }


    public String getDeviceToken() {
        return getStringPreference(context, FILENAME, KEY_DEVICE_TOKEN);
    }


    public void putUserRefreshToken(String token) {
        putStringPreference(context, FILENAME, AUTHENTICATE_USER_REFRESH_TOKEN, token);
    }


    public String getUserRefreshToken() {
        return getStringPreference(context, FILENAME, AUTHENTICATE_USER_REFRESH_TOKEN);
    }

    public void putUserToken(String token) {
        putStringPreference(context, FILENAME, AUTHENTICATE_USER_TOKEN, token);
    }

    public String getUserToken() {
        return getStringPreference(context, FILENAME, AUTHENTICATE_USER_TOKEN);
    }


    public void setLoginFitnessType(String type) {
        putStringPreference(context, FILENAME, KEY_TYPE_FITNESS, type);
    }

    public String getLoginFitnessType() {
        return getStringPreference(context, FILENAME, KEY_TYPE_FITNESS);
    }

//
//   public void putUser(User user) {
//        putStringPreference(context,
//                FILENAME,
//                KEY_USER,
//                new GsonBuilder()
//                        .create()
//                        .toJson(user));
//    }
//
//    public User getUser() {
//        return new GsonBuilder().create().fromJson(
//                getStringPreference(context, FILENAME, KEY_USER), User.class);
//    }


    public void putPhone(String phone) {
        putStringPreference(context, FILENAME, BIOMETRIC_USER_PHONE, phone);
    }

    public String getPhone() {
        return getStringPreference(context, FILENAME, BIOMETRIC_USER_PHONE);
    }




    public void removeLoginPreference() {
        setLoginStatus(false);
        removePreference(context, FILENAME, KEY_USER);
        removePreference(context, FILENAME, KEY_LOGIN_STATUS);
        removePreference(context, FILENAME, KEY_DEVICE_TOKEN);
        removePreference(context, FILENAME, AUTHENTICATE_USER_TOKEN);
        removePreference(context, FILENAME, AUTHENTICATE_USER_REFRESH_TOKEN);
    }


    public void removeUserPreference() {
        removePreference(context, FILENAME, KEY_USER);
    }


    public void setAccessibility(boolean isEnabled) {
        putBooleanPreference(context, FILENAME, ACCESSIBLITY, isEnabled);
    }
    public boolean getAccessibility() {
        return getBooleanPreference(context, FILENAME, ACCESSIBLITY);
    }


}
