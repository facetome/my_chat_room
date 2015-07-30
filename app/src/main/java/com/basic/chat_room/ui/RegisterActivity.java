package com.basic.chat_room.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basic.chat_room.R;
import com.basic.chat_room.utils.XmppUtil;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Registration;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity {
    private EditText mPassword;
    private EditText mUsername;
    private EditText mConfirmPassword;
    private Button mConfirm;
    private IQ mResult;

    private static final String REGISTER_KEY = "android";
    private static final String REGISTER_VALUE = "geolo_createUser_android";
    private static final String TAG = "RegisterActivity";
    private static final String ACCOUNT_EXITS = "conflict(409)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initData();
    }

    private void initData() {
        mPassword = (EditText) findViewById(R.id.value_password);
        mUsername = (EditText) findViewById(R.id.value_username);
        mConfirmPassword = (EditText) findViewById(R.id.value_confirm_password);
        mConfirm = (Button) findViewById(R.id.confirm_btn);
        mConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassword.getEditableText().toString();
                String username = mUsername.getEditableText().toString();
                if (isFactorReady(username, password)) {
                    register(username, password);
                }
            }
        });
    }

    private boolean isFactorReady(String username, String password) {
        String confirmPassword = mConfirmPassword.getEditableText().toString();
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(confirmPassword)) {
            if (password.equals(confirmPassword)) {
                return true;
            } else {
                Toast.makeText(this, getString(R.string.message_password_not_confirm), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, getString(R.string.message_login_not_empty), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void register(String username, String password) {
        XMPPConnection connection = XmppUtil.getConnection(this);
        Registration registration = new Registration();
        registration.setType(Type.SET);
        //设置连接的服务器
        registration.setTo(connection.getServiceName());
        registration.setRegistered(true);
        registration.setUsername(username);
        registration.setPassword(password);
        registration.addAttribute(REGISTER_KEY, REGISTER_VALUE);
        PacketFilter filter = new AndFilter(new PacketIDFilter(registration.getPacketID()),
                new PacketTypeFilter(IQ.class));
        PacketCollector collector = connection.createPacketCollector(filter);
        connection.sendPacket(registration);
        mResult = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();//停止请求result(是否成功的标志)
        if (mResult != null) {
            if (mResult.getType() == Type.ERROR) {
                if (mResult.getError().toString().equalsIgnoreCase(ACCOUNT_EXITS)) {
                    Toast.makeText(this, getString(R.string.message_account_exits),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.message_register_error),
                            Toast.LENGTH_SHORT).show();
                }

            } else if (mResult.getType() == Type.RESULT) {//注册成功
                Toast.makeText(this, getString(R.string.message_register_success),
                        Toast.LENGTH_SHORT).show();
                finish();

            }
        } else {
            //此时服务器没有返回的结果
            Log.d(TAG, "服务器么有返回结果>>>>>>>>>>>>>>>>>>>>>>>");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
