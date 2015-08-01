package com.basic.chat_room.view;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.basic.chat_room.R;


/**
 */
public class LocalActionBar {

    public static final int LEFT_TITLE = 0;
    public static final int MIDDLE_TITLE = 1;
    public static final int RIGHT_LEFT = 2;

    private TextView mLeftTitle;
    private TextView mMideleTitle;
    private TextView mRightTitle;
    ActionBar mActionBar;

    public LocalActionBar(Context context) {
        show(context);
    }

    public static LocalActionBar getLocalBar(Context context) {
        return new LocalActionBar(context);
    }

    public void show(Context context) {
        mActionBar = ((Activity) context).getActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);  //是否可以使用自定义布局
        mActionBar.setDisplayShowHomeEnabled(false); //显示程序图标
        mActionBar.setDisplayShowTitleEnabled(false); //是否显示系统的title
        mActionBar.setDisplayUseLogoEnabled(false);
        //setDisplayShowTitleEnabled和setDisplayShowHomeEnabled共同起作用
//        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
//                | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.local_actionbar, null);
        mLeftTitle = (TextView) view.findViewById(R.id.left_title);
        mRightTitle = (TextView) view.findViewById(R.id.right_title);
        mMideleTitle = (TextView) view.findViewById(R.id.middle_title);
        mActionBar.setCustomView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mActionBar.show();
    }

    /**
     * 设置ActionBar的title.
     *
     * @param title title
     * @param index title 索引
     */
    public void setTitle(String title, int index) {
        switch (index) {
            case LEFT_TITLE:
                mLeftTitle.setText(title);
                break;
            case MIDDLE_TITLE:
                mMideleTitle.setText(title);
                break;
            case RIGHT_LEFT:
                mRightTitle.setText(title);
                break;
            default:
                break;
        }
    }


    /**
     * 隐藏bar.
     */
    public void hideBar() {
        if (mActionBar != null) {
            mActionBar.hide();
        }
    }

    public void setRightTitleOnClickListener(OnClickListener listener) {
        mRightTitle.setOnClickListener(listener);
    }

}
