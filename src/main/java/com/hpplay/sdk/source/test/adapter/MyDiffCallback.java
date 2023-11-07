package com.hpplay.sdk.source.test.adapter;

import android.support.v7.util.DiffUtil;

import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MyDiffCallback extends DiffUtil.Callback {

    private List<LelinkServiceInfo> oldData;
    private List<LelinkServiceInfo> newData;

    // 这里通过构造函数把新老数据集传进来
    public MyDiffCallback(List<LelinkServiceInfo> oldData, List<LelinkServiceInfo> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData == null ? 0 : oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData == null ? 0 : newData.size();
    }

    // 判断是不是同一个Item：如果Item有唯一标志的Id的话，建议此处判断id
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        LelinkServiceInfo oldUser = oldData.get(oldItemPosition);
        LelinkServiceInfo newUser = newData.get(newItemPosition);
        if (oldUser == null || newUser == null) {
            return false;
        }
        return oldUser.equals(newUser);
    }

    // 判断两个Item的内容是否相同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // 默认内容是相同的，只要有一项不同，则返回false
        LelinkServiceInfo oldUser = oldData.get(oldItemPosition);
        LelinkServiceInfo newUser = newData.get(newItemPosition);

        if (oldUser == null || newUser == null) {
            return false;
        }

        Set<Integer> oldBrowserKey = oldUser.getBrowserInfos().keySet();
        Set<Integer> newBrowserKey = newUser.getBrowserInfos().keySet();
        if (oldBrowserKey.size() != newBrowserKey.size()) {
            return false;
        }

        if (oldUser.hashCode() != newUser.hashCode()) {
            return false;
        }

        return oldUser.equals(newUser);
    }
}
