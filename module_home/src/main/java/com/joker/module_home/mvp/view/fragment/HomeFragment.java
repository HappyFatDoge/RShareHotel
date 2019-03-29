package com.joker.module_home.mvp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.commonres.dialog.MaterialCalendarDialog;
import com.example.commonres.utils.DateTimeHelper;
import com.example.commonres.utils.DateUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerHomeComponent;
import com.joker.module_home.di.module.HomeModule;
import com.joker.module_home.mvp.contract.HomeContract;
import com.joker.module_home.mvp.presenter.HomePresenter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 主页
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R2.id.tv_city)
    TextView mCityTextView;
    @BindView(R2.id.main_search)
    EditText mSearchTextView;
    @BindView(R2.id.scan)
    ImageView mScan;
    @BindView(R2.id.banner_home)
    Banner mBanner;
    @BindView(R2.id.start_time_tv)
    TextView mStartTimeTextView;
    @BindView(R2.id.end_time_tv)
    TextView mEndTimeTextView;



    private Date mCheckInDate;
    private Date mCheckOutDate;


    private LinearLayout mLocationLinearLayout;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
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
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.mipmap.b1);
        imageList.add(R.mipmap.b2);
        imageList.add(R.mipmap.b3);
        imageList.add(R.mipmap.b5);
        imageList.add(R.mipmap.b6);

        //图片轮播
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.setViewPagerIsScroll(true);
        mBanner.setImages(imageList);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
                Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
            }
        });
        mBanner.start();

        //初始化日期
        mStartTimeTextView.setText(DateUtil.getTomorrow());
        mEndTimeTextView.setText(DateUtil.getAcquired());
    }


    @OnClick({R2.id.locatioin_ll,R2.id.main_ll_id,
        R2.id.ll_home_checkin,R2.id.ll_home_checkout,
        R2.id.ll_home_entire_rent,R2.id.ll_home_apartment,R2.id.find_tv})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.locatioin_ll)//选择城市
            mPresenter.choiceCity(this);
        else if (viewId == R.id.main_ll_id)//隐藏键盘
            hideKeyboard(view);
        else if (viewId == R.id.ll_home_checkin) //入住时间选择dialog
            showDateChoiceDialog(true);
        else if (viewId == R.id.ll_home_checkout)//退房时间选择dialog
            showDateChoiceDialog(false);
        else if (viewId == R.id.ll_home_entire_rent)//民宿
            searchHouse("House");
        else if (viewId == R.id.ll_home_apartment)//酒店公寓
            searchHouse("HotelApartment");
        else if (viewId == R.id.find_tv)//搜索
            searchHouse("All");
    }


    /**
     * 城市选择回调
     * @param result
     * @param cityName
     */
    @Override
    public void choiceCityResult(Boolean result,String cityName) {
        if (result)
            mCityTextView.setText(cityName);
    }


    /**
     * 取消城市选择回调
     * @param result
     */
    @Override
    public void cancelChoiceCity(Boolean result) {
        if (result)
            ToastUtil.makeText(getContext(),"取消选择");
    }


    /**
     * 选择日期dialog
     */
    private void showDateChoiceDialog(final boolean isCheckInDate) {
        MaterialCalendarDialog calendarDialog = MaterialCalendarDialog.getInstance(getContext(), null);
        calendarDialog.setOnOkClickLitener(new MaterialCalendarDialog.OnOkClickLitener() {
            @Override
            public void onOkClick(Date date) {
                if (isCheckInDate) {
                    mCheckInDate = date;
                    mStartTimeTextView.setText(DateTimeHelper.formatToString(date, "yyyy-MM-dd"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    mEndTimeTextView.setText(DateTimeHelper.formatToString(calendar.getTime(), "yyyy-MM-dd"));
                } else {
                    if (mCheckInDate == null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            mCheckInDate = sdf.parse(DateUtil.getTomorrow());
                            mStartTimeTextView.setText(DateTimeHelper.formatToString(mCheckInDate, "yyyy-MM-dd"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    mCheckOutDate = date;
                    if (mCheckOutDate.getTime() > mCheckInDate.getTime()) {
                        mEndTimeTextView.setText(DateTimeHelper.formatToString(date, "yyyy-MM-dd"));
                    } else {
                        Toast.makeText(getContext(), "重新选择退房日期", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });
        calendarDialog.show(getChildFragmentManager(), TAG);

    }


    /**
     * 搜索房子
     * @param searchMode
     */
    private void searchHouse(String searchMode) {
        ARouter.getInstance()
            .build(RouterHub.HOME_FINDACTIVITY)
            .withString("City",mCityTextView.getText().toString().trim())
            .withString("Search",mSearchTextView.getText().toString().trim())
            .withString("CheckInDate",mStartTimeTextView.getText().toString().trim())
            .withString("CheckOutDate",mEndTimeTextView.getText().toString().trim())
            .withString("SearchMode",searchMode)
            .navigation(getContext());
//        Intent intent = new Intent(getActivity(), FindActivity.class);
//        intent.putExtra("City", mCityTextView.getText().toString().trim());
//        intent.putExtra("Search", mSearchTextView.getText().toString().trim());
//        intent.putExtra("CheckInDate", mStartTimeTextView.getText().toString().trim());
//        intent.putExtra("CheckOutDate", mEndTimeTextView.getText().toString().trim());
//        intent.putExtra("SearchMode", searchMode);
//        startActivity(intent);
    }



    /**
     * 隐藏键盘
     */
    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
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
}
