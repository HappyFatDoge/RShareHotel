package com.joker.module_order.mvp.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.GosConstant;
import com.example.commonres.utils.GosDeploy;
import com.example.commonres.utils.NetUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosChooseDeviceWorkWiFiComponent;
import com.joker.module_order.di.module.GosChooseDeviceWorkWiFiModule;
import com.joker.module_order.mvp.contract.GosChooseDeviceWorkWiFiContract;
import com.joker.module_order.mvp.presenter.GosChooseDeviceWorkWiFiPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosChooseDeviceWorkWiFiActivity
    extends GosConfigModuleBaseActivity<GosChooseDeviceWorkWiFiPresenter>
    implements GosChooseDeviceWorkWiFiContract.View {

    private AlertDialog create;
    private ArrayList<ScanResult> wifiList;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION"};

    /**
     * wifi信息
     */
    public WifiInfo wifiInfo;

    /**
     * The et SSID
     */
    @BindView(R2.id.etSSID)
    EditText etSSID;

    /**
     * The et Psw
     */
    @BindView(R2.id.etPsw)
    EditText etPsw;

    /**
     * The btn Next
     */
    @BindView(R2.id.btnNext)
    Button btnNext;

    /**
     * The cb Laws
     */
    @BindView(R2.id.cbLaws)
    CheckBox cbLaws;

    /**
     * The img WiFiList
     */
    @BindView(R2.id.rlWiFiList)
    RelativeLayout rlWiFiList;

    /**
     * 配置用参数
     */
    private String softSSID, workSSID, workSSIDPsw;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosChooseDeviceWorkWiFiComponent
            .builder()
            .appComponent(appComponent)
            .gosChooseDeviceWorkWiFiModule(new GosChooseDeviceWorkWiFiModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_choose_device_work_wi_fi;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //softSSID = getIntent().getStringExtra("softssid");
        spf = getSharedPreferences(SPF_Name, Context.MODE_PRIVATE);
        workSSID = spf.getString("workSSID", "");
        initView();
        cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = etPsw.getText().toString();

                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });

        //检测是否有位置定位的权限
        int permission = ActivityCompat.checkSelfPermission(GosChooseDeviceWorkWiFiActivity.this,
            "android.permission.ACCESS_FINE_LOCATION");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            try {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(GosChooseDeviceWorkWiFiActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView(){
        if (!TextUtils.isEmpty(workSSID)) {
            etSSID.setText(workSSID);
            etSSID.setSelection(workSSID.length());
            if (checkworkSSIDUsed(workSSID)) {
                if (!TextUtils.isEmpty(spf.getString("workSSIDPsw", ""))) {
                    etPsw.setText(spf.getString("workSSIDPsw", ""));
                }
            }
        }
    }

    @OnClick({R2.id.back,R2.id.btnNext,R2.id.rlWiFiList})
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.back){
            Intent intent = new Intent(this, GosDeviceListActivity.class);
            startActivity(intent);
        }else if (viewId == R.id.btnNext){
            workSSID = etSSID.getText().toString();
            workSSIDPsw = etPsw.getText().toString();

            if (TextUtils.isEmpty(workSSID)) {
                Toast.makeText(GosChooseDeviceWorkWiFiActivity.this, R.string.choose_wifi_list_title,
                    Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(workSSIDPsw)) {
                TipsDialog tipsDialog = new TipsDialog(this);
                tipsDialog.show();
                tipsDialog.setTitle("提示");
                tipsDialog.setTipsContent(R.string.workwifi_isnull);
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
                        Intent intent = null;
                        //wifiModuleType-false-start  wifiModuleType-true-start
                        if (GosDeploy.appConfig_WifiModuleType().size() == 0) {
                            //wifiModuleType-false-end
                            intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                                GosModeListActivity.class);
                            //wifiModuleType-false-start
                        } else {
                            //wifiModuleType-true-end
                            List<Integer> moduleTypes = GosDeploy.appConfig_WifiModuleType();
                            JSONArray array = new JSONArray();
                            for (int type : moduleTypes) {
                                if (type == 4) {
                                    type = 0;
                                } else if (type < 4) {
                                    type = type + 1;
                                } else if (type == 12) {
                                    type = 6;
                                } else if (type >= 6 && type < 12) {
                                    type = type + 1;
                                }
                                array.put(type);
                            }
                            spf.edit().putString("modulestyles", array.toString()).commit();
                            intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                                GosDeviceReadyActivity.class);
                            //wifiModuleType-true-start
                        }
                        //wifiModuleType-false-end  wifiModuleType-true-end
                        spf.edit().putString("workSSID", workSSID).commit();
                        spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();
                        intent.putExtra("isAirlink", false);
                        startActivity(intent);
                    }
                });
            } else {
                Intent intent = null;
                //wifiModuleType-false-start  wifiModuleType-true-start
                if (GosDeploy.appConfig_WifiModuleType().size() == 0) {
                    //wifiModuleType-false-end
                    intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                        GosModeListActivity.class);
                    //wifiModuleType-false-start
                } else {
                    //wifiModuleType-true-end
                    List<Integer> moduleTypes = GosDeploy.appConfig_WifiModuleType();
                    JSONArray array = new JSONArray();
                    for (int type : moduleTypes) {
                        if (type == 4) {
                            type = 0;
                        } else if (type < 4) {
                            type = type + 1;
                        } else if (type == 12) {
                            type = 6;
                        } else if (type >= 6 && type < 12) {
                            type = type + 1;
                        }
                        array.put(type);
                    }
                    spf.edit().putString("modulestyles", array.toString()).commit();
                    intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                        GosDeviceReadyActivity.class);
                    //wifiModuleType-true-start
                }
                //wifiModuleType-false-end  wifiModuleType-true-end
                spf.edit().putString("workSSID", workSSID).commit();
                spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();
                /**   判断是否是从一键配置界面传过去的  */
                intent.putExtra("isAirlink", false);
                startActivity(intent);
            }
        }else if (viewId == R.id.rlWiFiList){
            AlertDialog.Builder dia = new AlertDialog.Builder(GosChooseDeviceWorkWiFiActivity.this);
            View view = View.inflate(GosChooseDeviceWorkWiFiActivity.this, R.layout.alert_gos_wifi_list, null);
            ListView listview = (ListView) view.findViewById(R.id.wifi_list);
            List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
            List<String> localList = new ArrayList<String>();
            localList.clear();
            wifiList = new ArrayList<ScanResult>();
            wifiList.clear();

            for (ScanResult sss : rsList) {
                if (sss.SSID.contains(GosConstant.SoftAP_Start)) {

                } else {
                    if (localList.toString().contains(sss.SSID)) {
                    } else {
                        localList.add(sss.SSID);
                        wifiList.add(sss);
                    }
                }
            }
            if (wifiList.size() == 0) {
                LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                    Toast.makeText(this, getString(R.string.open), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            WifiListAdapter adapter = new WifiListAdapter(wifiList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ScanResult sResult = wifiList.get(position);
                    String sSID = sResult.SSID;
                    etSSID.setText(sSID);
                    etSSID.setSelection(sSID.length());
                    etPsw.setText("");
                    create.dismiss();
                }
            });
            dia.setView(view);
            create = dia.create();
            create.show();
        }
    }


    // 检查当前使用的WiFi是否曾经用过
    protected boolean checkworkSSIDUsed(String workSSID) {
        if (spf.contains("workSSID")) {
            if (spf.getString("workSSID", "").equals(workSSID)) {
                return true;
            }
        }
        return false;
    }

    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, GosDeviceListActivity.class);
            startActivity(intent);
            //quitAlert(this, intent);
            return true;
        }
        return false;
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

    @Override
    public void onResume() {
        super.onResume();
        try {
            // 预设workSSID && workSSIDPsw
            workSSID = NetUtils.getCurentWifiSSID(this);
            String mypass = spf.getString("mypass", "");

            if (!TextUtils.isEmpty(workSSID)) {
                etSSID.setText(workSSID);
                etSSID.setSelection(workSSID.length());
                if (!TextUtils.isEmpty(mypass)) {
                    JSONObject obj = new JSONObject(mypass);

                    if (obj.has(workSSID)) {
                        String pass = obj.getString(workSSID);
                        etPsw.setText(pass);
                    } else {
                        etPsw.setText("");
                    }
                }

            } else {
                etSSID.setText(NetUtils.getCurentWifiSSID(this));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class WifiListAdapter extends BaseAdapter {

        ArrayList<ScanResult> xpgList;

        public WifiListAdapter(ArrayList<ScanResult> list) {
            this.xpgList = list;
        }

        @Override
        public int getCount() {
            return xpgList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            if (view == null) {
                view = LayoutInflater.from(GosChooseDeviceWorkWiFiActivity.this).inflate(R.layout.item_gos_wifi_list,
                    null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            String ssid = xpgList.get(position).SSID;
            holder.getTextView().setText(ssid);

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
}
