package com.hpplay.sdk.source.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hpplay.sdk.source.bean.DecodeSupportBean;
import com.hpplay.sdk.source.test.R;

import java.util.List;

public class DeCodeAdapter extends BaseAdapter {

    private List<DecodeSupportBean.DecodesInfo> mDeCodes;
    private Context mContext;

    public DeCodeAdapter(Context context, List<DecodeSupportBean.DecodesInfo> decodes) {
        this.mDeCodes = decodes;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDeCodes == null ? 0 : mDeCodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeCodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HodlerView hodlerView;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_decode_list_item, null);
            hodlerView = new HodlerView();
            hodlerView.mTitle = convertView.findViewById(R.id.decode_title);
            hodlerView.mType = convertView.findViewById(R.id.decode_type);
            hodlerView.mRes = convertView.findViewById(R.id.decode_res);
            convertView.setTag(hodlerView);
        } else {
            hodlerView = (HodlerView) convertView.getTag();
        }

        DecodeSupportBean.DecodesInfo decodeSupportBean = mDeCodes.get(position);
        hodlerView.mTitle.setText("Name:" + decodeSupportBean.name);
        hodlerView.mType.setText("Type:" + decodeSupportBean.type);
        hodlerView.mRes.setText("Res:" + decodeSupportBean.res);

        return convertView;
    }

    class HodlerView {
        TextView mTitle;
        TextView mType;
        TextView mRes;
    }
}
