package com.basic.chat_room.Entry;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "user")
public class User {

    public static final String USER_ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "password";
    public static final String LGOIN_AUTO = "login_auto";
    public static final String REMEMBER_PASSWORD = "remeber_password";
    public static final String LAST_TIME = "last_time";

     //id是为自增长，每插入一条数据，id自动加1
    @DatabaseField(canBeNull = false, columnName = "_id", generatedId = true)//是否是自增长
    int mUserId;

    @DatabaseField(canBeNull = false, columnName = USER_NAME)//用户名唯一
    String mUserName;

    @DatabaseField(canBeNull = false, columnName = USER_PASSWORD)
    String mPassword;

    @DatabaseField(columnName = LGOIN_AUTO)
    boolean mAutoLogin;

    @DatabaseField(columnName = REMEMBER_PASSWORD)
    boolean mRemeberPassword;

    @DatabaseField(columnName = LAST_TIME, canBeNull = false)
    long mLastCreateTime;
    
    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean isAutoLogin() {
        return mAutoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        mAutoLogin = autoLogin;
    }

    public boolean isRemeberPassword() {
        return mRemeberPassword;
    }

    public void setRemeberPassword(boolean remeberPassword) {
        mRemeberPassword = remeberPassword;
    }

    public long getLastCreateTime() {
        return mLastCreateTime;
    }

    public void setLastCreateTime(long lastCreateTime) {
        mLastCreateTime = lastCreateTime;
    }
}
