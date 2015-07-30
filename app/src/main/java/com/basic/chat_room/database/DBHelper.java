package com.basic.chat_room.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.basic.chat_room.Entry.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by basic on 2015/6/15.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper{
    private static final String DATABASE_NAME = "user_login";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DBHepler";
    private RuntimeExceptionDao<User, Integer> mUserDao;

    /**
     * 构造器.
     *
     * @param context
     */
   public DBHelper(Context context){
          super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.d(TAG, DBHelper.class.getName() + "  onCreate");
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, DBHelper.class.getName() + "  can not create");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.d(TAG, DBHelper.class.getName() + "  ononUpgrade");
            TableUtils.dropTable(connectionSource, User.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, DBHelper.class.getName() + "  can not drop");
        }
    }

    /**
     * 获取用户表DAO.
     *
     * @return RuntimeExceptionDao
     */
    public RuntimeExceptionDao<User, Integer> getUserDao() {
        if (mUserDao == null) {
            mUserDao = getRuntimeExceptionDao(User.class);
        }
        return mUserDao;

    }

}
