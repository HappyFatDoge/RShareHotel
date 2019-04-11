package com.joker.module_personal.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.OrderLoaderUtil;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.joker.module_personal.mvp.contract.MyCleanOrderContract;

import java.util.List;


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
}
