package com.basic.chat_room.Entry;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 聊天数据实体.
 */

@DatabaseTable(tableName = "comunication")
public class SingleComunicationDetailEntry {

    public static final int DATA_LEFT = 0;
    //右数据实体,代表是我方数据
    public static final int DATA_RIGHT = 1;
    public static final String COLUMN_CONTACT_NAME = "contact";
    public static final String  COLUMN_USER_NAME  = "user_name";
    public static final String COLUMN_TIME_STAMP = "time_stamp";

    @DatabaseField(canBeNull = false, generatedId = true, columnName = "_id")
    private int mId;
    @DatabaseField(canBeNull = false, columnName = "position")
    private int mDataPosition;

    @DatabaseField(columnName = "message")
    private String mMessage;

    @DatabaseField(columnName = COLUMN_TIME_STAMP)
    private long mTimeStamp;

    @DatabaseField(columnName = COLUMN_CONTACT_NAME)
    private String mContact;

    @DatabaseField(columnName = COLUMN_USER_NAME)
    private String mUserName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getDataPosition() {
        return mDataPosition;
    }

    public void setDataPosition(int dataPosition) {
        mDataPosition = dataPosition;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
