package com.basic.chat_room.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.basic.chat_room.Entry.User;
import com.basic.chat_room.R;
import com.basic.chat_room.utils.DBHelperUtils;
import com.basic.chat_room.utils.Uitity;
import com.basic.chat_room.utils.XmppUtil;
import com.j256.ormlite.stmt.QueryBuilder;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import java.sql.SQLException;


public class LoginActivity extends BaseActivity implements OnClickListener {
    private static final String AUTHOR_ERROR_CODE = "401";

    private XMPPConnection mConnection;
    private EditText mPassword;
    private EditText mUsername;
    private Button mLogin;
    private Button mRegister;
    private CheckBox mRemeberPassword;
    private CheckBox mAutoLogin;
    private static final int CONNECT_SUCCESS = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case CONNECT_SUCCESS:
                    initData();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //    getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Uitity.showProgressDialog(this, false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mConnection = XmppUtil.getConnection(LoginActivity.this);
                if (!mConnection.isConnected()) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "连接错误，稍后重试.",
                                    Toast.LENGTH_SHORT).show();
                            Uitity.dimissProgressDialog(LoginActivity.this);
                        }
                    });
                    finish();
                } else {
                    Uitity.dimissProgressDialog(LoginActivity.this);
                    mHandler.sendEmptyMessage(CONNECT_SUCCESS);
                }
            }
        });
        thread.start();
    }

    private void initData() {
        //开始初始化连接
        mPassword = (EditText) findViewById(R.id.value_password);
        mUsername = (EditText) findViewById(R.id.value_username);
        mLogin = (Button) findViewById(R.id.login_btn);
        mRegister = (Button) findViewById(R.id.register_btn);
        mRemeberPassword = (CheckBox) findViewById(R.id.remeber_password);
        mAutoLogin = (CheckBox) findViewById(R.id.login_auto);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        setUserInfo();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.login_btn:
                if (login()) {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.register_btn:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private boolean login() {
        String password = mPassword.getEditableText().toString();
        String username = mUsername.getEditableText().toString();
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            //执行登陆操作
            try {
                loginWithXmpp(username, password);
                //保存信息
                boolean isAutoLogin = mAutoLogin.isChecked();
                boolean isRemberPassword = mRemeberPassword.isChecked();
                User user = new User();
                user.setPassword(password);
                user.setUserName(username);
                user.setAutoLogin(isAutoLogin);
                user.setRemeberPassword(isRemberPassword);
                user.setLastCreateTime(System.currentTimeMillis()); //按时间排序
                DBHelperUtils.createOrUpdateUser(getDBHelper().getUserDao(), user); //插入或者更新数据库
                return true;
            } catch (XMPPException e) {
                e.printStackTrace();
                String error = e.getMessage();
                if (error.contains(AUTHOR_ERROR_CODE)) {
                    Toast.makeText(this, getString(R.string.message_user_not_exits),
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        } else {
            Toast.makeText(this, getString(R.string.message_login_not_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void loginWithXmpp(String username, String password) throws XMPPException {
        if (mConnection != null) {
            mConnection.login(username, password);
            //此时应该代表的是登陆成功,如果么有成功，将会抛出异常
            //登陆成功后发送在线状态
            Presence presence = new Presence(Type.available);
            mConnection.sendPacket(presence);
        }

    }

    private void setUserInfo() {
        try {
            QueryBuilder<User, Integer> builder = getDBHelper().getUserDao().queryBuilder();
            builder.orderByRaw(User.LAST_TIME + " DESC");
            Toast.makeText(this, builder.query().size()+"", Toast.LENGTH_SHORT).show();
            User user = builder.queryForFirst();
            if (user != null) {
                mUsername.setText(user.getUserName());
                mAutoLogin.setChecked(user.isAutoLogin());
                mRemeberPassword.setChecked(user.isRemeberPassword());
                if (user.isRemeberPassword()) {
                    mPassword.setText(user.getPassword());
                }
                if (user.isAutoLogin()) {
                    if (login()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
