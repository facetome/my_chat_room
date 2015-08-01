package com.basic.chat_room.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.basic.chat_room.Entry.SingleComunicationDetailEntry;
import com.basic.chat_room.database.DBHelper;
import com.basic.chat_room.utils.PreferenceUtil;
import com.basic.chat_room.utils.XmppUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by acer on 2015/7/23.
 */
public class ChatService extends Service {

    private DBHelper mHelper;
    private SingleChatMangerListener mChatListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mHelper == null) {
            mHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        mChatListener = new SingleChatMangerListener();
        XmppUtil.getConnection(this).getChatManager().addChatListener(mChatListener);
        return Service.START_STICKY;
    }


    private class SingleChatMangerListener implements ChatManagerListener {
        //创建单人聊天室
        @Override
        public void chatCreated(Chat chat, boolean b) {
            chat.addMessageListener(new MessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    String contactName = message.getFrom();
                    String body = message.getBody();
                    SingleComunicationDetailEntry entry = new SingleComunicationDetailEntry();
                    entry.setUserName(PreferenceUtil.getLoginUsername(ChatService.this));
                    entry.setMessage(body);
                    entry.setTimeStamp(System.currentTimeMillis());
                    entry.setContact(contactName);
                    entry.setDataPosition(SingleComunicationDetailEntry.DATA_LEFT);
                    mHelper.getSingleComunicationDetailDao().createOrUpdate(entry);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        XmppUtil.getConnection(this).getChatManager().removeChatListener(mChatListener);
        OpenHelperManager.releaseHelper();
        super.onDestroy();

    }
}
