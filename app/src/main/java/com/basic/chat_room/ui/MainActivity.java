package com.basic.chat_room.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.basic.chat_room.R;
import com.basic.chat_room.utils.XmppUtil;
import com.basic.chat_room.view.LocalActionBar;
import com.basic.chat_room.view.TabContent;

/**
 * Created by basic on 2015/6/15.
 */
public class MainActivity extends BaseActivity {
    private FragmentTabHost mTabHost;
    private static final long EXIT_DELAY_TIME = 4000;
    private long mFirstTime;
    public LocalActionBar mActionBar;

    private OnTabChangeListener mChangeListener = new OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
           mActionBar.setTitle(tabId, LocalActionBar.MIDDLE_TITLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initActionBar();
        initFragment();
    }

    private void initFragment() {
        mTabHost = (FragmentTabHost) findViewById(R.id.main_tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_fragment);
        mTabHost.setOnTabChangedListener(mChangeListener);
        setTabItme(SingleFragment.LABEL_ICON,
                getString(SingleFragment.LABLE_NAME), getString(R.string.label_single_point),
                SingleFragment.class);

        setTabItme(GroupFragment.LABLE_ICON,
                getString(GroupFragment.LABLE_NAME), getString(R.string.label_group), GroupFragment.class);
    }

    private void setTabItme(int icon, String spec, String label, Class c) {
        TabContent tabContent = new TabContent(this);
        tabContent.setLabel(label);
        tabContent.setIcon(icon);
        TabSpec tabSpec = mTabHost.newTabSpec(spec);
        tabSpec.setIndicator(tabContent);
        mTabHost.addTab(tabSpec, c, null);

    }

    private void initActionBar() {
        mActionBar = LocalActionBar.getLocalBar(this);
        mActionBar.setTitle(getString(R.string.switch_account_title), LocalActionBar.RIGHT_LEFT);
        mActionBar.setRightTitleOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disConnection();
                reLogin();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
//        在onDestory()中进行断开连接没有用！！
//        disConnection();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        long exitTime = System.currentTimeMillis();
        if (exitTime - mFirstTime < EXIT_DELAY_TIME) {
            finish();
            disConnection();
        } else {
            Toast.makeText(this, getString(R.string.message_exit_program), Toast.LENGTH_SHORT).show();
        }
        mFirstTime = exitTime;
    }

    private void disConnection(){
        XmppUtil.dissConnection();
    }
}
