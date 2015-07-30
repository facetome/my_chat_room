package com.basic.chat_room.utils;


import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XmppUtil {
    private static final String SERVER_IP = "192.168.0.6";
    private static final int SERVER_PORT = 5222;
    private static final String TAG = XmppUtil.class.getSimpleName();
    private static Context mContext;

    private static XMPPConnection mConnection;

    static {
        Connection.DEBUG_ENABLED = true;
    }

    public static XMPPConnection getConnection(Context context) {
         mContext = context;
        if (mConnection == null) {
            openConnection();
        }
        return mConnection;
    }

    private static void openConnection() {
        ConnectionConfiguration configuration = new ConnectionConfiguration(SERVER_IP, SERVER_PORT);
        configuration.setSASLAuthenticationEnabled(false);//是否启用安全验证
        mConnection = new XMPPConnection(configuration);
        try {
            mConnection.connect(); //开始连接
        } catch (XMPPException e) {
            e.printStackTrace();
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    public static void dissConnection() {
        if (mConnection != null) {
            mConnection.disconnect();
            mConnection = null;
        }
    }
}
