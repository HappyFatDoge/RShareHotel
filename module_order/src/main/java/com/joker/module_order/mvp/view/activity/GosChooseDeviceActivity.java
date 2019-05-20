package com.joker.module_order.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonres.utils.AssetsUtils;
import com.example.commonres.utils.GosConstant;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosChooseDeviceComponent;
import com.joker.module_order.di.module.GosChooseDeviceModule;
import com.joker.module_order.mvp.contract.GosChooseDeviceContract;
import com.joker.module_order.mvp.presenter.GosChooseDevicePresenter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosChooseDeviceActivity
    extends GosConfigModuleBaseActivity<GosChooseDevicePresenter>
    implements GosChooseDeviceContract.View {


    /**
     * The list View
     */
    @BindView(R2.id.list_view)
    ListView listView;

    /**
     * 系统WiFi集合
     */
    ArrayList<ScanResult> list;

    /**
     * 设备热点集合
     */
    ArrayList<ScanResult> softList;

    /**
     * 适配器
     */
    Myadapter myadapter;

    /**
     * 计时器
     */
    Timer timer;

    private enum handler_key {

        /**
         * 刷新列表
         */
        UPDATALIST,

    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                // 更新列表
                case UPDATALIST:
                    initData();
                    break;
            }
        }

        ;

    };
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosChooseDeviceComponent
            .builder()
            .appComponent(appComponent)
            .gosChooseDeviceModule(new GosChooseDeviceModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_choose_device;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GosChooseDeviceActivity.this, GosConfigCountdownActivity.class);
                intent.putExtra("softSSID", softList.get(position).SSID);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initData() {
//        list = new ArrayList<ScanResult>();
        list = (ArrayList<ScanResult>) GosConstant.ssidList;
        //list = (ArrayList<ScanResult>) NetUtils.getCurrentWifiScanResult(GosChooseDeviceActivity.this);
        softList = new ArrayList<ScanResult>();
        ScanResult scanResult;
        for (int i = 0; i < list.size(); i++) {
            scanResult = list.get(i);
            if (scanResult.SSID.length() > SoftAP_Start.length()) {
                if (scanResult.SSID.contains(SoftAP_Start)) {
                    softList.add(scanResult);
                }
            }
        }
        myadapter = new Myadapter(softList);
        listView.setAdapter(myadapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(handler_key.UPDATALIST.ordinal());
            }
        }, 0, 3000);
    }

    @OnClick({R2.id.nodevice,R2.id.back})
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.nodevice){
            if (list == null) {
                LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                    Toast.makeText(this, getString(R.string.open), Toast.LENGTH_LONG).show();
                    return;
                }
            } else if (list.size() == 0) {
                LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                    Toast.makeText(this, getString(R.string.open), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            finish();
        } else if (viewId == R.id.back)
            killMyself();
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

    class Myadapter extends BaseAdapter {

        ArrayList<ScanResult> softList;

        public Myadapter(ArrayList<ScanResult> list) {
            this.softList = list;
        }

        @Override
        public int getCount() {
            return softList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            if (view == null) {
                view = LayoutInflater.from(GosChooseDeviceActivity.this).inflate(R.layout.item_gos_wifi_device, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {

                holder = (Holder) view.getTag();
            }
            String ssid = softList.get(position).SSID;

//			String itemStart = (String) getText(R.string.itemtext_start);
//			String itemEnd = (String) getText(R.string.itemtext_end);
            String s = "";
            if (AssetsUtils.isZh(GosChooseDeviceActivity.this)) {
                s = getString(R.string.device) + ssid.substring(ssid.length() - 4);
            } else {
                s = getString(R.string.device) + " " + ssid.substring(ssid.length() - 4);
            }
            holder.getTextView().setText(s);

            return view;
        }
    }

    class Holder {
        View view;

        public Holder(View view) {
            this.view = view;
        }

        TextView textView;

        public TextView getTextView() {
            if (textView == null) {
                textView = (TextView) view.findViewById(R.id.SSID_text);
            }
            return textView;
        }
    }

    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}
