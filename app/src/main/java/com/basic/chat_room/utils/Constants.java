package com.basic.chat_room.utils;

import android.util.SparseArray;

import com.basic.chat_room.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量基类.
 */
public class Constants {

    public static List<Integer> mTypeList = new ArrayList<>();
    private static SparseArray<String> mExpressionArray = new SparseArray<>();
    private static Map<String, Integer> mTypeMap = new HashMap<>();

    static {
        mTypeList.add(R.drawable.ee_1);
        mTypeList.add(R.drawable.ee_10);
        mTypeList.add(R.drawable.ee_11);
        mTypeList.add(R.drawable.ee_12);
        mTypeList.add(R.drawable.ee_13);
        mTypeList.add(R.drawable.ee_14);
        mTypeList.add(R.drawable.ee_15);
        mTypeList.add(R.drawable.ee_16);
        mTypeList.add(R.drawable.ee_17);
        mTypeList.add(R.drawable.ee_18);
        mTypeList.add(R.drawable.ee_19);
        mTypeList.add(R.drawable.ee_2);
        mTypeList.add(R.drawable.ee_20);
        mTypeList.add(R.drawable.ee_21);
        mTypeList.add(R.drawable.ee_22);
        mTypeList.add(R.drawable.ee_23);
        mTypeList.add(R.drawable.ee_24);
        mTypeList.add(R.drawable.ee_25);
        mTypeList.add(R.drawable.ee_26);
        mTypeList.add(R.drawable.ee_27);
        mTypeList.add(R.drawable.ee_28);
        mTypeList.add(R.drawable.ee_29);
        mTypeList.add(R.drawable.ee_3);
        mTypeList.add(R.drawable.ee_30);
        mTypeList.add(R.drawable.ee_31);
        mTypeList.add(R.drawable.ee_32);
        mTypeList.add(R.drawable.ee_33);
        mTypeList.add(R.drawable.ee_34);
        mTypeList.add(R.drawable.ee_35);

        for (int i=0; i< mTypeList.size();i++){
            StringBuffer expression = new StringBuffer();
            expression.append("[exp").append(i).append("]");
            mExpressionArray.append(mTypeList.get(i), expression.toString());
            mTypeMap.put(expression.toString(), mTypeList.get(i));
        }

    }

    /**
     * 通过传入图片的资源id获取到表情图片的转义描述符.
     *
     * @param resId 资源id
     * @return 表情描述符
     */
    public static String getExpression(int resId) {
       return mExpressionArray.get(resId);
    }

    /**
     * 根据转义符获取图片id.
     *
     * @param express
     * @return 图片资源id
     */
    public static int getExpressionId(String express) {
        return mTypeMap.get(express);
    }
}
