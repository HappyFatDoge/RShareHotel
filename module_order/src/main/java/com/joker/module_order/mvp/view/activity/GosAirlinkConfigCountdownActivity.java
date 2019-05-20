package com.joker.module_order.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.commonres.utils.GosConstant;
import com.example.commonres.utils.GosDeploy;
import com.example.commonres.utils.ToastUtil;
import com.example.commonres.view.RoundProgressBar;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosAirlinkConfigCountdownComponent;
import com.joker.module_order.di.module.GosAirlinkConfigCountdownModule;
import com.joker.module_order.mvp.contract.GosAirlinkConfigCountdownContract;
import com.joker.module_order.mvp.presenter.GosAirlinkConfigCountdownPresenter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gizwits.gizwifisdk.enumration.GizWifiErrorCode.GIZ_SDK_ONBOARDING_STOPPED;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosAirlinkConfigCountdownActivity
    extends GosConfigModuleBaseActivity<GosAirlinkConfigCountdownPresenter>
    implements GosAirlinkConfigCountdownContract.View {

    /** The tv Time */
    //private TextView tvTimer;

    /**
     * The rpb Config
     */
    @BindView(R2.id.rpbConfig)
    RoundProgressBar rpbConfig;

    /**
     * 倒计时
     */
    int secondleft = 60;

    /**
     * The timer
     */
    Timer timer;

    /**
     * 配置用参数
     */
    String workSSID, workSSIDPsw;


    List<GizWifiGAgentType> modeList, modeDataList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosAirlinkConfigCountdownComponent
            .builder()
            .appComponent(appComponent)
            .gosAirlinkConfigCountdownModule(new GosAirlinkConfigCountdownModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_airlink_config_countdown;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        spf = getSharedPreferences(SPF_Name, Context.MODE_PRIVATE);
        workSSID = spf.getString("workSSID", "");
        workSSIDPsw = spf.getString("workSSIDPsw", "");
        modeDataList = new ArrayList<GizWifiGAgentType>();
        modeDataList.add(GizWifiGAgentType.GizGAgentESP);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP);
        modeDataList.add(GizWifiGAgentType.GizGAgentHF);
        modeDataList.add(GizWifiGAgentType.GizGAgentRTK);
        modeDataList.add(GizWifiGAgentType.GizGAgentWM);
        modeDataList.add(GizWifiGAgentType.GizGAgentQCA);
        modeDataList.add(GizWifiGAgentType.GizGAgentFlyLink);
        modeDataList.add(GizWifiGAgentType.GizGAgentTI);
        modeDataList.add(GizWifiGAgentType.GizGAgentFSK);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP3);
        modeDataList.add(GizWifiGAgentType.GizGAgentBL);
        modeDataList.add(GizWifiGAgentType.GizGAgentAtmelEE);
        modeDataList.add(GizWifiGAgentType.GizGAgentOther);
        modeList = new ArrayList<GizWifiGAgentType>();

        String types = spf.getString("modulestyles", null);
        if (types != null) {
            try {
                JSONArray array = new JSONArray(types);
                for (int i = 0; i < array.length(); i++) {
                    int type = (Integer) array.get(i);
                    modeList.add(modeDataList.get(type));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        startAirlink();
    }

    @OnClick(R2.id.back)
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.back) {
            Intent intent = new Intent(this, GosDeviceListActivity.class);
            quitAlert(this, intent, getString(R.string.cancel_configuration));
        }
    }

    private void initView(){
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();

        RelativeLayout cel_layout = findViewById(R.id.params);
        ViewGroup.LayoutParams params = cel_layout.getLayoutParams();
        params.height = width;
        params.width = width;
        cel_layout.setLayoutParams(params);
    }

    private void startAirlink() {
        switch (GosConstant.mNew) {
            case 0:
                GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw,
                    GizWifiConfigureMode.GizWifiAirLink, null, 60, modeList);
                break;
            case 1:
                GizWifiSDK.sharedInstance().setDeviceOnboardingByBind(workSSID, workSSIDPsw,
                    GizWifiConfigureMode.GizWifiAirLink, null, 60, modeList);
                break;
            case 2:
                GizWifiSDK.sharedInstance().setDeviceOnboardingDeploy(workSSID, workSSIDPsw,
                    GizWifiConfigureMode.GizWifiAirLink, null, 60, modeList, false);
                break;
            case 3:
                GizWifiSDK.sharedInstance().setDeviceOnboardingDeploy(workSSID, workSSIDPsw,
                    GizWifiConfigureMode.GizWifiAirLink, null, 60, modeList, true);
                break;
        }
        handler.sendEmptyMessage(handler_key.START_TIMER.ordinal());
    }

    @Override
    public Context getViewContext() {
        return this;
    }


    private enum handler_key {

        /**
         * 倒计时提示
         */
        TIMER_TEXT,

        /**
         * 倒计时开始
         */
        START_TIMER,

        /**
         * 配置成功
         */
        SUCCESSFUL,

        /**
         * 配置失败
         */
        FAILED,

    }

    /**
     * The handler.
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {

                case START_TIMER:
                    isStartTimer();
                    break;

                case SUCCESSFUL:
                    ToastUtil.makeText(getViewContext(), R.string.configuration_successful);
                    Intent intent1 = new Intent(GosAirlinkConfigCountdownActivity.this, GosDeviceListActivity.class);
                    startActivity(intent1);
                    break;

                case FAILED:
                    if (GosDeploy.appConfig_Config_Softap()) {
                        ToastUtil.makeText(getViewContext(), R.string.configuration_timeout);
                        Intent intent = new Intent(
                            GosAirlinkConfigCountdownActivity.this,
                            GosDeviceReadyActivity.class);
                        /**   判断是否是从一键配置界面传过去的  */
                        intent.putExtra("isAirLink", true);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(
                            GosAirlinkConfigCountdownActivity.this,
                            GosConfigFailedActivity.class);
                        /**   判断是否是从一键配置界面传过去的  */
                        intent.putExtra("isAirLink", true);
                        startActivity(intent);
                    }
                    break;

                default:
                    break;

            }
        }

    };


    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GosAirlinkConfigCountdownActivity.this, GosDeviceListActivity.class);
            //quitAlert(this, intent);
            quitAlert(this, intent, getString(R.string.cancel_configuration));
            return true;
        }
        return false;
    }

    // 倒计时
    public void isStartTimer() {

        secondleft = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondleft--;
                rpbConfig.setProgress((60 - secondleft) * (100 / 60.0));
            }
        }, 1000, 1000);
    }

    /**
     * 设备配置回调
     *
     * @param result     错误码
     * @param mac        MAC
     * @param did        DID
     * @param productKey PK
     */
    protected void didSetDeviceOnboarding(GizWifiErrorCode result, String mac,
                                          String did, String productKey) {
        if (GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING == result) {
            return;
        }

        if (timer != null) {
            timer.cancel();
        }
        Message message = new Message();
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            message.what = handler_key.SUCCESSFUL.ordinal();
        } else if (result == GIZ_SDK_ONBOARDING_STOPPED) {

        } else {
            message.what = handler_key.FAILED.ordinal();
            message.obj = toastError(result);
        }
        Log.i("Apptest", result.toString());
        handler.sendMessage(message);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
}
