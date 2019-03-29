package com.joker.module_home.mvp.view.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.commonres.beans.Hotel;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerMapComponent;
import com.joker.module_home.di.module.MapModule;
import com.joker.module_home.mvp.contract.MapContract;
import com.joker.module_home.mvp.presenter.MapPresenter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 主页-> 搜索 -> 选择酒店-> 酒店详情 -> 位置
 */
@Route(path = RouterHub.HOME_MAPACTIVITY)
public class MapActivity extends BaseActivity<MapPresenter> implements MapContract.View {

    @BindView(R2.id.mapview)
    MapView mMapView;

    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;

    private Geocoder mGeocoder;

    private Hotel mHotel;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMapComponent
            .builder()
            .appComponent(appComponent)
            .mapModule(new MapModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_map;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mHotel = (Hotel) getIntent().getSerializableExtra("Hotel");
        initLocation();
    }


    /**
     * 初始化位置
     */
    private void initLocation(){
        Address address = null;
        mGeocoder = new Geocoder(this);
        try {
            List<Address> list = mGeocoder.getFromLocationName(mHotel.getAddress(), 1);
            address = list.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        final LatLng newlatLng = new LatLng(address.getLatitude(), address.getLongitude());
        mLocationClient = new LocationClient(getApplicationContext());

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.red_location);
        OverlayOptions overlayOptions = new MarkerOptions().position(newlatLng).icon(bitmapDescriptor);//创建一个图层选项
        mBaiduMap.addOverlay(overlayOptions);
        MapStatus mapStatus = new MapStatus.Builder().target(newlatLng).zoom(14).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }


    @OnClick(R2.id.back)
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            finish();
    }

    private LocationClientOption InitLocOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//默认高精度定位模式
        option.setCoorType("bd0911");//默认gcj02，设置返回的定位结果坐标系
        //option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(true);
        option.setEnableSimulateGps(false);
        return option;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    protected void onDestroy() {
        if (mLocationClient != null && mLocationClient.isStarted()){
            mLocationClient.stop();
            mLocationClient = null;
        }
        mMapView.onDestroy();
        super.onDestroy();
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
