package com.joker.module_home.mvp.presenter;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_home.R;
import com.joker.module_home.mvp.contract.HomeContract;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@FragmentScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 城市定位和选择
     */
    public void choiceCity(Fragment fragment) {
        CityPicker.from(fragment)
            .enableAnimation(true)
            .setAnimationStyle(R.style.DefaultCityPickerAnimation)
            .setLocatedCity(null)
            .setOnPickListener(new OnPickListener() {
                @Override
                public void onPick(int position, City city) {
                    if (city != null)
                        mRootView.choiceCityResult(true,city.getName());
                    else
                        mRootView.choiceCityResult(false,"");
                }

                @Override
                public void onLocate() {
                    //注册定位监听
                    LocationClient mLocationClient = new LocationClient(fragment.getContext());
                    mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            final String province = bdLocation.getProvince();
                            final String city = bdLocation.getCity();
                            String city_cut = city.substring(0, city.length() - 1);
                            String code = bdLocation.getCityCode();
                            Log.i("HomeFragment", "定位：" + province + city_cut + code);
                            CityPicker.from(fragment).locateComplete(new LocatedCity(city_cut, province, code), LocateState.SUCCESS);
                        }
                    });
                    LocationClientOption option = new LocationClientOption();
                    option.setIsNeedAddress(true);
                    mLocationClient.setLocOption(option);
                    mLocationClient.start();
                    Log.i(TAG, "开始定位");
                }

                @Override
                public void onCancel() {
                    mRootView.cancelChoiceCity(true);
                }
            }).show();
    }
}
