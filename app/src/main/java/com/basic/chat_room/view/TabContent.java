package com.basic.chat_room.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basic.chat_room.R;

/**
 * Created by basic on 2015/6/16.
 */
public class TabContent extends LinearLayout {

    private TextView mNoticationNum;
    private ImageView mIcon;
    private TextView mLabel;

    public TabContent(Context context) {
        this(context, null);
    }

    public TabContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tabcontent_item, this);
        mNoticationNum = (TextView) view.findViewById(R.id.noticaiton_num);
        mLabel = (TextView) view.findViewById(R.id.tab_label);
        mIcon = (ImageView) findViewById(R.id.tab_icon);
    }

    public void setNoticationNum(int num) {
        mNoticationNum.setText(String.valueOf(num));
    }

    public void setIcon(int id) {
        mIcon.setImageResource(id);
    }

    public void setLabel(String label) {
        mLabel.setText(label);
    }
}
