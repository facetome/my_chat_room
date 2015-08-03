package com.basic.chat_room.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.basic.chat_room.Entry.SingleComunicationDetailEntry;
import com.basic.chat_room.database.DBHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.Where;

/**
 * Created by acer on 2015/7/21.
 */
public class ComunicationSaver extends AsyncTaskLoader<Boolean>{
    private SingleComunicationDetailEntry mEntry;
    private Bundle mBundle;
    private DBHelper mHelper;
    public static final String DELETE_OLD_DATA = "delete_old_data";
    public ComunicationSaver(Context context, DBHelper helper, Bundle bundle, SingleComunicationDetailEntry entry) {
        super(context);
        mEntry = entry;
        mHelper = helper;
        mBundle = bundle;
    }

    @Override
    public Boolean loadInBackground() {
        Log.d("basic_wangliangsen", "loadInBackground");
        boolean isDelete = mBundle.getBoolean(DELETE_OLD_DATA);
        if (mEntry != null){
            //根据时间排序删除数据
            if (mHelper != null){
                RuntimeExceptionDao<SingleComunicationDetailEntry, Integer> runtimeExceptionDao = mHelper.getSingleComunicationDetailDao();
                if (isDelete){
                    //TODO
                }
                Log.d("basic_wangliangsen", "保存数据");
                runtimeExceptionDao.createOrUpdate(mEntry);
            }
        }
        return true;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        onForceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
    }

    @Override
    public void deliverResult(Boolean data) {
        super.deliverResult(data);
    }
}
