package com.joker.module_order.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Order;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_order.mvp.contract.HistoryOrderContract;
import com.joker.module_order.mvp.util.OrderLoaderUtil;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@FragmentScope
public class HistoryOrderPresenter extends BasePresenter<HistoryOrderContract.Model, HistoryOrderContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public HistoryOrderPresenter(HistoryOrderContract.Model model, HistoryOrderContract.View rootView) {
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
     * 获取历史订单列表
     * @param orderState
     * @param account
     */
    public void getHistoryOrderList(Integer orderState, String account){
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
                        mRootView.getHistoryOrderListResult(true, tips, list);
                    }else
                        mRootView.getHistoryOrderListResult(false, "订单加载失败", null);
                }
            });
    }

    /**
     * 删除历史订单
     * @param order
     */
    public void deleteOrder(Order order){
        order.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    mRootView.deleteOrderResult(true, "成功删除订单", order);
                else
                    mRootView.deleteOrderResult(false, "删除订单失败", order);
            }
        });
    }
}
