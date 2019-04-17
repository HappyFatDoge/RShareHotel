package com.joker.module_personal.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Hotel;
import com.example.commonres.utils.OrderLoaderUtil;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_personal.mvp.contract.MyCleanOrderContract;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class MyCleanOrderPresenter extends BasePresenter<MyCleanOrderContract.Model, MyCleanOrderContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    private static final Integer STATUS_POST_CLEAN = 4;
    private static final Integer STATUS_ORDER_CLEAN = 5;
    private static final Integer STATUS_CLEANING = 6;

    @Inject
    public MyCleanOrderPresenter(MyCleanOrderContract.Model model, MyCleanOrderContract.View rootView) {
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
     * 获取清洁订单列表
     * @param orderState
     * @param account
     */
    public void getCleanOrders(Integer orderState, String account){
        OrderLoaderUtil.getCleanOrders(orderState, account)
                .findObjects(new FindListener<CleanOrder>() {
                    @Override
                    public void done(List<CleanOrder> list, BmobException e) {
                        String tips = "";
                        if (e == null){
                            if (list.size() == 0)
                                tips = "没有订单";
                            else
                                tips = "成功加载订单";
                            mRootView.getCleanOrdersResult(true, tips, list);
                        }else
                            mRootView.getCleanOrdersResult(false, "订单加载失败", null);
                    }
                });
    }


    /**
     * 完成清洁
     * @param cleanOrder
     */
    public void finishClean(CleanOrder cleanOrder){
        cleanOrder.setState(3);
        cleanOrder.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Hotel hotel = cleanOrder.getHotel();
                    hotel.setType(STATUS_POST_CLEAN);
                    hotel.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null)
                                mRootView.finishCleanResult(true, "完成清洁", cleanOrder);
                            else
                                mRootView.finishCleanResult(false, "完成清洁失败", cleanOrder);
                        }
                    });
                }else
                    mRootView.finishCleanResult(false, "完成清洁失败", cleanOrder);
            }
        });

    }
}
