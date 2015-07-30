package com.basic.chat_room.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.basic.chat_room.R;


/**
 * 基础工具类.
 */
public class Uitity {

    private static ProgressDialog mProgressDialog;

    private Uitity(){

    }

    public static void showProgressDialog(final Context context,  final boolean canCancle) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCancelable(canCancle);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.show();
            }
        });
        //这里必须要放在show的后面
        mProgressDialog.setContentView(R.layout.progress_loading_dialog);

    }

    public static void dimissProgressDialog(Context context) {

        if (mProgressDialog != null) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            });

        }
    }
}
