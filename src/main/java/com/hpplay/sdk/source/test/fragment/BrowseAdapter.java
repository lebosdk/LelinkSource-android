package com.hpplay.sdk.source.test.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.R;

import java.util.List;

/**
 * author : DON
 * date   : 5/19/219:00 PM
 * desc   :
 */
public class BrowseAdapter extends BaseAdapter {
    private Context mContext;
    private List<LelinkServiceInfo> mList;

    public BrowseAdapter(Context context, List<LelinkServiceInfo> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public LelinkServiceInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_browse, null);
            viewHolder.nameTxt = convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.nameTxt.setText(getItem(position).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView nameTxt;
    }
}
