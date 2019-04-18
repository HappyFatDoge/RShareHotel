package com.joker.module_order.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
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
import com.example.commonres.dialog.PasswordConfirmDialog;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.KeyBoardUtil;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerMoreOperationComponent;
import com.joker.module_order.di.module.MoreOperationModule;
import com.joker.module_order.mvp.contract.MoreOperationContract;
import com.joker.module_order.mvp.presenter.MoreOperationPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 订单Fragment -> 待入住订单Fragment -> 订单更多操作
 */
@Route(path = RouterHub.ORDER_MOREOPERATIONACTIVITY)
public class MoreOperationActivity extends BaseActivity<MoreOperationPresenter>
    implements MoreOperationContract.View {


    @BindView(R2.id.more_operation_layout)
    LinearLayout moreOperationLayout;
    //设置文字
    //卧室灯状态
    @BindView(R2.id.bedroom_light_status)
    TextView bedRoomLightStatus;
    //厨房灯状态
    @BindView(R2.id.kitchen_light_status)
    TextView kitchenLightStatus;
    //厕所灯状态
    @BindView(R2.id.toilet)
    TextView toiletLightState;


    private Order mOrder;
    private PasswordConfirmDialog passwordConfirmDialog;

    private Boolean isOpenBedRoomLight = false;
    private Boolean isOpenKitchenLight = false;
    private Boolean isOpenAirConditioning = false;
    private Boolean isOpenToiletLight = false;
//    private Boolean isOpenTelevision = false;

    private static final int openBedRoomLight = 1;
    private static final int closeBedRoomLight = 2;
    private static final int openKitchenLight = 3;
    private static final int closeKitchenLight = 4;
    private static final int openAirConditioning = 5;
    private static final int closeAirConditioning = 6;
    private static final int openToiletLight = 7;
    private static final int closeToiletLight = 8;

    private String wifiIp ="192.168.1.212:8080";
    private boolean isConnect = false;
    private WifiManager wifiManager = null;
    private Thread mThreadClient = null;
    private Socket mSocketClient = null;
    private static BufferedReader mBufferedReaderServer	= null;
    private static PrintWriter mPrintWriterServer = null;
    private static InputStream mBufferedReaderClient	= null;
    private static PrintWriter mPrintWriterClient = null;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMoreOperationComponent
            .builder()
            .appComponent(appComponent)
            .moreOperationModule(new MoreOperationModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_more_operation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mOrder = (Order) getIntent().getSerializableExtra("order");
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //获取家具状态

        //设置家具的状态
        //设置卧室灯状态
        if (isOpenBedRoomLight)
            bedRoomLightStatus.setText("关闭卧室灯");
        else
            bedRoomLightStatus.setText("开启卧室灯");

        //设置厨房灯状态
        if (isOpenKitchenLight)
            kitchenLightStatus.setText("关闭厨房灯");
        else
            kitchenLightStatus.setText("开启厨房灯");

        //设置厕所灯状态
        if (isOpenToiletLight)
            toiletLightState.setText("关闭厕所灯");
        else
            toiletLightState.setText("开启厕所灯");

        //进行密码输入确认
        showLoading();
    }


    @OnClick({R2.id.back,R2.id.close_or_open_bedroom_bulb_iv,
        R2.id.close_or_open_kitchen_bulb_iv,R2.id.close_or_open_air_conditioning,
        R2.id.close_or_open_television,R2.id.close_or_open_toilet_bulb_iv,
        R2.id.close_or_open_water_heater,R2.id.close_or_open_curtain,
        R2.id.close_or_open_window,R2.id.sleep_mode_iv,
        R2.id.electric_iv,R2.id.operate_mode_iv,
        R2.id.thermometer_iv,R2.id.check_out})
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)//返回
            killMyself();
        else if (viewId == R.id.close_or_open_bedroom_bulb_iv)//开/关卧室灯
            bedRoomLight();
        else if (viewId == R.id.close_or_open_kitchen_bulb_iv)//开/关厨房灯
            kitchenLight();
        else if (viewId == R.id.close_or_open_air_conditioning)//空调控制
            airConditioningControl();
        else if (viewId == R.id.close_or_open_toilet_bulb_iv)//开/关厕所灯
            toiletLight();
        else if (viewId == R.id.close_or_open_television) {//电视机控制
//            if (!isOpenTelevision) {
//                ToastUtil.makeText(this, "电视机已开启");
//                televisionState.setText("关闭电视");
//                isOpenTelevision = true;
//            }else{
//                ToastUtil.makeText(this, "电视机已关闭");
//                televisionState.setText("开启电视");
//                isOpenTelevision = false;
//            }
        }else if (viewId == R.id.close_or_open_water_heater) {//热水器控制

        }else if (viewId == R.id.close_or_open_curtain) {//窗帘布控制

        }else if (viewId == R.id.close_or_open_window) {//窗户控制

        }else if (viewId == R.id.sleep_mode_iv) {//睡眠模式

        }else if (viewId == R.id.electric_iv) {//电表

        }else if (viewId == R.id.operate_mode_iv) {//模式

        }else if (viewId == R.id.thermometer_iv) {//客房环境

        }else if (viewId == R.id.check_out)//退房
            checkOut();
    }


    /**
     * 开/关卧室灯
     */
    private void bedRoomLight(){
        if (wifiManager.isWifiEnabled()) {//判断wifi是否开启
            if (!isConnect) {
                ToastUtil.makeText(getViewContext(), "尚未进行wifi服务连接，wifi服务连接中...");
                connect();//连接wifi服务
            } else {
                if (!isOpenBedRoomLight)
                    //打开卧室灯
                    mPresenter.sendMessageAndControl(openBedRoomLight, "e",
                        "卧室灯开启失败", "卧室灯已开启",
                        mPrintWriterClient,mSocketClient);
                else
                    //关闭卧室灯
                    mPresenter.sendMessageAndControl(closeBedRoomLight, "f",
                        "卧室灯关闭失败", "卧室灯已关闭",
                        mPrintWriterClient,mSocketClient);
            }
        }else {
            ToastUtil.makeText(getViewContext(), "您尚未开启wifi进行连接，请先允许开启wifi进行连接");
            requestOpenWifi();
            connect();
        }
    }


    /**
     * 开/关厨房灯
     */
    private void kitchenLight(){
        if (wifiManager.isWifiEnabled()) {//判断wifi是否开启
            if (!isConnect) {
                ToastUtil.makeText(getViewContext(), "尚未进行wifi服务连接，wifi服务连接中...");
                connect();//连接wifi服务
            } else {
                if (!isOpenKitchenLight)
                    //打开厨房灯
                    mPresenter.sendMessageAndControl(openKitchenLight, "c",
                            "厨房灯开启失败", "厨房灯已开启",
                            mPrintWriterClient,mSocketClient);
                else
                    //关闭厨房灯
                    mPresenter.sendMessageAndControl(closeKitchenLight, "d",
                            "厨房灯关闭失败", "厨房灯已关闭",
                            mPrintWriterClient,mSocketClient);
            }
        }else {
            ToastUtil.makeText(getViewContext(), "您尚未开启wifi进行连接，请先允许开启wifi进行连接");
            requestOpenWifi();
            connect();
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
            if (wifiManager.isWifiEnabled()) {//判断wifi是否开启
                if (!isConnect) {
                    ToastUtil.makeText(getViewContext(), "尚未进行wifi服务连接，wifi服务连接中...");
                    connect();//连接wifi服务
                } else {
                    if (!isOpenAirConditioning)
                        //打开空调
                        mPresenter.sendMessageAndControl(openAirConditioning, "g",
                                "空调开启失败", "空调已开启",
                                mPrintWriterClient,mSocketClient);
                    else
                        //关闭空调
                        mPresenter.sendMessageAndControl(closeAirConditioning, "h",
                                "空调关闭失败", "空调已关闭",
                                mPrintWriterClient,mSocketClient);
                }
            }else {
                ToastUtil.makeText(getViewContext(), "您尚未开启wifi进行连接，请先允许开启wifi进行连接");
                requestOpenWifi();
                connect();
            }
        }
    }


    /**
     * 开/关厕所灯
     */
    private void toiletLight(){
        if (wifiManager.isWifiEnabled()) {//判断wifi是否开启
            if (!isConnect) {
                ToastUtil.makeText(getViewContext(), "尚未进行wifi服务连接，wifi服务连接中...");
                connect();//连接wifi服务
            } else {
                if (!isOpenToiletLight)
                    //打开厕所灯
                    mPresenter.sendMessageAndControl(openToiletLight, "a",
                            "厕所灯开启失败", "厕所灯已开启",
                            mPrintWriterClient,mSocketClient);
                else
                    //关闭厕所灯
                    mPresenter.sendMessageAndControl(closeToiletLight, "b",
                            "厕所灯关闭失败", "厕所灯已关闭",
                            mPrintWriterClient,mSocketClient);
            }
        }else {
            ToastUtil.makeText(getViewContext(), "您尚未开启wifi进行连接，请先允许开启wifi进行连接");
            requestOpenWifi();
            connect();
        }
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


    @Override
    public Context getViewContext() {
        return this;
    }


    /**
     * 根据传输的信息对家具进行智能控制的结果
     * @param result
     * @param tips
     * @param message
     */
    @Override
    public void sendMessageResult(Boolean result, String tips, Message message) {
        if (tips == null)
            mHandler.sendMessage(message);
        else
            ToastUtil.makeText(this, tips);
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
                passwordConfirmDialog.dismiss();
                KeyBoardUtil.hideKeyboard(moreOperationLayout);
                requestOpenWifi();//请求打开Wifi
                connect();
            }else {
                ToastUtil.makeText(getViewContext(), "密码输入错误，请重新输入");
                passwordConfirmDialog.resetPassword();
            }
        }
    }

    /**
     * 开启wifi并连接
     */
    private void requestOpenWifi(){
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);
    }

    /**
     * 连接wifi服务
     */
    private void connect(){
        mThreadClient = new Thread(mRunnable);
        mThreadClient.start();
    }


    /**
     * 断开wifi服务
     */
    private void disConnect(){
        if (isConnect){
            isConnect = false;
            try {
                if(mSocketClient!=null) {
                    mSocketClient.close();
                    mSocketClient = null;

                    mPrintWriterClient.close();
                    mPrintWriterClient = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mThreadClient.interrupt();
        }
    }

    /**
     * wifi服务runnable
     */
    private Runnable mRunnable = new Runnable() {
        public void run() {
            if(wifiIp.length()<=0) {
                Log.d("data", "IP can't be empty!");
                return;
            }
            int start = wifiIp.indexOf(":");
            if( (start == -1) ||(start+1 >= wifiIp.length()) ) {
                Log.d("data", "IP address is error!");
                return;
            }
            String sIP = wifiIp.substring(0, start);
            String sPort = wifiIp.substring(start+1);
            int port = Integer.parseInt(sPort);

            Log.d("gjz", "IP:"+ sIP + ":" + port);

            try {
                //连接
                mSocketClient = new Socket(sIP, port);	//portnum
                //获取输入输出流
                mBufferedReaderClient = mSocketClient.getInputStream();

                mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
                Log.d("data", "connected to server!");
            } catch (Exception e) {
                Log.d("data", "connecting IP is error:" + e.toString() + e.getMessage());
                return;
            }

            isConnect = true;

            byte[] buffer = new byte[1024];
            int count = 0;
            while (isConnect) {
                try {
                    mBufferedReaderClient.read(buffer);
                } catch (Exception e) {
                }
            }
        }
    };



    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String tips = (String) msg.obj;
            ToastUtil.makeText(getViewContext(),tips);
            switch (msg.what){
                case openBedRoomLight:
                    isOpenBedRoomLight = true;
                    bedRoomLightStatus.setText("关闭卧室灯");
                    break;
                case closeBedRoomLight:
                    isOpenBedRoomLight = false;
                    bedRoomLightStatus.setText("开启卧室灯");
                    break;
                case openKitchenLight:
                    isOpenKitchenLight = true;
                    kitchenLightStatus.setText("关闭厨房灯");
                    break;
                case closeKitchenLight:
                    isOpenKitchenLight = false;
                    kitchenLightStatus.setText("开启厨房灯");
                    break;
                case openAirConditioning:
                    isOpenAirConditioning = true;
                    break;
                case closeAirConditioning:
                    isOpenAirConditioning = false;
                    break;
                case openToiletLight:
                    isOpenToiletLight = true;
                    toiletLightState.setText("关闭厕所灯");
                    break;
                case closeToiletLight:
                    isOpenToiletLight = false;
                    toiletLightState.setText("开启厕所灯");
                    break;
            }
        }
    };


    @Override
    public void showLoading() {
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
                passwordConfirmDialog.dismiss();
                KeyBoardUtil.hideKeyboard(moreOperationLayout);
                finish();
            }
        });
        passwordConfirmDialog.setConfirmClickedListener(new ConfirmClickedListener());
    }

    @Override
    public void hideLoading() {
        if (passwordConfirmDialog != null)
            passwordConfirmDialog.dismiss();
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
        disConnect();//断开wifi服务
        super.onDestroy();
    }
}
