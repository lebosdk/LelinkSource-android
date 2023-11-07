package com.hpplay.sdk.source.test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.browse.data.BrowserInfo;
import com.hpplay.sdk.source.log.SourceLog;
import com.hpplay.sdk.source.test.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Zippo on 2018/6/8.
 * Date: 2018/6/8
 * Time: 17:00:39
 */
public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.RecyclerHolder> {

    private static final String TAG = "BrowseAdapter";

    public interface OnSelectListener {

        void onSelected(boolean hasSelected);

    }

    private List<LelinkServiceInfo> mDatas;
    private final LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    private LelinkServiceInfo mSelectInfo;
    private OnSelectListener mOnSelectListener;

    public interface OnItemClickListener {

        void onClick(int position, LelinkServiceInfo pInfo);

    }

    public BrowseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_browse_main, parent, false);
        return new RecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        LelinkServiceInfo info = mDatas.get(position);
        if (null == info) {
            return;
        }

        Map<Integer, BrowserInfo> browserInfoMap = info.getBrowserInfos();
        String browseType = "";
        if(browserInfoMap != null && browserInfoMap.size() > 0){
            boolean isBleDis;
            for(BrowserInfo browserInfo : browserInfoMap.values()){
                isBleDis = browserInfo.getCreateType() == BrowserInfo.CREATE_TYPE_BLUETOOTH_PIN ? true : false;
//                SourceLog.i(TAG, "getTypes-----" + info.getName()+"========" + browserInfo.getCreateType());
                if(isBleDis){
                    browseType = "  isBle: true";
                    break;
                }
            }
        }

        String item = info.getName() + " uid:" + info.getUid() + " types:" + info.getTypes() + browseType +
                "\n bili_drainage: " + info.getDrainage(LelinkServiceInfo.DRAINAGE_INDEX_BILI);
        String ssdp = info.getSSDPPacketData();
        if (!TextUtils.isEmpty(ssdp) && ssdp.toUpperCase().contains("UUID")) {
            item = item + " ssdp: 有UUID ";
        }
        holder.textView.setText(item);
        holder.textView.setBackgroundColor(Color.TRANSPARENT);
        holder.textView.setTag(R.id.id_position, position);
        holder.textView.setTag(R.id.id_info, info);
        holder.textView.setOnClickListener(mOnItemClickListener);
        if (mSelectInfo == null) {
            return;
        }
        if (!TextUtils.isEmpty(info.getUid()) && TextUtils.equals(info.getUid(), mSelectInfo.getUid())) {
            holder.textView.setBackgroundColor(Color.YELLOW);
        } else if (TextUtils.equals(mSelectInfo.getName(), info.getName()) && TextUtils.equals(mSelectInfo.getIp(), info.getIp())) {
            holder.textView.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    public void clearDatas() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void updateDatas(List<LelinkServiceInfo> infos) {
        mDatas.clear();
        if (null != infos) {
            mDatas.addAll(infos);
            if (mDatas.contains(mSelectInfo)) {
                mOnSelectListener.onSelected(true);
                return;
            }
        }
        mOnSelectListener.onSelected(false);
        this.notifyDataSetChanged();
    }

    public List<LelinkServiceInfo> getDatas() {
        return mDatas;
    }

    public void setSelectServiceInfo(LelinkServiceInfo selectInfo) {
        mSelectInfo = selectInfo;
        if (!mDatas.contains(selectInfo)) {
            mDatas.add(selectInfo);
        }
        notifyDataSetChanged();
        mOnSelectListener.onSelected(true);
    }

    public LelinkServiceInfo getSelectServiceInfo() {
        if (mDatas.contains(mSelectInfo)) {
            return mSelectInfo;
        }
        return null;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {

        TextView textView;

        private RecyclerHolder(android.view.View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview);
        }

    }

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.id_position);
            LelinkServiceInfo info = (LelinkServiceInfo) v.getTag(R.id.id_info);
            if (null != mItemClickListener) {
                mItemClickListener.onClick(position, info);
            }
        }
    };
}
