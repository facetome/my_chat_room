package com.basic.chat_room.ui;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.basic.chat_room.Entry.SingleComunicationDetailEntry;
import com.basic.chat_room.Loader.ComunicationLoader;
import com.basic.chat_room.Loader.ComunicationSaver;
import com.basic.chat_room.R;
import com.basic.chat_room.adapter.ComunicationAdapter;
import com.basic.chat_room.utils.PreferenceUtil;
import com.basic.chat_room.utils.Uitity;
import com.basic.chat_room.view.LocalActionBar;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 聊天室界面基类.
 */
public class BaseComunicationActivity extends BaseActivity implements LoaderCallbacks, OnClickListener {

    private PullToRefreshListView mPullListView;
    //聊天室的唯一标识，单人聊天是用户的名称 ,多人聊天则是聊天室的名称；
    //在数据库中为了保持数据的唯一性，所以查找的条件为：用户名+comunication_id的方式进行查找
    public static final String KEY_COMUNICATION_ID = "comunication_id";
    public static final String COMUNICATION_COUNT = "comunication_count";
    private static final String TAG = "BaseComunicationActivity";
    private LoaderManager mLoaderManger;
    private static final int LOAER_ID = 1;
    private static final int SAVER_ID = 2;
    private ComunicationAdapter mAdapter;
    private List<SingleComunicationDetailEntry> mData = new ArrayList<>();
    private String mComunicationId;
    private static final long COMUNICATION_PAGE_SIZE = 10;
    private SingleComunicationDetailEntry mEntry;
    private Chat mContactChat;
    //判断是否下来刷新
    private boolean mIsPullDown = false;

    private EditText mInput;
    private Button mSendMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_room);
        initView();
        initData();
        initActionBar();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Loader loader = null;
        switch (id) {
            case LOAER_ID:
                loader = new ComunicationLoader(this, getDBHelper(), args);
                break;
            case SAVER_ID:
                loader = new ComunicationSaver(this, getDBHelper(), getSaveBundle(true), mEntry);
                break;
            default:
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader.getId() == LOAER_ID) {
            dispatchData((List<SingleComunicationDetailEntry>) data);
        } else if (loader.getId() == SAVER_ID) {
            mLoaderManger.restartLoader(LOAER_ID, getSendLoadBundle(), this);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        loader.reset();
    }

    private void initData() {
        mComunicationId = getIntent().getStringExtra(KEY_COMUNICATION_ID);
        mLoaderManger = getLoaderManager();

        mLoaderManger.initLoader(LOAER_ID, getPullLoadBundle(), this);

        // 初始化聊天室
        mContactChat = Uitity.getFriendChat(this, mComunicationId);
    }

    private void initActionBar() {
        LocalActionBar actionBar = LocalActionBar.getLocalBar(this);
        actionBar.setTitle(mComunicationId, LocalActionBar.MIDDLE_TITLE);
    }


    private void initView() {
        mInput = (EditText) findViewById(R.id.input_edit);
        mSendMsg = (Button) findViewById(R.id.send_btn);
        mSendMsg.setOnClickListener(this);
        mPullListView = (PullToRefreshListView) findViewById(R.id.communication_listview);
        mPullListView.setMode(Mode.PULL_FROM_START);  //只能进行下拉刷新，不存在上拉刷新.
        ILoadingLayout pullDownLayout = mPullListView.getLoadingLayoutProxy(true, false);
        pullDownLayout.setRefreshingLabel(getString(R.string.lable_update_refresh));

        mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
               mIsPullDown = true;
                //下拉刷新
                mLoaderManger.restartLoader(LOAER_ID, getPullLoadBundle(), BaseComunicationActivity.this);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                //上啦属性
            }
        });
        mAdapter = new ComunicationAdapter(this);
        mPullListView.getRefreshableView().setAdapter(mAdapter);


    }

    @Override
    public void onClick(View v) {
        String message = mInput.getEditableText().toString();
        if (!TextUtils.isEmpty(message)) {
            sendMessage(message);
            mInput.setText("");
        } else {
            Toast.makeText(this, getString(R.string.msg_no_message), Toast.LENGTH_SHORT).show();
        }
    }

    //发送消息
    private void sendMessage(String message) {
        mEntry = new SingleComunicationDetailEntry();
        mEntry.setContact(mComunicationId);
        mEntry.setDataPosition(SingleComunicationDetailEntry.DATA_RIGHT);
        mEntry.setMessage(message);
        mEntry.setTimeStamp(System.currentTimeMillis());
        mEntry.setUserName(PreferenceUtil.getLoginUsername(this));
        mLoaderManger.restartLoader(SAVER_ID, new Bundle(), this);
        if (mContactChat != null) {
            try {
                mContactChat.sendMessage(message);
            } catch (XMPPException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.msg_send_message_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchData(List<SingleComunicationDetailEntry> data) {
        if (mPullListView.isRefreshing()) {
            mPullListView.onRefreshComplete();
        }
        // todo
        if (data != null && data.size() >=1 && data.get(data.size() - 1).getId() ==
                getInformationMinId()) {
            if (mIsPullDown) {
                Toast.makeText(this, getString(R.string.msg_no_more_data), Toast.LENGTH_SHORT).show();
                mIsPullDown = false;
            }
        } else {
            if (data != null) {
                mData = data;
                //对list进行排序
                Collections.sort(mData, new Comparator<SingleComunicationDetailEntry>() {
                    @Override
                    public int compare(SingleComunicationDetailEntry lhs, SingleComunicationDetailEntry rhs) {
                        return new Long(lhs.getTimeStamp()).compareTo(new Long(rhs.getTimeStamp()));
                    }
                });
            }
            mAdapter.onRefresh(mData);
        }

    }

    private Bundle getPullLoadBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COMUNICATION_ID, mComunicationId);
        bundle.putLong(COMUNICATION_COUNT, COMUNICATION_PAGE_SIZE + mData.size());
        return bundle;
    }

    private Bundle getSendLoadBundle(){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COMUNICATION_ID, mComunicationId);
        bundle.putLong(COMUNICATION_COUNT, COMUNICATION_PAGE_SIZE );
        return bundle;
    }


    //封装保存数据加载器.
    //是否删除以前的数据
    private Bundle getSaveBundle(boolean isDelete){
        Bundle bundle = new Bundle();
        bundle.putBoolean(ComunicationSaver.DELETE_OLD_DATA, isDelete);
        return bundle;
    }

    private long getInformationMinId() {
        QueryBuilder<SingleComunicationDetailEntry, Integer> builder = getDBHelper()
                .getSingleComunicationDetailDao().queryBuilder();
        try {
            builder.where()
                    .eq(SingleComunicationDetailEntry.COLUMN_CONTACT_NAME, mComunicationId)
                    .and()
                    .eq(SingleComunicationDetailEntry.COLUMN_USER_NAME, PreferenceUtil.getLoginUsername(this));
            SingleComunicationDetailEntry entry = builder.queryForFirst();
            if (entry != null) {
                return entry.getId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      return 0L;
    }


}
