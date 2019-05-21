package com.joker.module_order.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.Order;
import com.example.commonres.dialog.AirConditioningControlDialog;
import com.example.commonres.dialog.EnvironmentDialog;
import com.example.commonres.dialog.PasswordConfirmDialog;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.KeyBoardUtil;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerMoreControlComponent;
import com.joker.module_order.di.module.MoreControlModule;
import com.joker.module_order.mvp.contract.MoreControlContract;
import com.joker.module_order.mvp.presenter.MoreControlPresenter;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.ORDER_MORECONTROLACTIVITY)
public class MoreControlActivity
    extends GosControlModuleBaseActivity<MoreControlPresenter>
    implements MoreControlContract.View {

    @BindView(R2.id.more_control_layout)
    LinearLayout moreOperationLayout;
    //设置文字
    //卧室灯状态
    @BindView(R2.id.bedroom_light)
    TextView bedRoomLightStatus;


    private Order mOrder;
    private PasswordConfirmDialog passwordConfirmDialog;

    private Boolean isOpenBedRoomLight = false;
    private Boolean isOpenAirConditioning = false;

    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (isDeviceCanBeControlled()) {
                hideLoading();
            } else {
                toastDeviceNoReadyAndExit();
            }
        }

    };

    private enum handler_key {

        /** 更新界面 */
        UPDATE_UI,

        DISCONNECT,
    }

    /** The handler. */
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                case UPDATE_UI:
                    updateUI();
                    break;
                case DISCONNECT:
                    toastDeviceDisconnectAndExit();
                    break;
            }
        }
    };

    /** 设备列表传入的设备变量 */
    private GizWifiDevice mDevice;
    private static final String TAG = "MoreControlActivity";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMoreControlComponent
            .builder()
            .appComponent(appComponent)
            .moreControlModule(new MoreControlModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_more_control;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mOrder = (Order) getIntent().getSerializableExtra("order");
        mDevice = getIntent().getParcelableExtra("GizWifiDevice");
        mDevice.setListener(gizWifiDeviceListener);
        Log.i(TAG, mDevice.getDid());
    }

    @Override
    public void onResume() {
        super.onResume();
        //进行密码输入确认
        showPasswordDialog();
    }


    /**
     * Description:页面加载后弹出等待框，等待设备可被控制状态回调，如果一直不可被控，等待一段时间后自动退出界面
     */
    private void getStatusOfDevice() {
        // 设备是否可控
        if (isDeviceCanBeControlled()) {
            // 可控则查询当前设备状态
            mDevice.getDeviceStatus();
        } else {
            // 显示等待栏
            showLoading();
            if (mDevice.isLAN()) {
                // 小循环10s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 10000);
            } else {
                // 大循环20s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 20000);
            }
        }
    }

    private boolean isDeviceCanBeControlled() {
        return mDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled;
    }

    private void toastDeviceNoReadyAndExit() {
        ToastUtil.makeText(this, "设备无响应，请检查设备是否正常工作");
        finish();
    }

    private void toastDeviceDisconnectAndExit() {
        ToastUtil.makeText(this, "连接已断开");
        finish();
    }

    @OnClick({R2.id.back,R2.id.close_or_open_bedroom_bulb,
        R2.id.close_or_open_kitchen_bulb,R2.id.air_conditioning,
        R2.id.television,R2.id.close_or_open_toilet_bulb,
        R2.id.water_heater,R2.id.curtain,
        R2.id.window,R2.id.sleep_mode,
        R2.id.electric,R2.id.operate_mode,
        R2.id.thermometer,R2.id.check_out})
    public void onClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.back)//返回
            killMyself();
        else if (viewId == R.id.close_or_open_bedroom_bulb)//开/关卧室灯
            bedRoomLight();
        else if (viewId == R.id.close_or_open_kitchen_bulb) {//开/关厨房灯

        } else if (viewId == R.id.air_conditioning)//空调控制
            airConditioningControl();
        else if (viewId == R.id.close_or_open_toilet_bulb) {//开/关厕所灯

        } else if (viewId == R.id.television) {//电视机控制

        } else if (viewId == R.id.water_heater) {//热水器控制

        } else if (viewId == R.id.curtain) {//窗帘布控制

        } else if (viewId == R.id.window) {//窗户控制

        } else if (viewId == R.id.sleep_mode) {//睡眠模式

        } else if (viewId == R.id.electric) {//电表

        } else if (viewId == R.id.operate_mode) {//模式

        } else if (viewId == R.id.thermometer) {//客房环境
            showTermeter();
        } else if (viewId == R.id.check_out)//退房
            checkOut();
    }


    /**
     * 开/关卧室灯
     */
    private void bedRoomLight(){
        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
        if (!isOpenBedRoomLight) {
            map.put(KEY_RGB_BRIGHTNESS, 255);
            map.put(KEY_RGB_RED, 125);
            map.put(KEY_RGB_BLUE, 125);
            map.put(KEY_RGB_GREEN, 125);
            sendCommads(map);
        } else {
            map.put(KEY_RGB_BRIGHTNESS, 0);
            map.put(KEY_RGB_RED, 0);
            map.put(KEY_RGB_BLUE, 0);
            map.put(KEY_RGB_GREEN, 0);
            sendCommads(map);
        }
    }



    /**
     * 空调控制
     */
    private void airConditioningControl(){
        AirConditioningControlDialog dialog = new AirConditioningControlDialog(this);
        dialog.show();
        //设置各种控制
        dialog.setSwitchListener(new AirConditioningSwitchListener());//空调开关设置
    }


    /**
     * 空调开关控制
     */
    class AirConditioningSwitchListener implements AirConditioningControlDialog.SwitchListener{
        @Override
        public void switchOfAirConditioning(View view) {
            if (isOpenAirConditioning)
                sendCommand(KEY_ESP8266_RELAY, false);
            else
                sendCommand(KEY_ESP8266_RELAY, true);
        }
    }


    /**
     * 显示实时客房环境
     */
    private void showTermeter(){
        EnvironmentDialog environmentDialog = new EnvironmentDialog(this);
        environmentDialog.show();
        environmentDialog.setTemperature(String.valueOf(data_ESP8266_temp));
        environmentDialog.setHumidity(String.valueOf(data_ESP8266_hum));
        environmentDialog.setLight(String.valueOf(data_ESP8266_light));
    }

    /**
     * 退房
     */
    private void checkOut(){
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.show();
        tipsDialog.setTitle("提示");
        tipsDialog.setTipsContent("确认退房吗？");
        tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
            @Override
            public void onCancel() {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                tipsDialog.dismiss();
                //若未进行人脸注册，先进行人脸注册
                if (LoginUtil.getInstance().getUser().getFaceRegister()){
                    ARouter.getInstance()
                        .build(RouterHub.PERSONAL_FACEVERIFICATIONACTIVITY)
                        .withString("userId", mOrder.getUser().getAccount())
                        .withSerializable("order",mOrder)
                        .withString("lockAddress", "")
                        .withBoolean("checkOut", true)
                        .navigation(getViewContext());
                    finish();
                }
                else{
                    ToastUtil.makeText(getViewContext(), "您尚未进行人脸注册，请先进行人脸注册");
                    Utils.navigation(getViewContext(),RouterHub.PERSONAL_FACEREGISTERACTIVITY);
                }
            }
        });
    }

    /**
     * 发送指令,下发单个数据点的命令可以用这个方法
     *
     * <h3>注意</h3>
     * <p>
     * 下发多个数据点命令不能用这个方法多次调用，一次性多次调用这个方法会导致模组无法正确接收消息，参考方法内注释。
     * </p>
     *
     * @param key
     *            数据点对应的标识名
     * @param value
     *            需要改变的值
     */
    private void sendCommand(String key, Object value) {
        if (value == null) {
            return;
        }
        int sn = 5;
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        // 同时下发多个数据点需要一次性在map中放置全部需要控制的key，value值
        // hashMap.put(key2, value2);
        // hashMap.put(key3, value3);
        mDevice.write(hashMap, sn);
        Log.i(TAG, "下发单个数据点命令：" + hashMap.toString());
    }

    /**
     * 下发多个数据点命令
     * @param map
     */
    private void sendCommads(ConcurrentHashMap<String, Object> map){
        int sn = 5;
        mDevice.write(map,sn);
        Log.i(TAG, "下发多个数据点命令：" + map.toString());
    }

    /*
     * 设备状态改变回调，只有设备状态为可控才可以下发控制命令
     */
    @Override
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
        super.didUpdateNetStatus(device, netStatus);
        if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
            mHandler.removeCallbacks(mRunnable);
            progressDialog.cancel();
        } else {
            mHandler.sendEmptyMessage(handler_key.DISCONNECT.ordinal());
        }
    }

    /*
     * 设备上报数据回调，此回调包括设备主动上报数据、下发控制命令成功后设备返回ACK
     */
    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                  ConcurrentHashMap<String, Object> dataMap, int sn) {
        super.didReceiveData(result, device, dataMap, sn);
        Log.i(TAG, "接收到数据");
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS && dataMap.get("data") != null) {
            getDataFromReceiveDataMap(dataMap);
            mHandler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
        }
    }

    /**
     * 更新UI，设置家具状态
     */
    private void updateUI(){
        //获取家具状态
        getState();
        //设置卧室灯状态
        if (isOpenBedRoomLight)
            bedRoomLightStatus.setText("关闭卧室灯");
        else
            bedRoomLightStatus.setText("开启卧室灯");
    }

    /**
     * 获取家具状态
     */
    private void getState(){
        if (data_RGB_Brightness > 0 &&
            (data_RGB_red > 0 || data_RGB_blue > 0 || data_RGB_green > 0))
            isOpenBedRoomLight = true;
        else
            isOpenBedRoomLight = false;
        if (data_ESP8266_relay)
            isOpenAirConditioning = true;
        else
            isOpenAirConditioning = false;
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showPasswordDialog() {
        if (passwordConfirmDialog == null)
            passwordConfirmDialog = new PasswordConfirmDialog(this);
        passwordConfirmDialog.show();
        passwordConfirmDialog.getWindow()
            .clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        passwordConfirmDialog.getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        passwordConfirmDialog.setCancelClickedListener(new PasswordConfirmDialog.CancelClickedListener() {
            @Override
            public void canceled(View view) {
                hidePasswordDialog();
                KeyBoardUtil.hideKeyboard(moreOperationLayout);
                finish();
            }
        });
        passwordConfirmDialog.setConfirmClickedListener(new ConfirmClickedListener());
    }

    @Override
    public void hidePasswordDialog() {
        if (passwordConfirmDialog != null)
            passwordConfirmDialog.dismiss();
    }


    /**
     * 确认密码按钮点击时间监听事件
     */
    class ConfirmClickedListener implements PasswordConfirmDialog.ConfirmClickedListener{
        @Override
        public void confirmed(View view) {
            String inputPassword = passwordConfirmDialog.getPassword();
            String password = LoginUtil.getInstance().getUser().getPassword();
            if (inputPassword.equals(password)){
                ToastUtil.makeText(getViewContext(), "密码输入正确，请进行操作");
                hidePasswordDialog();
                KeyBoardUtil.hideKeyboard(moreOperationLayout);
                getStatusOfDevice();
            }else {
                ToastUtil.makeText(getViewContext(), "密码输入错误，请重新输入");
                passwordConfirmDialog.resetPassword();
            }
        }
    }


    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        // 退出页面，取消设备订阅
        mDevice.setSubscribe(false);
        mDevice.setListener(null);
    }
}
