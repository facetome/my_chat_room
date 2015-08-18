package com.basic.chat_room.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.SpannableString;

import com.basic.chat_room.R;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;

import java.util.HashMap;
import java.util.Map;


/**
 * 基础工具类.
 */
public final class Uitity {

    private static ProgressDialog sProgressDialog;
    private static Map<String, Chat> sChatMap = new HashMap<>();
    private Uitity() {

    }

    public static void showProgressDialog(final Context context, final boolean canCancle) {
        if (sProgressDialog == null) {
            sProgressDialog = new ProgressDialog(context);
            sProgressDialog.setCancelable(canCancle);
            sProgressDialog.setCanceledOnTouchOutside(false);
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sProgressDialog.show();
            }
        });
        //这里必须要放在show的后面
        sProgressDialog.setContentView(R.layout.progress_loading_dialog);

    }

    public static void dimissProgressDialog(Context context) {

        if (sProgressDialog != null) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sProgressDialog.dismiss();
                    sProgressDialog = null;
                }
            });

        }
    }

    /**
     * 创建单人聊天室
     *
     * @param context 上下文
     * @param friendName 联系人
     * @return 聊天室
     */
    public static Chat getFriendChat(Context context, String friendName) {
        if (sChatMap.containsKey(friendName)) {
            return sChatMap.get(friendName);
        } else {
            XMPPConnection connection = XmppUtil.getConnection(context);
            Chat chat = connection.getChatManager()
                    .createChat(new StringBuffer()
                            .append(friendName)
                            .append("@")
                            .append(connection.getServiceName())
                            .toString(), null);
            sChatMap.put(friendName, chat);
            return chat;
        }
    }


}
