package com.joker.module_home.mvp.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerHotelSelectedInfoComponent;
import com.joker.module_home.di.module.HotelSelectedInfoModule;
import com.joker.module_home.mvp.contract.HotelSelectedInfoContract;
import com.joker.module_home.mvp.presenter.HotelSelectedInfoPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 主页-.搜索->选择酒店->酒店详情
 */
@Route(path = RouterHub.HOME_HOTELSELECTEDINFOACTIVITY)
public class HotelSelectedInfoActivity
    extends BaseActivity<HotelSelectedInfoPresenter>
    implements HotelSelectedInfoContract.View {


    @BindView(R2.id.tv_hotel_name)
    TextView hotelName;
    @BindView(R2.id.tv_hotel_commnent)
    TextView hotelComment;
    @BindView(R2.id.tv_hotel_grade)
    TextView hotelGrade;
    @BindView(R2.id.tv_hotel_price)
    TextView hotelPrice;
    @BindView(R2.id.tv_detail_mode)
    TextView detailMode;
    @BindView(R2.id.tv_detail_house_type)
    TextView detailHouseType;
    @BindView(R2.id.tv_detail_area)
    TextView detailArea;
    @BindView(R2.id.tv_detail_address)
    TextView detailAddress;
    @BindView(R2.id.tv_detail_description)
    TextView detailDescription;
    @BindView(R2.id.tv_detail_start_date)
    TextView detailStartDate;
    @BindView(R2.id.tv_detail_end_date)
    TextView detailEndDate;
    @BindView(R2.id.btn_detail_colection)
    Button mCollectButton;

    //数据
    private String mCheckInDate;
    private String mCheckOutDate;
    private Hotel mHotel;

    private boolean isCollect;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Integer mDays;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHotelSelectedInfoComponent
            .builder()
            .appComponent(appComponent)
            .hotelSelectedInfoModule(new HotelSelectedInfoModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_hotel_selected_info;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        mHotel = (Hotel) intent.getSerializableExtra("HotelBundle");
        mCheckInDate = intent.getStringExtra("CheckInDate");
        mCheckOutDate = intent.getStringExtra("CheckOutDate");

        //天数
        try {
            mDays = getCountDays(sdf.parse(mCheckInDate), sdf.parse(mCheckOutDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        initView();//初始化控件
    }


    private void initView(){
        hotelName.setText(mHotel.getName());
        hotelComment.setText(mHotel.getComment().toString());
        hotelGrade.setText(mHotel.getGrade().toString());
        hotelPrice.setText(mHotel.getPrice().toString());
        detailMode.setText(mHotel.getMode());
        detailHouseType.setText(mHotel.getHouseType());
        detailArea.setText(mHotel.getArea().toString());
        detailAddress.setText(mHotel.getAddress());
        if (mHotel.getDescription() == null)
            detailDescription.setText("暂无数据");
        else
            detailDescription.setText(mHotel.getDescription());
        detailStartDate.setText(mHotel.getStartDate().getDate());
        detailEndDate.setText(mHotel.getEndDate().getDate());

        mPresenter.isCollect(mHotel,LoginUtil.getInstance().getUser());
    }

    @OnClick({R2.id.back,R2.id.hotelmessage_btn_phone,
        R2.id.hotelmessage_btn_evaluation,R2.id.bt_hotel_book,
        R2.id.btn_detail_colection,R2.id.btn_detail_location})
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)//返回
            killMyself();
        else if (viewId == R.id.hotelmessage_btn_phone) {//打电话
            TipsDialog tipsDialog = new TipsDialog(this);
            tipsDialog.show();
            tipsDialog.setTitle("联系");
            tipsDialog.setTipsContent("拨打房主电话？");
            tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    tipsDialog.dismiss();
                    callPhone();
                }
            });
            tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
                @Override
                public void onCancel() {
                    tipsDialog.dismiss();
                }
            });
        }else if (viewId == R.id.hotelmessage_btn_evaluation){//查看评价
            ARouter.getInstance()
                .build(RouterHub.HOME_COMMENTACTIVITY)
                .withSerializable("Hotel", mHotel)
                .navigation(this);
        }else if (viewId == R.id.btn_detail_colection){
            //收藏与取消收藏
            if (!LoginUtil.getInstance().isLogin()) {
                ToastUtil.makeText(this, "请先登录");
            } else
                mPresenter.setCollect(mHotel,LoginUtil.getInstance().getUser(),isCollect);
        }else if (viewId == R.id.btn_detail_location){
            //地图
            ARouter.getInstance()
                .build(RouterHub.HOME_MAPACTIVITY)
                .withSerializable("Hotel", mHotel)
                .navigation(this);
        }else if (viewId == R.id.bt_hotel_book){//创建订单
            if (LoginUtil.getInstance().isLogin())
                mPresenter.createOrder(LoginUtil.getInstance().getUser(),mHotel,mCheckInDate,mCheckOutDate,mDays);
             else
                ToastUtil.makeText(this, "请先登录");
        }
    }


    /**
     * 获取该hotel是否被收藏的结果
     * @param result
     */
    @Override
    public void isCollectResult(Boolean result) {
        if (result){
            Drawable star = getResources().getDrawable(R.mipmap.star_choice);
            star.setBounds(0, 0, star.getMinimumWidth(), star.getMinimumHeight());
            mCollectButton.setCompoundDrawables(null, star, null, null);
            isCollect = true;
        }else{
            Drawable star = getResources().getDrawable(R.mipmap.hotelmessage_collection);
            star.setBounds(0, 0, star.getMinimumWidth(), star.getMinimumHeight());
            mCollectButton.setCompoundDrawables(null, star, null, null);
            isCollect = false;
        }
    }


    /**
     * 设置收藏或者取消收藏结果
     * @param tips
     */
    @Override
    public void setCollectResult(Boolean result, Boolean isCollect, String tips) {
        if (result)
            isCollectResult(isCollect);
        ToastUtil.makeText(this,tips);
    }


    /**
     * 预定hotel结果
     * @param result
     * @param tips
     * @param order
     */
    @Override
    public void orderResult(Boolean result, String tips, Order order) {
        ToastUtil.makeText(this, tips);
        if (result){
            ARouter.getInstance()
                .build(RouterHub.HOME_ORDERDETAILACTIVITY)
                .withSerializable("Order", order)
                .navigation(this);
            killMyself();
        }
    }


    /**
     * 打电话
     */
    private void callPhone(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mHotel.getHost().getAccount()));
            this.startActivity(callIntent);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mHotel.getHost().getAccount()));
            this.startActivity(callIntent);
        }
    }


    /**
     * 计算预定天数
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer getCountDays(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
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
