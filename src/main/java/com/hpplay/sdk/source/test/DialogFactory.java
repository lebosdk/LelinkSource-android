package com.hpplay.sdk.source.test;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.bean.DanmakuBean;
import com.hpplay.sdk.source.bean.DanmakuPropertyBean;
import com.hpplay.sdk.source.browse.api.IAPI;

public class DialogFactory {
    /**
     * 发送弹幕设置
     */
    private static String mDanmakuFontColor;  // 弹幕字体颜色
    private static int mDanmakuFontSize = 12; // 弹幕字体大小

    /**
     * 弹幕参数设置
     */
    private static boolean isSwitch;    // 是否开启弹幕
    private static int mDanmakuLine;    // 展示行数
    private static float mDanmakuSpeed; // 速度


    /**
     * 发送弹幕
     *
     * @param context
     */
    public static void showDanmukuSendDialog(final Context context) {
        final EditText contentEt;
        final TextView mFontColorTv;
        final TextView mFontSizeTv;
        Button mWhiteBtn, mRedBtn, mYellowBtn;
        Button mSendBtn, mExitBtn;
        Button mFontMinBtn, mFontMaxBtn;

        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_send_danmaku, null);
        contentEt = view.findViewById(R.id.danmaku_content_et);
        mFontColorTv = view.findViewById(R.id.danmaku_text_color_tv);
        mFontColorTv.setBackgroundColor(context.getResources().getColor(R.color.white));
        mFontSizeTv = view.findViewById(R.id.danmaku_font_size_tv);
        mWhiteBtn = view.findViewById(R.id.danmaku_white_btn);
        mRedBtn = view.findViewById(R.id.danmaku_red_btn);
        mYellowBtn = view.findViewById(R.id.danmaku_yellow_btn);
        mSendBtn = view.findViewById(R.id.danmaku_send_btn);
        mExitBtn = view.findViewById(R.id.danmaku_exit_btn);
        mFontMaxBtn = view.findViewById(R.id.danmaku_font_max);
        mFontMinBtn = view.findViewById(R.id.danmaku_font_min);
        dialog.setContentView(view);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialog.getWindow().setLayout(dm.widthPixels, dialog.getWindow().getAttributes().height);
        dialog.show();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.danmaku_white_btn:
                        mFontColorTv.setBackgroundColor(context.getResources().getColor(R.color.white));
                        mDanmakuFontColor = "#ffffff";
                        break;
                    case R.id.danmaku_red_btn:
                        mFontColorTv.setBackgroundColor(context.getResources().getColor(R.color.red));
                        mDanmakuFontColor = "#FF0000";
                        break;
                    case R.id.danmaku_yellow_btn:
                        mFontColorTv.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                        mDanmakuFontColor = "#FFFF00";
                        break;
                    case R.id.danmaku_font_min:
                        mDanmakuFontSize--;
                        mFontSizeTv.setText(mDanmakuFontSize + "");
                        mFontSizeTv.setTextSize(mDanmakuFontSize);
                        break;
                    case R.id.danmaku_font_max:
                        mDanmakuFontSize++;
                        mFontSizeTv.setText(mDanmakuFontSize + "");
                        mFontSizeTv.setTextSize(mDanmakuFontSize);
                        break;
                    case R.id.danmaku_exit_btn:
                        dialog.dismiss();
                        break;
                    case R.id.danmaku_send_btn:
                        String content = contentEt.getText().toString().trim();
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(context, "请输入弹幕内容", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 组装弹幕参数
                        DanmakuBean danmakuBean = new DanmakuBean();
                        danmakuBean.setContent(content); // 弹幕内容
                        danmakuBean.setFontColor(mDanmakuFontColor); // 字体颜色
                        danmakuBean.setFontSize(mDanmakuFontSize);   // 字体大小
                        danmakuBean.setImmShow(true); //是否马上显示
                        // 发送弹幕
                        LelinkSourceSDK.getInstance().sendDanmaku(danmakuBean);
                        break;
                }
            }
        };

        mWhiteBtn.setOnClickListener(onClickListener);
        mRedBtn.setOnClickListener(onClickListener);
        mYellowBtn.setOnClickListener(onClickListener);
        mFontMinBtn.setOnClickListener(onClickListener);
        mFontMaxBtn.setOnClickListener(onClickListener);
        mSendBtn.setOnClickListener(onClickListener);
        mExitBtn.setOnClickListener(onClickListener);
    }


    /**
     * 弹幕设置
     *
     * @param context
     */
    public static void showDanmakuSettingDailog(Context context) {

        final EditText mPaddingEt, mRowSpaceEt, mLineSpaceEt;
        AppCompatSpinner mLineSpinner, mSpeedSpinner;
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_setting_danmaku, null);
        // 设置弹幕行数
        mLineSpinner = view.findViewById(R.id.danmaku_linespace_sp);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, getDanmuLines());
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLineSpinner.setAdapter(adapter);
        mLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDanmakuLine = Integer.parseInt(getDanmuLines()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 设置弹幕速度
        mSpeedSpinner = view.findViewById(R.id.danmaku_speed_sp);
        ArrayAdapter<String> adapterSpeed = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, getDanmuSpeed());
        adapterSpeed.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpeedSpinner.setAdapter(adapterSpeed);
        mSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDanmakuSpeed = Float.parseFloat(getDanmuSpeed()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mPaddingEt = view.findViewById(R.id.danmaku_padding_et);
        mRowSpaceEt = view.findViewById(R.id.danmaku_rowspace_et);
        mLineSpaceEt = view.findViewById(R.id.danmaku_lineSpace_et);
        dialog.setContentView(view);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialog.getWindow().setLayout(dm.widthPixels, dialog.getWindow().getAttributes().height);
        dialog.show();

        SwitchCompat swithch = view.findViewById(R.id.danmaku_switch);
        swithch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSwitch = isChecked;
            }
        });

        view.findViewById(R.id.danmaku_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 组装弹幕设置参数
                DanmakuPropertyBean danmakuPropertyBean = new DanmakuPropertyBean();
                danmakuPropertyBean.setSwitch(isSwitch);

                String padding = mPaddingEt.getText().toString().trim();
                if (!TextUtils.isEmpty(padding)) {
                    danmakuPropertyBean.setPadding(Integer.parseInt(padding));
                }

                String rowSpace = mRowSpaceEt.getText().toString().trim();
                if (!TextUtils.isEmpty(rowSpace)) {
                    danmakuPropertyBean.setRowSpace(Integer.parseInt(rowSpace));
                }

                String lineSpace = mLineSpaceEt.getText().toString().trim();
                if (!TextUtils.isEmpty(lineSpace)) {
                    danmakuPropertyBean.setLineSpace(Integer.parseInt(lineSpace));
                }

                danmakuPropertyBean.setLines(mDanmakuLine);
                danmakuPropertyBean.setSpeed(mDanmakuSpeed);
                // 设置弹幕参数
                LelinkSourceSDK.getInstance().sendDanmakuProperty(danmakuPropertyBean);

                dialog.dismiss();
            }
        });
        view.findViewById(R.id.danmaku_exit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    /**
     * 弹幕行数设置数据
     * 1~10行，通过使用常量： DanmakuPropertyBean.LINES_1 获取
     *
     * @return
     */
    private static String[] getDanmuLines() {
        String[] lines = new String[10];
        lines[0] = String.valueOf(DanmakuPropertyBean.LINES_1);
        lines[1] = String.valueOf(DanmakuPropertyBean.LINES_2);
        lines[2] = String.valueOf(DanmakuPropertyBean.LINES_3);
        lines[3] = String.valueOf(DanmakuPropertyBean.LINES_4);
        lines[4] = String.valueOf(DanmakuPropertyBean.LINES_5);
        lines[5] = String.valueOf(DanmakuPropertyBean.LINES_6);
        lines[6] = String.valueOf(DanmakuPropertyBean.LINES_7);
        lines[7] = String.valueOf(DanmakuPropertyBean.LINES_8);
        lines[8] = String.valueOf(DanmakuPropertyBean.LINES_9);
        lines[9] = String.valueOf(DanmakuPropertyBean.LINES_10);
        return lines;
    }

    /**
     * 弹幕速度
     * SPEED_1~SPEED_10 通过使用常量：DanmakuPropertyBean.SPEED_1 获取
     *
     * @return
     */
    private static String[] getDanmuSpeed() {
        String[] speeds = new String[10];
        speeds[0] = String.valueOf(DanmakuPropertyBean.SPEED_1);
        speeds[1] = String.valueOf(DanmakuPropertyBean.SPEED_2);
        speeds[2] = String.valueOf(DanmakuPropertyBean.SPEED_3);
        speeds[3] = String.valueOf(DanmakuPropertyBean.SPEED_4);
        speeds[4] = String.valueOf(DanmakuPropertyBean.SPEED_5);
        speeds[5] = String.valueOf(DanmakuPropertyBean.SPEED_6);
        speeds[6] = String.valueOf(DanmakuPropertyBean.SPEED_7);
        speeds[7] = String.valueOf(DanmakuPropertyBean.SPEED_8);
        speeds[8] = String.valueOf(DanmakuPropertyBean.SPEED_9);
        speeds[9] = String.valueOf(DanmakuPropertyBean.SPEED_10);
        return speeds;

    }

}
