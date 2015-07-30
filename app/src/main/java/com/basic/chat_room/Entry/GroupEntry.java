package com.basic.chat_room.Entry;

/**
 * Created by basic on 2015/7/18.
 */
public class GroupEntry {
    private String mGroupName;
    private int mTotalNum;
    private int mOnlineNum;

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public int getTotalNum() {
        return mTotalNum;
    }

    public void setTotalNum(int totalNum) {
        mTotalNum = totalNum;
    }

    public int getOnlineNum() {
        return mOnlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        mOnlineNum = onlineNum;
    }
}
