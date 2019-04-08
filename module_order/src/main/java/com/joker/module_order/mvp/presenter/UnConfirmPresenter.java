package com.joker.module_order.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Order;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_order.mvp.contract.UnConfirmContract;
import com.example.commonres.utils.OrderLoaderUtil;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@FragmentScope
public class UnConfirmPresenter extends BasePresenter<UnConfirmContract.Model, UnConfirmContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public UnConfirmPresenter(UnConfirmContract.Model model, UnConfirmContract.View rootView) {
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
     * 获取待付款的订单
     * @param orderState
     * @param account
     */
    public void getUnConfirmOrders(Integer orderState, String account){
        OrderLoaderUtil.getOrder(orderState, account)
                .findObjects(new FindListener<Order>() {
                    @Override
                    public void done(List<Order> list, BmobException e) {
                        String tips = "";
                        if (e == null){
                            if (list.size() == 0)
                                tips = "没有订单";
                            else
                                tips = "成功加载订单";
                            mRootView.getUnConfirmOrdersResult(true, tips, list);
                        }else
                            mRootView.getUnConfirmOrdersResult(false, "订单加载失败", null);
                    }
                });
    }


    /**
     * 取消订单
     * @param order
     */
    public void cancelOrder(Order order){
        order.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    order.getHotel().setAvailable(1);
                    order.getHotel().setType(1);
                    order.getHotel().update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null)
                                mRootView.cancelOrderResult(true, "取消成功", order);
                            else
                                mRootView.cancelOrderResult(false, "取消失败", order);
                        }
                    });
                } else
                    mRootView.cancelOrderResult(false, "加载失败：" + e.toString(),order);
            }
        });
    }
}
