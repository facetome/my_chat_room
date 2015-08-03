package com.basic.chat_room.utils;

import com.basic.chat_room.Entry.User;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Iterator;
import java.util.List;

/**
 * 数据库工具类.
 */
public class DBHelperUtils {
    /**
     * 根据自增长索引更新或创建一条数据.
     *
     * @param dao  dao
     * @param data data
     */
    public static void createOrUpdateUser(RuntimeExceptionDao<User, Integer> dao, User data) {
        if (dao != null) {
            List<User> list = dao.queryForAll();
            if (list != null && list.size() != 0) {
                Iterator<User> iterator = list.iterator();
                while (iterator.hasNext()) {
                    User user = iterator.next();
                    if (user.getUserName().equals(data.getUserName())) {
                        data.setUserId(user.getUserId());
                        dao.createOrUpdate(data);
                        return;
                    }
                }
                dao.createOrUpdate(data);
            }
            dao.createOrUpdate(data);
        }
    }
}
