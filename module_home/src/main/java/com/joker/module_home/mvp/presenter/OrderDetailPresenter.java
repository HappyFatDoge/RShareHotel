package com.joker.module_home.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_home.mvp.contract.OrderDetailContract;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class OrderDetailPresenter extends BasePresenter<OrderDetailContract.Model, OrderDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public OrderDetailPresenter(OrderDetailContract.Model model, OrderDetailContract.View rootView) {
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
     * 进行订单付款操作
     * @param order
     * @param hotel
     */
    public void payOrder(Order order, Hotel hotel){
        order.setState(2);
        order.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){//设置hotel已经可以放出，后期需要进行更改
                    hotel.setAvailable(0);
                    hotel.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null)
                                mRootView.payOrderResult(true, "付款成功");
                            else
                                mRootView.payOrderResult(false, "付款失败");
                        }
                    });
                }else
                    mRootView.payOrderResult(false,"付款失败");
            }
        });
    }

}
