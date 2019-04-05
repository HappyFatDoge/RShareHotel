package com.joker.module_personal.mvp.view.activity;

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
import com.example.commonres.beans.Collection;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.DateUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerCollectionComponent;
import com.joker.module_personal.di.module.CollectionModule;
import com.joker.module_personal.mvp.contract.CollectionContract;
import com.joker.module_personal.mvp.presenter.CollectionPresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.adapter.CollectionListAdapter;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 收藏
 */
@Route(path = RouterHub.PERSONAL_COLLECTIONACTIVITY)
public class CollectionActivity extends BaseActivity<CollectionPresenter>
        implements CollectionContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView collectionList;

    private CollectionListAdapter collectionListAdapter;
    private ProgressDialog progressDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCollectionComponent
            .builder()
            .appComponent(appComponent)
            .collectionModule(new CollectionModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_collection;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        collectionListAdapter = new CollectionListAdapter();
        collectionList.setLayoutManager(new LinearLayoutManager(this));
        collectionList.setItemAnimator(new DefaultItemAnimator());
        collectionList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        collectionList.setAdapter(collectionListAdapter);

        collectionListAdapter.setCancelCollectionListener(new CancelCollectionListener());
        collectionListAdapter.setOrderListener(new BookHotelListener());

        showLoading();
        //获取收藏的酒店公寓/民宿
        mPresenter.getCollections();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    /**
     * 取消收藏结果
     * @param result
     * @param tips
     * @param collection
     */
    @Override
    public void cancelCollectionResult(Boolean result, String tips, Collection collection) {
        ToastUtil.makeText(this, tips);
        if (result)
            collectionListAdapter.removeItem(collection);
    }

    /**
     * 加载收藏列表结果
     * @param result
     * @param tips
     * @param list
     */
    @Override
    public void getCollectionsResult(Boolean result, String tips, List<Collection> list) {
        ToastUtil.makeText(this, tips);
        collectionListAdapter.setItems(list);
    }

    /**
     * 取消收藏
     */
    class CancelCollectionListener implements CollectionListAdapter.CancelCollectionListener{
        @Override
        public void cancelCollection(View view, int position) {
            TipsDialog tipsDialog = new TipsDialog(getViewContext());
            tipsDialog.show();
            tipsDialog.setTitle("提示");
            tipsDialog.setTipsContent("取消收藏？");
            tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    tipsDialog.dismiss();
                    //取消收藏
                    mPresenter.cancelCollection(collectionListAdapter.getItem(position));
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

    /**
     * 预定按钮监听器
     */
    class BookHotelListener implements CollectionListAdapter.OrderListener{
        @Override
        public void order(View view, int position) {
            ARouter.getInstance()
                    .build(RouterHub.HOME_HOTELSELECTEDINFOACTIVITY)
                    .withSerializable("HotelBundle",collectionListAdapter.getItem(position).getHotel())
                    .withString("CheckInDate", DateUtil.getTomorrow())
                    .withString("CheckOutDate", DateUtil.getAcquired())
                    .navigation(getViewContext());
        }
    }

    @OnClick(R2.id.back)
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            killMyself();
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
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
