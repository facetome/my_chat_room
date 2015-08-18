package com.basic.chat_room.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.basic.chat_room.R;
import com.basic.chat_room.utils.Constants;

import java.util.List;

/**
 * 表情栏.
 */
public class ExpressionGridView extends PopupWindow{
    private Context mContext;
    private List<Integer> mExpressionArray = Constants.mTypeList;
    private View mContentView;
    private int mScreenWidth;
    private int mScreenHeight;
    private GridView mGridView;
    public ExpressionGridView(Context context){
        mContext = context;
        initScreen();
        initContentView();
    }

    private void initContentView(){
        setFocusable(true);
        setWidth(mScreenWidth);
        setOutsideTouchable(true);
        setHeight(mScreenHeight);
        setTouchable(true);
        setBackgroundDrawable(new BitmapDrawable()); //这句话必须加，否则不能响应点击返回按钮
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.expression_gridview, null);
        mGridView = (GridView) mContentView.findViewById(R.id.expression_grid);
        mGridView.setCacheColorHint(Color.TRANSPARENT);
        mGridView.setAdapter(new gridAdapter());
        setContentView(mContentView);
    }

    public void show(){
        showAtLocation(mContentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void setGridItemOnclickListener(OnItemClickListener listener){
        if(listener != null){
            mGridView.setOnItemClickListener(listener);
        }
    }


    private class gridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mExpressionArray == null ? 0: mExpressionArray.size();
        }

        @Override
        public Object getItem(int position) {
            return mExpressionArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image;
            if(convertView == null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.expression_gridview_item, null);
                image = (ImageView) convertView.findViewById(R.id.expression_item_image);
            } else {
                image = (ImageView) convertView.findViewById(R.id.expression_item_image);
            }

            image.setImageResource(mExpressionArray.get(position));
            return convertView;
        }
    }

    private void initScreen(){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels / 3;
    }

}
