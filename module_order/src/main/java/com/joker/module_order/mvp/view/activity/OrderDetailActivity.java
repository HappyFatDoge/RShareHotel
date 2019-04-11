package com.joker.module_order.mvp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.beans.User;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.ImageUtil;
import com.example.commonres.utils.TelUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerOrderDetailComponent;
import com.joker.module_order.mvp.contract.OrderDetailContract;
import com.joker.module_order.mvp.presenter.OrderDetailPresenter;

import com.joker.module_order.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;


import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 16:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
/**
 * 订单Fragment -> 待付款订单Fragment -> 付款，订单详情页面
 */
@Route(path = RouterHub.ORDER_ORDERDETAILACTIVITY)
public class OrderDetailActivity extends BaseActivity<OrderDetailPresenter>
        implements OrderDetailContract.View {


    @BindView(R2.id.iv_find_hotel)
    ImageView hotelIcon;
    @BindView(R2.id.tv_order_detail_name)
    TextView hotelName;
    @BindView(R2.id.tv_detail_mode)
    TextView hotelMode;
    @BindView(R2.id.tv_detail_housetype)
    TextView hotelType;
    @BindView(R2.id.tv_detail_area)
    TextView hotelArea;
    @BindView(R2.id.tv_order_detail_checkindate)
    TextView checkInDate;
    @BindView(R2.id.tv_order_detail_checkoutdate)
    TextView checkOutDate;
    @BindView(R2.id.tv_detail_username)
    TextView userName;
    @BindView(R2.id.tv_detail_tel)
    TextView userTel;
    @BindView(R2.id.tv_order_detail_ordernum)
    TextView orderNum;
    @BindView(R2.id.tv_order_detail_createdtime)
    TextView orderCreatedTime;
    @BindView(R2.id.tv_order_detail_price)
    TextView hotelPrice;
    @BindView(R2.id.tv_order_detail_all_money)
    TextView totalPrice;

    private Order order;
    private Hotel hotel;
    private User owner;//住宅所有者
    private User payer;//预定用户
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOrderDetailComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        order = (Order) getIntent().getSerializableExtra("Order");
        hotel = order.getHotel();
        owner = hotel.getHost();
        payer = order.getUser();

        initView();//初始化控件
    }


    /**
     * 初始化控件
     */
    private void initView(){
        //设置hotel图片
        hotelIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageAware imageAware = new ImageViewAware(hotelIcon,false);
        ImageLoader.getInstance().displayImage(hotel.getUrl(),imageAware, ImageUtil.createOptions());
        //设置hotel名字
        hotelName.setText(hotel.getName());
        //出租方式
        hotelMode.setText(hotel.getMode());
        //户型
        hotelType.setText(hotel.getHouseType());
        //面积
        hotelArea.setText(hotel.getArea().toString());
        //入住时间
        checkInDate.setText(order.getCheckInTime().getDate());
        //退房时间
        checkOutDate.setText(order.getCheckOutTime().getDate());
        //订房用户名字
        userName.setText(payer.getName());
        //订房用户电话
        userTel.setText(payer.getAccount());
        //订单号
        orderNum.setText(order.getObjectId());
        //订单创建时间
        orderCreatedTime.setText(order.getCreatedAt());
        //房费
        hotelPrice.setText(hotel.getPrice().toString());
        //总金额
        totalPrice.setText(order.getPrice().toString());
    }


    @OnClick({R2.id.back,R2.id.bt_order_detail_pay,
            R2.id.chat_with_owner,R2.id.connect_owner})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)//返回
            quitOrderDetail();
        else if (viewId == R.id.bt_order_detail_pay)//付款
            payOrder();
        else if (viewId == R.id.chat_with_owner)//与住宅所有者进行聊天，后期可做
            ToastUtil.makeText(this, "与" + owner.getAccount() + "聊天");
        else if (viewId == R.id.connect_owner) {//与住宅所有者通话
            TipsDialog tipsDialog = new TipsDialog(this);
            tipsDialog.show();
            tipsDialog.setTitle("联系");
            tipsDialog.setTipsContent("拨打房主电话？");
            tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    tipsDialog.dismiss();
                    TelUtil.callPhone(owner.getAccount(), getActivity());
                }
            });
            tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
                @Override
                public void onCancel() {
                    tipsDialog.dismiss();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        quitOrderDetail();
    }

    /**
     * 退出订单详情界面，提示是否进行付款再退出
     */
    private void quitOrderDetail(){
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.show();
        tipsDialog.setTitle("提示");
        tipsDialog.setTipsContent("订单尚未付款，确定退出？");
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
                killMyself();
            }
        });
    }

    /**
     * 订单付款
     */
    private void payOrder(){
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.show();
        tipsDialog.setTitle("确认付款");
        tipsDialog.setTipsContent("￥" + order.getPrice().toString());
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
                //付款
                mPresenter.payOrder(order, hotel);
            }
        });
    }


    /**
     * 付款结果
     * @param result
     * @param tips
     */
    @Override
    public void payOrderResult(Boolean result, String tips) {
        ToastUtil.makeText(this, tips);
        if (result){
            Utils.navigation(this, RouterHub.HOME_PAYSUCCESSACTIVITY);
            killMyself();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
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
