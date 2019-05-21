package com.joker.module_order.mvp.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.Order;
import com.example.commonres.dialog.PasswordConfirmDialog;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.utils.GosDeploy;
import com.example.commonres.utils.KeyBoardUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonres.view.SlideListView2;
import com.example.commonres.view.VerticalSwipeRefreshLayout;
import com.example.commonsdk.core.RouterHub;
import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosDeviceListComponent;
import com.joker.module_order.di.module.GosDeviceListModule;
import com.joker.module_order.mvp.contract.GosDeviceListContract;
import com.joker.module_order.mvp.presenter.GosDeviceListPresenter;
import com.joker.module_order.mvp.view.GosMessageHandler;
import com.joker.module_order.mvp.view.GosPushManager;
import com.joker.module_order.mvp.view.adapter.GosDeviceListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.ORDER_GOSDEVICELISTACTIVITY)
public class GosDeviceListActivity
    extends GosBaseActivity<GosDeviceListPresenter>
    implements GosDeviceListContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    /**
     * The ll NoDevice
     */
    @BindView(R2.id.llNoDevice)
    ScrollView llNoDevice;

    @BindView(R2.id.id_swipe_ly)
    VerticalSwipeRefreshLayout mSwipeLayout;

    @BindView(R2.id.id_swipe_ly1)
    VerticalSwipeRefreshLayout mSwipeLayout1;

    /**
     * The img NoDevice
     */
    @BindView(R2.id.imgNoDevice)
    ImageView imgNoDevice;

    /**
     * The btn NoDevice
     */
    @BindView(R2.id.btnNoDevice)
    Button btnNoDevice;

    /**
     * The ic BoundDevices
     */
    @BindView(R2.id.icBoundDevices)
    View icBoundDevices;

    /**
     * The ic FoundDevices
     */
    @BindView(R2.id.icFoundDevices)
    View icFoundDevices;


    /**
     * The tv BoundDevicesListTitle
     */
    private TextView tvBoundDevicesListTitle;

    /**
     * The tv FoundDevicesListTitle
     */
    private TextView tvFoundDevicesListTitle;


    /**
     * The ll NoBoundDevices
     */
    private LinearLayout llNoBoundDevices;

    /**
     * The ll NoFoundDevices
     */
    private LinearLayout llNoFoundDevices;


    /**
     * The slv BoundDevices
     */
    private SlideListView2 slvBoundDevices;

    /**
     * The slv FoundDevices
     */
    private SlideListView2 slvFoundDevices;


    /**
     * The sv ListGroup
     */
    @BindView(R2.id.svListGroup)
    ScrollView svListGroup;

    /**
     * 适配器
     */
    private GosDeviceListAdapter myadapter;

    private GosDeviceListAdapter myadapter1;

    /**
     * 设备列表分类
     */
    List<GizWifiDevice> boundDevicesList = new ArrayList<GizWifiDevice>();
    List<GizWifiDevice> foundDevicesList = new ArrayList<GizWifiDevice>();
    List<GizWifiDevice> offlineDevicesList = new ArrayList<GizWifiDevice>();

    /**
     * 设备热点名称列表
     */
    ArrayList<String> softNameList;

    /**
     * 与APP绑定的设备的ProductKey
     */
    private List<String> ProductKeyList;

    Intent intent;

    String softssid, uid, token;

    public static List<String> boundMessage;

    /**
     * 判断用户登录状态 0：未登录 1：实名用户登录 2：匿名用户登录 3：匿名用户登录中 4：匿名用户登录中断
     */
    public static int loginStatus;

    int threeSeconds = 3;

    /**
     * 获取设备列表
     */
    protected static final int GETLIST = 0;

    /**
     * 刷新设备列表
     */
    protected static final int UPDATALIST = 1;

    /**
     * 订阅成功前往控制页面
     */
    protected static final int TOCONTROL = 2;

    /**
     * 通知
     */
    protected static final int TOAST = 3;

    /**
     * 设备绑定
     */
    protected static final int BOUND = 9;

    /**
     * 设备解绑
     */
    protected static final int UNBOUND = 99;


    private static final int PULL_TO_REFRESH = 888;

    private static final String TAG = "GosDeviceListFragment";

    private Order mOrder;


    Handler handler = new Handler() {
        private AlertDialog myDialog;
        private TextView dialog_name;

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETLIST:
                    Log.e(TAG, "handleMessage:GETLIST -----------------" + uid);
                    if (!uid.isEmpty() && !token.isEmpty()) {
                        // GizWifiSDK.sharedInstance().getBoundDevices(uid, token, ProductKeyList);
                        GizWifiSDK.sharedInstance().getBoundDevices(uid, token);
                    }

                    //login_anonymous-false-start
                    if (loginStatus == 0 && GosDeploy.appConfig_Login_Anonymous()) {
                        loginStatus = 3;
                        GizWifiSDK.sharedInstance().userLoginAnonymous();
                    }
                    //login_anonymous-false-end
                    break;

                case UPDATALIST:
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                    UpdateUI();
                    break;

                case BOUND:

                    break;

                case UNBOUND:
                    showLoading();
                    GizWifiSDK.sharedInstance().unbindDevice(uid, token, msg.obj.toString());
                    break;

                case TOCONTROL:
                    //更多操作
                    GizWifiDevice device = (GizWifiDevice) msg.obj;
                    ARouter.getInstance()
                        .build(RouterHub.ORDER_MORECONTROLACTIVITY)
                        .withSerializable("order",mOrder)
                        .withParcelable("GizWifiDevice", device)
                        .navigation(GosDeviceListActivity.this, 1);
//                    intent = null;
//                    Bundle bundle = new Bundle();
//                    if (intent == null) {
//                        intent = new Intent(getViewContext(), MoreOperationActivity.class);
//                    }
//                    bundle.putParcelable("GizWifiDevice", device);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, 1);
                    break;

                case TOAST:
                    Toast.makeText(getViewContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                case PULL_TO_REFRESH:
                    handler.sendEmptyMessage(GETLIST);
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout1.setRefreshing(false);

                    break;
            }
        }
    };

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosDeviceListComponent
            .builder()
            .appComponent(appComponent)
            .gosDeviceListModule(new GosDeviceListModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_device_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mOrder = (Order) getIntent().getSerializableExtra("order");
        GosMessageHandler.getSingleInstance().StartLooperWifi(this);
        spf = getSharedPreferences(SPF_Name, Context.MODE_PRIVATE);
        boundMessage = new ArrayList<>();
        // ProductKeyList = GosDeploy.setProductKeyList();
        ProductKeyList = null;
        uid = spf.getString("Uid", "");
        token = spf.getString("Token", "");
        if (uid.isEmpty() && token.isEmpty()) {
            loginStatus = 0;
        }
        initView();
        initEvent();
    }

    private void initView(){
        slvBoundDevices = icBoundDevices.findViewById(R.id.slideListView1);
        slvFoundDevices = icFoundDevices.findViewById(R.id.slideListView1);

        llNoBoundDevices = icBoundDevices.findViewById(R.id.llHaveNotDevice);
        llNoFoundDevices = icFoundDevices.findViewById(R.id.llHaveNotDevice);

        tvBoundDevicesListTitle = icBoundDevices.findViewById(R.id.tvListViewTitle);
        tvFoundDevicesListTitle = icFoundDevices.findViewById(R.id.tvListViewTitle);

        String boundDevicesListTitle = null;
        String foundDevicesListTitle = null;
        boundDevicesListTitle = (String) getText(R.string.my_device);
        foundDevicesListTitle = (String) getText(R.string.found_devices);

        tvBoundDevicesListTitle.setText(boundDevicesListTitle);

        tvFoundDevicesListTitle.setText(foundDevicesListTitle);

        //下拉刷新
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout1.setOnRefreshListener(this);
        mSwipeLayout1.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initEvent() {
        myadapter = new GosDeviceListAdapter(this, foundDevicesList);
        myadapter.setHandler(handler);
        myadapter.setSpf(spf);
        slvFoundDevices.setAdapter(myadapter);
        myadapter1 = new GosDeviceListAdapter(this, boundDevicesList);
        myadapter1.setHandler(handler);
        myadapter1.setSpf(spf);
        slvBoundDevices.setAdapter(myadapter1);
        // config-all-start
        imgNoDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice();
            }
        });
        btnNoDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice();
            }
        });
        // config-all-end
        slvFoundDevices.initSlideMode(SlideListView2.MOD_FORBID);
        slvFoundDevices.setFocusable(false);
        slvBoundDevices.initSlideMode(SlideListView2.MOD_RIGHT);
        slvBoundDevices.setFocusable(false);
        slvFoundDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                slvFoundDevices.setEnabled(false);
                slvFoundDevices.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slvFoundDevices.setEnabled(true);
                    }
                }, 1000);
                final GizWifiDevice device = foundDevicesList.get(position);
                device.setListener(getGizWifiDeviceListener(device));
                if (device.getNetStatus() != GizWifiDeviceNetStatus.GizDeviceOffline) {
                    boolean isAuto = false;

                    List<Map<String, String>> list2 = GosDeploy.appConfig_ProductList();
                    for (Map<String, String> map2 : list2) {
                        String productkey = device.getProductKey();
                        Iterator it1 = map2.entrySet().iterator();
                        while (it1.hasNext()) {
                            Map.Entry entry1 = (Map.Entry) it1.next();
                            if (productkey.equals(entry1.getKey())) {
                                isAuto = true;
                                device.setSubscribe(entry1.getValue().toString(), true);
                                showLoading();
                                break;
                            }
                        }
                    }

                    if (device.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOnline
                        && !TextUtils.isEmpty(device.getDid()) && !device.isBind()
                        && device.getProductType() == GizWifiDeviceType.GizDeviceSub) {

                        if (!isAuto) {
                            PasswordConfirmDialog passwordDialog = new PasswordConfirmDialog(getViewContext());
                            passwordDialog.show();
                            passwordDialog.getWindow()
                                .clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                            passwordDialog.getWindow()
                                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            passwordDialog.setTitle("请输入产品密钥绑定设备");
                            passwordDialog.setHint("请输入32位的Product Secret");
                            passwordDialog.setCancelClickedListener(new PasswordConfirmDialog.CancelClickedListener() {
                                @Override
                                public void canceled(View view) {
                                    passwordDialog.dismiss();
                                    KeyBoardUtil.hideKeyboard(view);
                                }
                            });
                            passwordDialog.setConfirmClickedListener(new PasswordConfirmDialog.ConfirmClickedListener() {
                                @Override
                                public void confirmed(View view) {
                                    String ps = passwordDialog.getPassword();
                                    if (TextUtils.isEmpty(ps) || ps.length() != 32) {
                                        ToastUtil.makeText(getViewContext(),"产品密钥有误，请重新输入");
                                    } else {
                                        device.setSubscribe(passwordDialog.getPassword(), true);
                                        //device.setSubscribe(true);
                                        showLoading();
                                        /**隐藏软键盘**/
                                        KeyBoardUtil.hideKeyboard(view);
                                        passwordDialog.dismiss();
                                    }
                                }
                            });
                        }
                    } else {
//                           device.setSubscribe(null, true);
                        if (!isAuto) {
                            device.setSubscribe(null, true);
                            showLoading();
                        }
                    }
                }

            }
        });

        slvBoundDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (position < boundDevicesList.size()) {

                    slvBoundDevices.setEnabled(false);
                    slvBoundDevices.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slvBoundDevices.setEnabled(true);
                        }
                    }, 1000);

                    final GizWifiDevice device = boundDevicesList.get(position);
                    device.setListener(getGizWifiDeviceListener(device));
                    if (device.getNetStatus() != GizWifiDeviceNetStatus.GizDeviceOffline) {
                        String productKey = device.getProductKey();
                        List<Map<String, String>> list2 = GosDeploy.appConfig_ProductList();
                        boolean isSubscribe = false;
                        for (Map<String, String> map2 : list2) {
                            Iterator it1 = map2.entrySet().iterator();
                            while (it1.hasNext()) {
                                Map.Entry entry1 = (Map.Entry) it1.next();
                                if (productKey.equals(entry1.getKey())) {
                                    isSubscribe = true;
                                    device.setSubscribe(entry1.getValue().toString(), true);
                                }
                            }
                        }
                        if (!isSubscribe) {
                            device.setSubscribe(null, true);
                            showLoading();
                        }
                    }
                }

            }
        });
    }


    @OnClick({R2.id.back,R2.id.right_image})
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.back)
            finish();
        else if (viewId == R.id.right_image)
            addDevice();
    }

    protected void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
        Log.e(TAG, "didDiscovered: 更新数据---------------");
        GosBaseActivity.deviceslist.clear();
        for (GizWifiDevice gizWifiDevice : deviceList) {
            GosBaseActivity.deviceslist.add(gizWifiDevice);
        }
        handler.sendEmptyMessage(UPDATALIST);
    }

    protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {
        Log.e(TAG, "didUserLogin: -----------");
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            loginStatus = 2;
            this.uid = uid;
            this.token = token;
            spf.edit().putString("Uid", this.uid).commit();
            spf.edit().putString("Token", this.token).commit();
            handler.sendEmptyMessage(GETLIST);
            // TODO 绑定推送
            //GosPushManager.pushBindService(token);
            if (GosDeploy.appConfig_Push_BaiDu()) {
                GosPushManager.pushBindService(token);
            }
            if (GosDeploy.appConfig_Push_JiGuang()) {
                GosPushManager.pushBindService(token);
            }
        } else {
            loginStatus = 0;
            if (GosDeploy.appConfig_Login_Anonymous()) {
                tryUserLoginAnonymous();
            }
        }
    }

    protected void didUnbindDevice(GizWifiErrorCode result, String did) {
        hideLoading();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            // String unBoundFailed = (String) getText(R.string.unbound_failed);
            ToastUtil.makeText(getViewContext(), toastError(result));
        }
    }

    @Override
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        // TODO 控制页面跳转
        hideLoading();
        Message msg = new Message();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            msg.what = TOCONTROL;
            msg.obj = device;
        } else {
            if (device.isBind()) {
                msg.what = TOAST;
                // String setSubscribeFail = (String)
                // getText(R.string.setsubscribe_failed);
                msg.obj = toastError(result);// setSubscribeFail + "\n" + arg0;
            }
        }
        handler.sendMessage(msg);
    }

    /**
     * 推送绑定回调
     *
     * @param result
     */
    @Override
    protected void didChannelIDBind(GizWifiErrorCode result) {
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            ToastUtil.makeText(getViewContext(), toastError(result));
        }
    }

    /**
     * 设备绑定回调(旧)
     *
     * @param error
     * @param errorMessage
     * @param did
     */
    protected void didBindDevice(int error, String errorMessage, String did) {
        hideLoading();
        if (error != 0) {

            String toast = getResources().getString(R.string.bound_failed) + "\n" + errorMessage;
            ToastUtil.makeText(getViewContext(),toast);
            // Toast.makeText(this, R.string.bound_failed + "\n" + errorMessage,
            // 2000).show();
        } else {
            ToastUtil.makeText(getViewContext(), R.string.bound_successful);
        }

    }

    /**
     * 设备绑定回调
     *
     * @param result
     * @param did
     */
    protected void didBindDevice(GizWifiErrorCode result, String did) {
        hideLoading();
        if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            ToastUtil.makeText(getViewContext(), toastError(result));
        } else {
            ToastUtil.makeText(getViewContext(), R.string.add_successful);
        }
    }


    private void UpdateUI() {

        if (GosBaseActivity.deviceslist.isEmpty()) {
            svListGroup.setVisibility(View.GONE);
            llNoDevice.setVisibility(View.VISIBLE);
            mSwipeLayout1.setVisibility(View.VISIBLE);
            return;
        } else {
            llNoDevice.setVisibility(View.GONE);
            mSwipeLayout1.setVisibility(View.GONE);
            svListGroup.setVisibility(View.VISIBLE);
        }

        if (boundDevicesList == null) {
            boundDevicesList = new ArrayList<GizWifiDevice>();
        } else {
            boundDevicesList.clear();
        }
        if (foundDevicesList == null) {
            foundDevicesList = new ArrayList<GizWifiDevice>();
        } else {
            foundDevicesList.clear();
        }
        if (offlineDevicesList == null) {
            offlineDevicesList = new ArrayList<GizWifiDevice>();
        } else {
            offlineDevicesList.clear();
        }

        for (GizWifiDevice gizWifiDevice : GosBaseActivity.deviceslist) {
            if (gizWifiDevice.isBind()) {
                boundDevicesList.add(gizWifiDevice);
            } else {
                foundDevicesList.add(gizWifiDevice);
            }
        }
        if (foundDevicesList.isEmpty()) {
            slvFoundDevices.setVisibility(View.GONE);
            llNoFoundDevices.setVisibility(View.VISIBLE);
        } else {
            if (myadapter == null) {
                myadapter = new GosDeviceListAdapter(getViewContext(), foundDevicesList);
                myadapter.setHandler(handler);
                myadapter.setSpf(spf);
                slvFoundDevices.setAdapter(myadapter);
            } else {
                myadapter.notifyDataSetChanged();
            }
            llNoFoundDevices.setVisibility(View.GONE);
            slvFoundDevices.setVisibility(View.VISIBLE);
        }
        if (boundDevicesList.isEmpty()) {
            slvBoundDevices.setVisibility(View.GONE);
            llNoBoundDevices.setVisibility(View.VISIBLE);
        } else {
            if (myadapter1 == null) {
                myadapter1 = new GosDeviceListAdapter(getViewContext(), boundDevicesList);
                myadapter1.setHandler(handler);
                myadapter1.setSpf(spf);
                slvBoundDevices.setAdapter(myadapter1);
            } else {
                if (slvBoundDevices.isSlided()) {
                    slvBoundDevices.slideBack();
                }
                myadapter1.notifyDataSetChanged();
            }
            llNoBoundDevices.setVisibility(View.GONE);
            slvBoundDevices.setVisibility(View.VISIBLE);
        }
    }

    private void addDevice() {
        if (GosDeploy.appConfig_Config_Softap()) {
            if (!checkNetwork(getViewContext())) {
                ToastUtil.makeText(getViewContext(),R.string.network_error);
                return;
            }
            if (GosDeploy.appConfig_Config_Airlink()) {
                final Dialog dialog = new AlertDialog.Builder(getViewContext(), R.style.alert_dialog_style)
                    .setView(new EditText(getViewContext())).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.alert_gos_overflow);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(layoutParams);
                LinearLayout llAirlink;
                LinearLayout llSoftap;
                llAirlink = (LinearLayout) window.findViewById(R.id.llAirlink);
                llSoftap = (LinearLayout) window.findViewById(R.id.llSoftap);
                llAirlink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getViewContext(), GosAirlinkChooseDeviceWorkWiFiActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                llSoftap.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getViewContext(), GosChooseDeviceWorkWiFiActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            } else {
                Intent intent = new Intent(getViewContext(), GosChooseDeviceWorkWiFiActivity.class);
                startActivity(intent);
            }
        } else {
            if (GosDeploy.appConfig_Config_Airlink()) {
                if (!checkNetwork(getViewContext())) {
                    ToastUtil.makeText(getViewContext(), R.string.network_error);
                    return;
                }
                Intent intent = new Intent(getViewContext(), GosAirlinkChooseDeviceWorkWiFiActivity.class);
                startActivity(intent);
            }
        }
    }

    private void tryUserLoginAnonymous() {
        threeSeconds = 3;
        final Timer tsTimer = new Timer();
        tsTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                threeSeconds--;
                if (threeSeconds <= 0) {
                    tsTimer.cancel();
                    handler.sendEmptyMessage(GETLIST);
                } else {
                    if (loginStatus == 4) {
                        tsTimer.cancel();
                    }
                }
            }
        }, 1000, 1000);
    }

    /**
     * 检查网络连通性（工具方法）
     *
     * @param context
     * @return
     */
    public boolean checkNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            return true;
        }
        return false;
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
    public Context getViewContext() {
        return this;
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(PULL_TO_REFRESH, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: -------------");
        handler.sendEmptyMessage(GETLIST);
        GosBaseActivity.deviceslist = GizWifiSDK.sharedInstance().getDeviceList();
        UpdateUI();
        // TODO GosMessageHandler.getSingleInstance().SetHandler(handler);
        if (boundMessage.size() != 0) {
            showLoading();
            if (boundMessage.size() == 2) {
                GizWifiSDK.sharedInstance().bindDevice(uid, token, boundMessage.get(0), boundMessage.get(1), null);
            } else if (boundMessage.size() == 1) {
                GizWifiSDK.sharedInstance().bindDeviceByQRCode(uid, token, boundMessage.get(0), false);
            } else if (boundMessage.size() == 3) {
                GizDeviceSharing.checkDeviceSharingInfoByQRCode(spf.getString("Token", ""), boundMessage.get(2));
            } else {
                Log.i("Apptest", "ListSize:" + boundMessage.size());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        boundMessage.clear();
    }
}
