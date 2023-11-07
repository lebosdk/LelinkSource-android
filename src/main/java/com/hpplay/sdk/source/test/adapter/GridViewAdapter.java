package com.hpplay.sdk.source.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hpplay.sdk.source.test.R;

import java.util.List;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<String> items;
    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener;

    public GridViewAdapter(Context context, List<String> items, OnRecyclerviewItemClickListener onRecyclerviewItemClickListener) {
        this.mContext = context;
        this.items = items;
        this.onRecyclerviewItemClickListener = onRecyclerviewItemClickListener;
    }

    public interface OnRecyclerviewItemClickListener {
        void onItemClickListener(View v, int position);

    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerviewItemClickListener != null) {
                    onRecyclerviewItemClickListener.onItemClickListener(view, (Integer) view.getTag());
                }
            }
        });
        GridViewHolder gridViewHolder = new GridViewHolder(itemView);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(200, 200);
        itemView.setLayoutParams(layoutParams);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        String path = items.get(position);
        holder.itemView.setTag(position);
        Glide.with(mContext).load(path).thumbnail(0.2f).into(holder.itemIcon);
    }

    @Override
    public int getItemCount() {
        return null != items ? items.size() : 0;
    }

    @Override
    public void onClick(View view) {

    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemIcon;

        public GridViewHolder(View itemView) {
            super(itemView);
            itemIcon = (ImageView) itemView.findViewById(R.id.image);
        }

    }
}

