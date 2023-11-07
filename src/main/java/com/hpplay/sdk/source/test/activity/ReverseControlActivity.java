package com.hpplay.sdk.source.test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.EditText;
import android.widget.ImageView;

import com.hpplay.sdk.source.api.ISinkKeyEventListener;
import com.hpplay.sdk.source.api.ISinkTouchEventListener;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.bean.SinkTouchEventArea;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.constants.KeyEventName;
import com.hpplay.sdk.source.test.utils.ScreenUtil;

public class ReverseControlActivity extends Activity {
    private static final String TAG = "ReverseControlActivity";

    private ImageView mUpIv, mDownIv, mLeftIv, mRightIv, mOkIv, mBackIv, mSettingsTv;
    private EditText mScaleModulusEt;
    private ImageView mScaleIv;

    private long firstDownTime = -1;
    private int preEventAction = -1;

    public static void show(Context context) {
        context.startActivity(new Intent(context, ReverseControlActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_control);

        mUpIv = (ImageView) findViewById(R.id.iv_key_up);
        mDownIv = (ImageView) findViewById(R.id.iv_key_down);
        mLeftIv = (ImageView) findViewById(R.id.iv_key_left);
        mRightIv = (ImageView) findViewById(R.id.iv_key_right);
        mOkIv = (ImageView) findViewById(R.id.iv_key_ok);
        mBackIv = (ImageView) findViewById(R.id.iv_key_back);
        mSettingsTv = (ImageView) findViewById(R.id.iv_key_settings);
        setSinkKeyEventListener();

        mScaleIv = (ImageView) findViewById(R.id.iv_scale);
        mScaleModulusEt = findViewById(R.id.et_scale_modulus);
        mScaleModulusEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSinkTouchEventListener();
            }
        });
        ScaleGestureDetector detector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Logger.i(TAG, "onScale: ");
                mScaleIv.setScaleX(mScaleIv.getScaleX() * detector.getScaleFactor());
                mScaleIv.setScaleY(mScaleIv.getScaleY() * detector.getScaleFactor());
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
        mScaleIv.setOnTouchListener((v, event) -> {
            Logger.i(TAG, "onTouchEvent: event: " + event.toString());
            return detector.onTouchEvent(event);
        });
        setSinkTouchEventListener();
    }

    private String getKeyName(int keyCode) {
        String keyName = KeyEventName.KeyEventMap.getInstance().get(keyCode);
        if (TextUtils.isEmpty(keyName)) {
            keyName = KeyEventName.KEY_NAME_UNKNOWN;
        }
        return keyName;
    }

    private void showByAction(ImageView imageView, int action) {
        DemoApplication.runOnUI(() -> imageView.setBackgroundColor(action == KeyEvent.ACTION_UP ? Color.TRANSPARENT : Color.GRAY));
    }

    private void setSinkTouchEventListener() {
        int[] screenSize = ScreenUtil.getRelScreenSize(this);
        // 坐标转换基数，默认是屏幕宽高
        SinkTouchEventArea sinkTouchEventArea = new SinkTouchEventArea(screenSize[0], screenSize[1]);
        // 缩放基数，电视屏幕较大，在双指触摸缩放时，电视上两个手指间的距离除屏幕尺寸之后得出值会很小，相应的得出缩放值也与原始值基本无差别，
        // 反馈到手机上之后，根据其缩放值得出的效果差强人意，所以在这里需要传入一个缩放比例，用于放大缩放值。
        float scaleModulus = 0f;
        if (!TextUtils.isEmpty(mScaleModulusEt.getText())) {
            scaleModulus = Float.parseFloat(mScaleModulusEt.getText().toString());
        }
        LelinkSourceSDK.getInstance().registerSinkTouchEvent();
        LelinkSourceSDK.getInstance().setSinkTouchEventListener(sinkTouchEventArea, scaleModulus, new ISinkTouchEventListener() {
            @Override
            public void onTouchEvent(MotionEvent motionEvent) {
                Logger.i(TAG, "onTouchEvent: " + motionEvent.toString());
                dispatchTouchEvent(motionEvent);
            }
        });

        // 判断无障碍服务是否开启，用于全局反控
        boolean isStart = LelinkSourceSDK.getInstance().isAccessibilityServiceStart(this.getApplicationContext());
        Logger.i(TAG, "setSinkTouchEventListener: isAccessibilityServiceStart:" + isStart);
        if (!isStart) {
            // 打开辅助功能设置菜单，提示用户为应用手动打开服务（需要手动打开）
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void setSinkKeyEventListener() {
        LelinkSourceSDK.getInstance().registerSinkKeyEvent();
        LelinkSourceSDK.getInstance().setSinkKeyEventListener(new ISinkKeyEventListener() {
            @Override
            public void onKeyEvent(KeyEvent keyEvent) {
                handleKeyEvent(keyEvent);
            }
        });
    }

    private void handleKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        int action = keyEvent.getAction();
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            DemoApplication.runOnUI(() -> getWindow().getDecorView().dispatchKeyEvent(keyEvent));
            return;
        }
        Logger.i(TAG, "handleKeyEvent: keyCode: " + keyCode + " action: " + action);
        ImageView targetView = null;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                targetView = mUpIv;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                targetView = mDownIv;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                targetView = mLeftIv;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                targetView = mRightIv;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                targetView = mOkIv;
                break;
            case KeyEvent.KEYCODE_BACK:
                targetView = mBackIv;
                break;
            case KeyEvent.KEYCODE_MENU:
                targetView = mSettingsTv;
                break;
        }
        // 判断是否第一次按下
        if (preEventAction != KeyEvent.ACTION_DOWN && action == KeyEvent.ACTION_DOWN) {
            firstDownTime = System.currentTimeMillis();
        }
        // 判断是否是第一次抬起
        if (preEventAction == KeyEvent.ACTION_DOWN && action == KeyEvent.ACTION_UP) {
            String keyName = getKeyName(keyCode);
            if (System.currentTimeMillis() - firstDownTime > 1500) {
                DemoApplication.toast(keyName + ": long clicked");
            } else {
                DemoApplication.toast(keyName + ": clicked");
            }
        }
        preEventAction = action;
        if (targetView == null) {
            return;
        }
        showByAction(targetView, action);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Logger.i(TAG, "onTouchEvent: downTime: " + event.getDownTime() + ", eventTime: " + event.getEventTime() +
                ", action: " + event.getAction() + ", pointerCount: " + event.getPointerCount() + ", pressure: " + event.getPressure() +
                ", size: " + event.getSize() + ", metaState: " + event.getMetaState() + ", xPrecision: " + event.getXPrecision() +
                ", yPrecision: " + event.getYPrecision() + ", edgeFlag: " + event.getEdgeFlags());
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy");
        LelinkSourceSDK.getInstance().unregisterSinkKeyEvent();
        LelinkSourceSDK.getInstance().setSinkKeyEventListener(null);
        LelinkSourceSDK.getInstance().unregisterSinkTouchEvent();
        LelinkSourceSDK.getInstance().setSinkTouchEventListener(null);
    }
}