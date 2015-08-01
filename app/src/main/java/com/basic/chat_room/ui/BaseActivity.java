package com.basic.chat_room.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.basic.chat_room.database.DBHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * activity基础类.
 */
public class BaseActivity extends FragmentActivity {
    private ProgressDialog mProgressDialog;
    private DBHelper mHelper;

    public void dissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
            mProgressDialog = null;
        }
    }

    public DBHelper getDBHelper() {
        if (mHelper == null) {
            mHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mHelper;
    }

    protected void showProgressDialg(boolean isCancle) {
        dissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载数据");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(isCancle);
            //防止在子线程中显示dialog
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.show();
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            OpenHelperManager.releaseHelper();
            mHelper = null;
        }
    }

    public void reLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
