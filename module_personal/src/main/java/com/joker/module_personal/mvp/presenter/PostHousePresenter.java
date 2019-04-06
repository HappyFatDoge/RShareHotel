package com.joker.module_personal.mvp.presenter;

import android.app.Application;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.commonres.beans.Hotel;
import com.example.commonres.utils.LoginUtil;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_personal.mvp.contract.PostHouseContract;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class PostHousePresenter extends BasePresenter<PostHouseContract.Model, PostHouseContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Inject
    public PostHousePresenter(PostHouseContract.Model model, PostHouseContract.View rootView) {
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
    public void choiceCity(FragmentActivity fragmentActivity) {
        CityPicker.from(fragmentActivity)
                .enableAnimation(true)
                .setAnimationStyle(com.joker.module_home.R.style.DefaultCityPickerAnimation)
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
                        LocationClient mLocationClient = new LocationClient(fragmentActivity);
                        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                            @Override
                            public void onReceiveLocation(BDLocation bdLocation) {
                                final String province = bdLocation.getProvince();
                                final String city = bdLocation.getCity();
                                String city_cut = city.substring(0, city.length() - 1);
                                String code = bdLocation.getCityCode();
                                Log.i("HomeFragment", "定位：" + province + city_cut + code);
                                CityPicker.from(fragmentActivity).locateComplete(new LocatedCity(city_cut, province, code), LocateState.SUCCESS);
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


    /**
     * 发布房子
     * @param houseName
     * @param lockAddress
     * @param city
     * @param houseAddress
     * @param houseMode
     * @param houseType
     * @param area
     * @param price
     * @param startDate
     * @param endDate
     * @param description
     * @param mPaths
     */
    public void postHouse(String houseName, String lockAddress,
                          String city, String houseAddress,
                          String houseMode, String houseType,
                          String area, String price, String startDate,
                          String endDate, String description, List<String> mPaths){
        if (!houseName.equals("") && !lockAddress.equals("")
                && !price.equals("") && !houseAddress.equals("")
                && !city.equals("请选择城市") && !area.equals("")) {
            final Hotel hotel = new Hotel();
            hotel.setName(houseName);
            hotel.setLockAddress(lockAddress);
            hotel.setCity(city);
            hotel.setAddress(houseAddress);
            //设置房子可用起始、结束日期
            try {
                BmobDate date = new BmobDate(sdf.parse(startDate));
                hotel.setStartDate(date);
                date = new BmobDate(sdf.parse(endDate));
                hotel.setEndDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String district = houseAddress.substring(0, 2);
            hotel.setMode(houseMode);
            hotel.setHouseType(houseType);
            hotel.setArea(Integer.valueOf(area));
            hotel.setPrice(Integer.valueOf(price));

            hotel.setDistrict(district);
            hotel.setComment(0);
            hotel.setGrade(0.0);
            hotel.setAvailable(1);
            if (!description.equals(""))
                hotel.setDescription(description);

            hotel.setHost(LoginUtil.getInstance().getUser());
            if (mPaths != null && mPaths.size() != 0) {
                String[] mPathArray = new String[mPaths.size()];
                mPaths.toArray(mPathArray);
                BmobFile.uploadBatch(mPathArray, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                        if (list1.size() == mPathArray.length) {
                            Log.i(TAG, "上传相册成功");

                            hotel.setUrl(list1.get(0));
                            hotel.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null)
                                        mRootView.postHouseResult(true, "发布出租成功");
                                    else {
                                        mRootView.postHouseResult(false, "发布出租失败");
                                        Log.i(TAG, "发布失败" + e.toString());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "上传相册失败");
                    }
                });
            } else {
                mRootView.postHouseResult(false, "请至少选择一张图片");
                Log.i(TAG, "相册为空");
            }

        } else
            mRootView.postHouseResult(false, "请输入完整信息");
    }
}
