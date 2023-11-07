package com.hpplay.sdk.source.test.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * author : DON
 * date   : 5/28/212:39 PM
 * desc   :
 */
public class SettingAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;

    public SettingAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LinearLayout rootView = new LinearLayout(mContext);
            rootView.setPadding(30, 20, 30, 20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootView.setLayoutParams(params);
            TextView textView = new TextView(mContext);
            textView.setTextColor(Color.WHITE);
            rootView.addView(textView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder.textView = textView;
            convertView = rootView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mList.get(position));
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
