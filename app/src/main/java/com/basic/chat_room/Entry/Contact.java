package com.basic.chat_room.Entry;

/**
 * Created by basic on 2015/6/16.
 */
public class Contact {
    private boolean mIsAvaliable;
    private String mName;

    /**
     * user jid.
     */
    private String mUserId;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isAvaliable() {
        return mIsAvaliable;
    }

    public void setIsAvaliable(boolean isAvaliable) {
        mIsAvaliable = isAvaliable;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
