package com.hpplay.sdk.source.test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.browse.data.BrowserInfo;
import com.hpplay.sdk.source.test.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.RecyclerHolder> {

    private static final String TAG = "DeviceAdapter";

    private Context mContext;
    private List<LelinkServiceInfo> mDatas;
    private final LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    private List<LelinkServiceInfo> selectedInfoList = new ArrayList<>();

    public interface OnItemClickListener {
        void onClick(int position, LelinkServiceInfo pInfo);
    }

    public DeviceAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
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

        String item = info.getName() + " uid:" + info.getUid() + " appId:" + info.getAppId() + " types:" + info.getTypes() + browseType
                + " 在线:" + info.isOnLine();

        if(!TextUtils.isEmpty(info.getAlias())) {
            item = item + " 别名:" + info.getAlias();
        }
        holder.textView.setText(item);
        holder.textView.setTag(R.id.id_position, position);
        holder.textView.setTag(R.id.id_info, info);
        holder.textView.setOnClickListener(mOnItemClickListener);
        if (selectedInfoList.contains(info)){
            holder.textView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.textView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    public void updateDatas(List<LelinkServiceInfo> infos) {
        mDatas.clear();
        if(infos == null) {
            return;
        }
        mDatas.addAll(infos);
        notifyDataSetChanged();
    }

    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public List<LelinkServiceInfo> getData() {
        return mDatas;
    }

    public void selectItem(LelinkServiceInfo selectInfo) {
        if(selectedInfoList.contains(selectInfo)) {
            selectedInfoList.remove(selectInfo);
        } else {
            selectedInfoList.add(selectInfo);
        }
        notifyDataSetChanged();
    }

    public List<LelinkServiceInfo> getSelectedInfoList() {
        return selectedInfoList;
    }

    public void clearSelectedInfo() {
        selectedInfoList.clear();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {

        TextView textView;

        private RecyclerHolder(View itemView) {
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
