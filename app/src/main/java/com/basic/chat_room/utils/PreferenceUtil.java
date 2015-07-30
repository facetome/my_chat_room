package com.basic.chat_room.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * preference的工具类.
 */
public class PreferenceUtil {
    private static final String LOGIN_USERNAME = "username";
    private static final String LOGIN_PASSWORD = "password";

    /**
     * 保存用户信息.
     *
     * @param context  上下文
     * @param username 用户名
     * @param password 密码
     */
    public static void saveLoginInfo(Context context, String username, String password) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LOGIN_USERNAME, username)
                .putString(LOGIN_PASSWORD, password)
                .apply();
    }

    /**
     * 获取用户名.
     *
     * @param context 上下文
     * @return username
     */
    public static String getLoginUsername(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LOGIN_USERNAME, "");
    }

    /**
     * 获取用户密码.
     *
     * @param context 上下文
     * @return password
     */
    public static String getLoginPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LOGIN_PASSWORD, "");
    }
}
