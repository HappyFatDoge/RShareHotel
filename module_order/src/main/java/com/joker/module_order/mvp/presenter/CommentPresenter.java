package com.joker.module_order.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Order;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_order.mvp.contract.CommentContract;
import com.joker.module_order.mvp.util.OrderLoaderUtil;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@FragmentScope
public class CommentPresenter extends BasePresenter<CommentContract.Model, CommentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CommentPresenter(CommentContract.Model model, CommentContract.View rootView) {
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
     * 获取待评论的订单
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
                        mRootView.getUnCommentOrders(true, tips, list);
                    }else
                        mRootView.getUnCommentOrders(false, "订单加载失败", null);
                }
            });
    }

}
