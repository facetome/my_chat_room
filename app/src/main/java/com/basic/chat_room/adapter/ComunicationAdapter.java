package com.basic.chat_room.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.chat_room.Entry.SingleComunicationDetailEntry;
import com.basic.chat_room.R;
import com.basic.chat_room.utils.Constants;
import com.j256.ormlite.field.DatabaseField;

import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by acer on 2015/7/20.
 */
public class ComunicationAdapter extends BaseAdapter {

    private List<SingleComunicationDetailEntry> mData;
    private Context mContext;

    public ComunicationAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getDataPosition();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        MyHolder holder = null;
        if (convertView == null) {
            switch (itemViewType){
                case SingleComunicationDetailEntry.DATA_RIGHT:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.communication_right_item, null);
                    holder = new MyHolder(convertView);
                    convertView.setTag(holder);
                    break;
                case SingleComunicationDetailEntry.DATA_LEFT:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.communication_left_item, null);
                    holder = new MyHolder(convertView);
                    convertView.setTag(holder);
                    break;
                default:
                    break;
            }
        } else {
            switch (itemViewType){
                case SingleComunicationDetailEntry.DATA_LEFT:
                    holder = (MyHolder) convertView.getTag();
                    break;
                case SingleComunicationDetailEntry.DATA_RIGHT:
                    holder = (MyHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        //将message进行转换为表情
        String message = mData.get(position).getMessage();
        Pattern pattern = Pattern.compile("\\[exp[0-9][0-9]?\\]");
        Matcher matcher = pattern.matcher(message);
        SpannableString span = new SpannableString(message);
        while (matcher.find()) {
            Drawable drawable = mContext.getResources().getDrawable(Constants
                    .getExpressionId(matcher.group()));
            drawable.setBounds(0, 0, 40, 40);
            span.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), matcher.start(), matcher
                    .end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        holder.mMessage.setText(span);
        return convertView;
    }

    public void onRefresh(List<SingleComunicationDetailEntry> data){
        mData = data;
        notifyDataSetChanged();
    }

    private class MyHolder {
        private TextView mMessage;

        public MyHolder(View view) {
            mMessage = (TextView) view.findViewById(R.id.communication_item_text);
        }

    }

}
