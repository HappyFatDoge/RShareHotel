package com.joker.module_personal.mvp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.Hotel;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerMyHouseComponent;
import com.joker.module_personal.di.module.MyHouseModule;
import com.joker.module_personal.mvp.contract.MyHouseContract;
import com.joker.module_personal.mvp.presenter.MyHousePresenter;
import com.joker.module_personal.mvp.view.adapter.MyHouseListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 我的房子
 */
@Route(path = RouterHub.PERSONAL_MYHOUSEACTIVITY)
public class MyHouseActivity extends BaseActivity<MyHousePresenter>
    implements MyHouseContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView myHouseList;


    private MyHouseListAdapter mMyHouseListAdapter;

    private static final Integer STATUS_POST = 1;
    private static final Integer STATUS_BOOK = 2;
    private static final Integer STATUS_RENT = 3;

    private ProgressDialog mProgressDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMyHouseComponent
            .builder()
            .appComponent(appComponent)
            .myHouseModule(new MyHouseModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my_house;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mMyHouseListAdapter = new MyHouseListAdapter();
        myHouseList.setLayoutManager(new LinearLayoutManager(this));
        myHouseList.setItemAnimator(new DefaultItemAnimator());
        myHouseList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        myHouseList.setAdapter(mMyHouseListAdapter);

        mMyHouseListAdapter.setEditHotelListener(new EditHotelListener());
        mMyHouseListAdapter.setOperationListener(new OperationListener());
        showLoading();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.findMyHouse();
        mMyHouseListAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    /**
     * 加载我的房子结果
     * @param result
     * @param tips
     * @param list
     */
    @Override
    public void findMyHouseResult(Boolean result, String tips, List<Hotel> list) {
        hideLoading();
        ToastUtil.makeText(this, tips);
        mMyHouseListAdapter.setItems(list);
    }


    /**
     * 下架房子结果
     * @param result
     * @param tips
     * @param hotel
     */
    @Override
    public void removeHouseResult(Boolean result, String tips, Hotel hotel) {
        ToastUtil.makeText(this, tips);
        if (result)
            mMyHouseListAdapter.removeItem(hotel);
    }


    /**
     * 接收订单结果
     * @param result
     * @param tips
     * @param position
     */
    @Override
    public void receiveOrderResult(Boolean result, String tips, int position) {
        ToastUtil.makeText(this, tips);
        if (result)
            mMyHouseListAdapter.notifyItemChanged(position);
    }


    /**
     * 拒绝订单结果
     * @param result
     * @param tips
     * @param position
     */
    @Override
    public void rejectOrderResult(Boolean result, String tips, int position) {
        ToastUtil.makeText(this, tips);
        if (result)
            mMyHouseListAdapter.notifyItemChanged(position);
    }


    /**
     * 编辑发布中的房子
     */
    class EditHotelListener implements MyHouseListAdapter.EditHotelListener{
        @Override
        public void edit(View view, int position) {
            ARouter.getInstance()
                .build(RouterHub.PERSONAL_POSTHOUSEACTIVITY)
                .withSerializable("Hotel",mMyHouseListAdapter.getItem(position))
                .navigation(getActivity(), 1);
        }
    }


    /**
     * 房子的状态以及操作
     */
    class OperationListener implements MyHouseListAdapter.OperationListener{
        @Override
        public void operate(View view, int position) {
            Hotel hotel = mMyHouseListAdapter.getItem(position);
            if (hotel.getType() == STATUS_POST) //下架房子
                mPresenter.removeHouse(hotel);
            else if (hotel.getType() == STATUS_BOOK) { //接收/取消订单
                TipsDialog tipsDialog = new TipsDialog(getViewContext());
                tipsDialog.show();
                tipsDialog.setTitle("订单操作");
                tipsDialog.setTipsContent("接收或者取消订单");
                tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
                    @Override
                    public void onCancel() {  //取消订单
                        tipsDialog.dismiss();
                        mPresenter.rejectOrder(hotel, position);
                    }
                });
                tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {  //接收订单
                        tipsDialog.dismiss();
                        mPresenter.receiveOrder(hotel, position);
                    }
                });
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1://编辑发布的房子结果返回
                if (data.getBooleanExtra("update", false))
                    mMyHouseListAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R2.id.back)
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            killMyself();
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
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
