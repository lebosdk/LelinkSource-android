package com.hpplay.sdk.source.test.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.hpplay.sdk.source.api.DeviceListenerConstant;
import com.hpplay.sdk.source.api.LelinkSourceSDK;
import com.hpplay.sdk.source.bean.ServiceInfoParseBean;
import com.hpplay.sdk.source.bean.SinkParameterBean;
import com.hpplay.sdk.source.browse.api.IServiceInfoListParseListener;
import com.hpplay.sdk.source.browse.api.IServiceInfoParseListener;
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo;
import com.hpplay.sdk.source.test.DemoApplication;
import com.hpplay.sdk.source.test.Logger;
import com.hpplay.sdk.source.test.R;
import com.hpplay.sdk.source.test.adapter.BrowseAdapter;
import com.hpplay.sdk.source.test.adapter.DeviceAdapter;
import com.hpplay.sdk.source.test.manager.DeviceManager;
import com.hpplay.sdk.source.test.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class DeviceFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "DeviceFragment";
    private static final String DEMO_KEY_HISTORY_SWITCH = "demo_key_history_switch";
    private static final String DEMO_KEY_SOURCE_UID = "demo_key_source_uid";

    private EditText mEditSourceID;
    private RecyclerView mRecyclerView;
    private BrowseAdapter mAdapter;
    private TextView mTvFail;

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            int id = compoundButton.getId();
            if (id == R.id.history_switch) {
                LelinkSourceSDK.getInstance().enableHistoryDevice(checked);
            }
            PreferenceUtil.getInstance().put(DEMO_KEY_HISTORY_SWITCH, checked);
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.f_device;
    }

    @Override
    public void init(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.btn_favorite).setOnClickListener(this);
        view.findViewById(R.id.btn_history).setOnClickListener(this);
        view.findViewById(R.id.btn_source_id).setOnClickListener(this);
        view.findViewById(R.id.btn_create_service).setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mEditSourceID = view.findViewById(R.id.edit_source_id);
        mTvFail = view.findViewById(R.id.tvFail);
        SwitchCompat historySwitch = view.findViewById(R.id.history_switch);
        boolean checked = PreferenceUtil.getInstance().get(DEMO_KEY_HISTORY_SWITCH, false);
        Logger.i(TAG, "checked:" + checked);
        historySwitch.setChecked(checked);
        historySwitch.setOnCheckedChangeListener(onCheckedChangeListener);

        String id = PreferenceUtil.getInstance().get(DEMO_KEY_SOURCE_UID, null);
        Logger.i(TAG, "id:" + id);

        if (id == null) {
            id = LelinkSourceSDK.getInstance().getSDKInfos(LelinkSourceSDK.KEY_GET_UID);
        }
        if (id != null) {
            mEditSourceID.setText(id);
        }

        mAdapter = new BrowseAdapter(getActivity());
        mAdapter.setOnItemClickListener((position, info) -> {
            mAdapter.setSelectServiceInfo(info);
        });
        mAdapter.setOnSelectListener(hasSelected -> {

        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                break;
            case R.id.btn_source_id:
                String id = mEditSourceID.getText().toString().trim();
                LelinkSourceSDK.getInstance().setSourceID(id);
                PreferenceUtil.getInstance().put(DEMO_KEY_SOURCE_UID, id);
                break;
            case R.id.btn_favorite:
                String sourceId = PreferenceUtil.getInstance().get(DEMO_KEY_SOURCE_UID, null);
                if(sourceId == null) {
                    DemoApplication.toast("请先设置设备唯一标识");
                    return;
                }
                try {
                    Fragment favoriteDeviceFragment = getActivity().getSupportFragmentManager().findFragmentByTag("favorite_fm");
                    if (favoriteDeviceFragment != null && favoriteDeviceFragment.isAdded()) {
                        return;
                    }
                    if (favoriteDeviceFragment == null) {
                        favoriteDeviceFragment = new FavoriteDeviceFragment();
                    }
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, favoriteDeviceFragment, "favorite_fm")
                            .addToBackStack("favorite")
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w(TAG, e);
                }
                break;
            case R.id.btn_history:
                String sourceId2 = PreferenceUtil.getInstance().get(DEMO_KEY_SOURCE_UID, null);
                if(sourceId2 == null) {
                    DemoApplication.toast("请先设置设备唯一标识");
                    return;
                }
                try {
                    Fragment historyDeviceFragment = getActivity().getSupportFragmentManager().findFragmentByTag("history_fm");
                    if (historyDeviceFragment != null && historyDeviceFragment.isAdded()) {
                        return;
                    }
                    if (historyDeviceFragment == null) {
                        historyDeviceFragment = new HistoryDeviceFragment();
                    }
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, historyDeviceFragment, "history_fm")
                            .addToBackStack("history")
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    Logger.w(TAG, e);
                }
                break;
            case R.id.btn_create_service:
                showCreateDeviceDialog();
                break;
        }
    }

    private void showCreateDeviceDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_browse_list, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_browse);
        DeviceAdapter adapter = new DeviceAdapter(getActivity());
        adapter.setOnItemClickListener((position, info) -> {
            adapter.selectItem(info);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.updateDatas(DeviceManager.getInstance().getBrowseList());

        new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("查询", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mTvFail.setText("");
                        mAdapter.clearDatas();
                        List<LelinkServiceInfo> selectedInfoList = adapter.getSelectedInfoList();
                        getLelinkServiceInfoList(selectedInfoList);
                    }
                }).setNegativeButton("取消", null).setCancelable(false).show();
    }

    private void getLelinkServiceInfoList(List<LelinkServiceInfo> selectedInfoList) {
        List<SinkParameterBean> sinkParameterBeanList = new ArrayList<>();
        for (LelinkServiceInfo serviceInfo : selectedInfoList) {
            SinkParameterBean bean = new SinkParameterBean();
            bean.createType = SinkParameterBean.CREATE_BY_SINK_APPID_UID;
            bean.uid = serviceInfo.getUid();
            bean.appID = String.valueOf(serviceInfo.getAppId());
            sinkParameterBeanList.add(bean);
        }
        LelinkSourceSDK.getInstance().createLelinkSeviceInfoList(sinkParameterBeanList, new IServiceInfoListParseListener(){

            @Override
            public void onParseResult(final List<ServiceInfoParseBean> result) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(result == null) {
                            Logger.i(TAG, "onParseResult error");
                            DemoApplication.toast("查询失败！");
                            return;
                        }

                        List<LelinkServiceInfo> serviceInfoList = new ArrayList<>();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (ServiceInfoParseBean serviceInfoParseBean : result) {
                            if(serviceInfoParseBean == null) {
                                continue;
                            }
                            Logger.i(TAG, "onParseResult :" + serviceInfoParseBean.resultCode
                                            + " / " + serviceInfoParseBean.uid + " / " + serviceInfoParseBean.info);
                            if(serviceInfoParseBean.resultCode == IServiceInfoParseListener.PARSE_SUCCESS) {
                                serviceInfoList.add(serviceInfoParseBean.info);
                            } else {
                                if(serviceInfoParseBean.uid != null) {
                                    stringBuilder.append("uid: ")
                                            .append(serviceInfoParseBean.uid)
                                            .append(",失败原因:")
                                            .append(getParseErrorMsg(serviceInfoParseBean.resultCode))
                                            .append("\n");
                                } else {
                                    stringBuilder.append("失败原因:")
                                            .append(getParseErrorMsg(serviceInfoParseBean.resultCode))
                                            .append("\n");
                                }
                            }
                        }
                        Logger.i(TAG, "onParseResult success:" + serviceInfoList.size());
                        mAdapter.updateDatas(serviceInfoList);
                        String failTip = stringBuilder.toString();
                        if(!TextUtils.isEmpty(failTip)) {
                            mTvFail.setText("查询失败:\n" + failTip);
                        }
                    }
                });

            }
        });
    }

    public static String getDeviceErrorMsg(int errorCode) {
        String msg = "";
        switch (errorCode) {
            case DeviceListenerConstant.ERROR_INVALID_SOURCE_ID:
                //需调用LelinkSourceSDK.getInstance().setSourceID()设置设备标识
                msg = "未设置sourceID";
                break;
            case DeviceListenerConstant.ERROR_INVALID_PARAMS:
                msg = "传入参数错误";
                break;
            case DeviceListenerConstant.ERROR_NULL_APP_ID:
            case DeviceListenerConstant.ERROR_NULL_UID:
                //收藏的设备缺少appId,uid
                msg = "非乐播收端不支持收藏";
                break;
            case DeviceListenerConstant.ERROR_NULL_RESPONSE:
                //网络请求没有拿到响应内容,响应内容为空或者请求被取消了
                msg = "网络响应为空";
                break;
            case DeviceListenerConstant.ERROR_INVALID_RESPONSE_CODE:
                msg = "响应内容格式不对,没有code";
                break;
            case DeviceListenerConstant.ERROR_PARSE_ERROR:
                msg = "解析收端信息失败";
                break;
            case DeviceListenerConstant.ERROR_FUNCTION_DISABLE:
                msg = "后台配置功能不可用";
                break;
            case DeviceListenerConstant.ERROR_ALIAS_LENGTH_OVER_TEN:
                msg = "别名不能超过十个字符";
                break;
            case DeviceListenerConstant.ERROR_ADD_FAVORITE_WITHOUT_CONNECT:
                msg = "收藏设备之前需要先连接设备";
                break;
            case DeviceListenerConstant.ERROR_SINK_REJECT_FAVORITE:
                msg = "收端拒绝被收藏";
                break;
            case DeviceListenerConstant.ERROR_HISTORY_DEV_SWITCH_CLOSED:
                //需要设置LelinkSourceSDK.getInstance().enableHistoryDevice(true);
                msg = "历史投屏设备功能开关未打开";
                break;
            case DeviceListenerConstant.ERROR_BUSINESS_EXCEPTION:
                msg = "业务异常";
                break;
            case DeviceListenerConstant.ERROR_AUTH_FAIL:
                msg = "鉴权失败";
                break;
            case DeviceListenerConstant.ERROR_REJECT_REQUEST:
                msg = "请求被拒绝";
                break;
            case DeviceListenerConstant.ERROR_SERVICE_NOT_SUPPORT:
                msg = "渠道服务未开通";
                break;
            case DeviceListenerConstant.ERROR_NOT_FOUND:
                msg = "没找到请求";
                break;
            case DeviceListenerConstant.ERROR_SERVICE_EXCEPTION:
                msg = "服务器异常";
                break;
            case DeviceListenerConstant.ERROR_NOT_FOUND_DATA:
                msg = "没找到对应的数据";
                break;
            case DeviceListenerConstant.ERROR_SERVICE_GET_MESSAGE_FAIL:
                msg = "消息不能读取";
                break;
            case DeviceListenerConstant.ERROR_NOT_SUPPORT_REQUEST_METHOD:
                msg = "不支持当前请求方法";
                break;
            case DeviceListenerConstant.ERROR_NOT_SUPPORT_MIME_TYPE:
                msg = "不支持当前媒体类型";
                break;
            case DeviceListenerConstant.ERROR_LACK_OF_PARAMS:
                msg = "缺少必要的请求参数";
                break;
            case DeviceListenerConstant.ERROR_PARAMS_WRONG_TYPE:
                msg = "请求参数类型错误";
                break;
            case DeviceListenerConstant.ERROR_PARAMS_BIND_FAIL:
                msg = "请求参数绑定错误";
                break;
            case DeviceListenerConstant.ERROR_PARAMS_PARSE_FAIL:
                msg = "参数校验失败";
                break;
            case DeviceListenerConstant.ERROR_ALIAS_EXIST:
                msg = "设置别名已经存在";
                break;
            case DeviceListenerConstant.ERROR_ALIAS_LENGTH_OVER_LIMIT:
                msg = "设置别名的字段超过最大字数";
                break;
            case DeviceListenerConstant.ERROR_SENSITIVE_ALIAS:
                msg = "存在敏感词字段";
                break;
            case DeviceListenerConstant.ERROR_DEVICE_NOT_EXIST:
                msg = "设置别名的设备不存在";
                break;
            case DeviceListenerConstant.ERROR_FAVORITE_OVER_LIMIT:
                msg = "超过收藏最大数量";
                break;
            case DeviceListenerConstant.ERROR_FAVORITE_INFO_NOT_EXIST:
                msg = "收藏信息不存在";
                break;
            case DeviceListenerConstant.ERROR_FAVORITE_DEVICE_EXIST:
                msg = "已存在收藏记录";
                break;
            case DeviceListenerConstant.ERROR_HISTORY_DEVICE_NOT_EXIST:
                msg = "设备历史记录不存在";
                break;
            case DeviceListenerConstant.ERROR_FAVORITE_DEVICE_NOT_EXIST:
                msg = "收藏的设备不存在";
                break;
            default:
                msg = "未知错误";
        }
        return msg;
    }

    public static String getParseErrorMsg(int errorCode) {
        String msg = String.valueOf(errorCode);
        switch (errorCode) {
            case IServiceInfoParseListener.PARSE_AUTH_FAILED:
                msg = "认证失败";
                break;
            case IServiceInfoParseListener.PARSE_SHORT_URL_INVALID:
                msg = "无效短链";
                break;
            case IServiceInfoParseListener.PARSE_SERVER_ERROR:
                msg = "服务请求失败";
                break;
            case IServiceInfoParseListener.PARSE_CODE_NON_EXISTENT:
                msg = "投屏码不存在";
                break;
            case IServiceInfoParseListener.PARSE_CODE_INVALID:
                msg = "投屏码已失效";
                break;
            case IServiceInfoParseListener.PARSE_DEVICE_NONSUPPORT:
                msg = "接收端不支持此功能";
                break;
            case IServiceInfoParseListener.PARSE_DEVICE_OFFLINE:
                msg = "接收端不在线";
                break;
            case IServiceInfoParseListener.PARSE_INVALID_INPUT:
                msg = "输入参数无效";
                break;
            default:
                msg = "未知错误";
        }
        return msg;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mEditSourceID = null;
        mRecyclerView = null;
        mTvFail = null;
        mAdapter = null;
    }
}
