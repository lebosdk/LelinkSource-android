package com.hpplay.sdk.source.test.manager;

import com.hpplay.sdk.source.api.IDaPlayerListener;
import com.hpplay.sdk.source.api.INewPlayerListener;
import com.hpplay.sdk.source.bean.CastBean;
import com.hpplay.sdk.source.bean.DaCastBean;
import com.hpplay.sdk.source.test.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送
 */
public class CastManager {
    private final static String TAG = "DemoCastManager";
    private static CastManager sInstance = null;
    private final List<INewPlayerListener> mListenerList = new ArrayList<>();
    private final List<IDaPlayerListener> mDaListenerList = new ArrayList<>();

    private final INewPlayerListener mPlayerListener = new INewPlayerListener() {

        @Override
        public void onLoading(CastBean bean) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onLoading(bean);
            }
        }

        @Override
        public void onStart(CastBean bean) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onStart(bean);
            }
        }

        @Override
        public void onPause(CastBean bean) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onPause(bean);
            }
        }

        @Override
        public void onCompletion(CastBean bean, int type) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onCompletion(bean, type);
            }
        }

        @Override
        public void onStop(CastBean bean) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onStop(bean);
            }
        }

        @Override
        public void onSeekComplete(CastBean bean, int position) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onSeekComplete(bean, position);
            }
        }

        @Override
        public void onInfo(CastBean bean, final int what, final int extra) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onInfo(bean, what, extra);
            }
        }

        @Override
        public void onInfo(CastBean bean, int what, final String data) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onInfo(bean, what, data);
            }
        }

        @Override
        public void onError(CastBean bean, int what, int extra) {
            Logger.e(TAG, "onError what:" + what + " extra:" + extra);
            for (INewPlayerListener listener : mListenerList) {
                listener.onError(bean, what, extra);
            }
        }

        @Override
        public void onVolumeChanged(CastBean bean, float percent) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onVolumeChanged(bean, percent);
            }
        }

        @Override
        public void onPositionUpdate(CastBean bean, long duration, long position) {
            for (INewPlayerListener listener : mListenerList) {
                listener.onPositionUpdate(bean, duration, position);
            }
        }
    };

    private IDaPlayerListener mDaPlayerListener = new IDaPlayerListener() {
        @Override
        public void onResult(DaCastBean bean,boolean hasDa) {
            for (IDaPlayerListener listener : mDaListenerList) {
                listener.onResult(bean, hasDa);
            }
        }

        @Override
        public void onLoading(DaCastBean bean) {
            for (IDaPlayerListener listener : mDaListenerList) {
                listener.onLoading(bean);
            }
        }

        @Override
        public void onStart(DaCastBean bean) {
            for (IDaPlayerListener listener : mDaListenerList) {
                listener.onStart(bean);
            }
        }

        @Override
        public void onStop(DaCastBean bean) {
            for (IDaPlayerListener listener : mDaListenerList) {
                listener.onStop(bean);
            }
        }
    };

    public synchronized static CastManager getInstance() {
        synchronized (CastManager.class) {
            if (sInstance == null) {
                sInstance = new CastManager();
            }
        }
        return sInstance;
    }

    private CastManager() {

    }

    public INewPlayerListener getPlayerListener() {
        return mPlayerListener;
    }

    public void addPlayerListener(INewPlayerListener listener) {
        mListenerList.add(listener);
    }

    public void removeListener(INewPlayerListener listener) {
        mListenerList.remove(listener);
    }

    public IDaPlayerListener getDaPlayerListener() {
        return mDaPlayerListener;
    }

    public void addDaPlayerListener(IDaPlayerListener listener) {
        mDaListenerList.add(listener);
    }

    public void removeDaListener(IDaPlayerListener listener) {
        mDaListenerList.remove(listener);
    }
}
