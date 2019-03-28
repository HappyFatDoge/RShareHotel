package com.joker.module_home.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.commonres.utils.ToastUtil;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerHomeComponent;
import com.joker.module_home.di.module.HomeModule;
import com.joker.module_home.mvp.contract.HomeContract;
import com.joker.module_home.mvp.presenter.HomePresenter;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R2.id.tv_city)
    TextView mCityTextView;


    private LinearLayout mLocationLinearLayout;
    //定位
    private LocationClient mLocationClient = null;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
            .builder()
            .appComponent(appComponent)
            .homeModule(new HomeModule(this))
            .build()
            .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @OnClick({R2.id.locatioin_ll,R2.id.main_ll_id,
        R2.id.ll_home_checkin,R2.id.ll_home_checkout,
        R2.id.ll_home_entire_rent,R2.id.ll_home_apartment,R2.id.find_tv})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.locatioin_ll)
            choiceCity();
    }


    /**
     * 城市定位和选择
     */
    private void choiceCity() {
        CityPicker.from(this)
            .enableAnimation(true)
            .setAnimationStyle(R.style.DefaultCityPickerAnimation)
            .setLocatedCity(null)
            .setOnPickListener(new OnPickListener() {
                @Override
                public void onPick(int position, City city) {
                    if (city != null) {
                        mCityTextView.setText(city.getName());
                    }
                }

                @Override
                public void onLocate() {
                    //注册定位监听
                    mLocationClient = new LocationClient(getContext());
                    mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            final String province = bdLocation.getProvince();
                            final String city = bdLocation.getCity();
                            String city_cut = city.substring(0, city.length() - 1);
                            String code = bdLocation.getCityCode();
                            Log.i("HomeFragment", "定位：" + province + city_cut + code);
                            CityPicker.from(getFragment()).locateComplete(new LocatedCity(city_cut, province, code), LocateState.SUCCESS);
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
                    ToastUtil.makeText(getContext(),"取消选择");
                }
            }).show();
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

    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
