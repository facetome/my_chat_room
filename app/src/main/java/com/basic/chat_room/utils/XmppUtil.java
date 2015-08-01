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
    private static Context sContext;

    private static XMPPConnection sConnection;

    static {
        Connection.DEBUG_ENABLED = true;
    }

    public static XMPPConnection getConnection(Context context) {
         sContext = context;
        if (sConnection == null) {
            openConnection();
        }
        return sConnection;
    }

    private static void openConnection() {
        ConnectionConfiguration configuration = new ConnectionConfiguration(SERVER_IP, SERVER_PORT);
        configuration.setSASLAuthenticationEnabled(false); //是否启用安全验证
        sConnection = new XMPPConnection(configuration);
        try {
            sConnection.connect(); //开始连接
        } catch (XMPPException e) {
            e.printStackTrace();
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    public static void dissConnection() {
        if (sConnection != null) {
            sConnection.disconnect();
            sConnection = null;
        }
    }
}
