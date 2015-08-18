package com.basic.chat_room.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.basic.chat_room.Entry.SingleComunicationDetailEntry;
import com.basic.chat_room.database.DBHelper;
import com.basic.chat_room.ui.BaseComunicationActivity;
import com.basic.chat_room.utils.PreferenceUtil;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * 数据加载器.
 */
public class ComunicationLoader extends AsyncTaskLoader<List<SingleComunicationDetailEntry>>{

    private String mComunicationId;
    private DBHelper mHelper;
    private Context mContext;
    private long mBlockSize;


    public ComunicationLoader(Context context, DBHelper helper, Bundle agrs) {
        super(context);
        mComunicationId = agrs.getString(BaseComunicationActivity.KEY_COMUNICATION_ID);
        mBlockSize= agrs.getLong(BaseComunicationActivity.COMUNICATION_COUNT);
        mHelper = helper;
        mContext = context;
    }


    @Override
    public List<SingleComunicationDetailEntry> loadInBackground() {
        String userName = PreferenceUtil.getLoginUsername(mContext);

        QueryBuilder queryBuilder = mHelper.getSingleComunicationDetailDao().queryBuilder();
        try {
            queryBuilder.where()
                    .eq(SingleComunicationDetailEntry.COLUMN_USER_NAME, userName)
                    .and()
                    .eq(SingleComunicationDetailEntry.COLUMN_CONTACT_NAME, mComunicationId);
            return queryBuilder.orderByRaw(SingleComunicationDetailEntry
                            .COLUMN_TIME_STAMP + " DESC").limit(mBlockSize).query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onReset() {
        super.onReset();
        stopLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        onForceLoad();
    }

    @Override
    public void deliverResult(List<SingleComunicationDetailEntry> data) {
        super.deliverResult(data);
    }
}

