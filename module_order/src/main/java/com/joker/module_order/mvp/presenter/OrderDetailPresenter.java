package com.joker.module_order.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.joker.module_order.mvp.contract.OrderDetailContract;


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
