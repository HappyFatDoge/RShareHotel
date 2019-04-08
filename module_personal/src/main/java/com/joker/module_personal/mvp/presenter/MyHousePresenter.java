package com.joker.module_personal.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.OrderLoaderUtil;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_personal.mvp.contract.MyHouseContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class MyHousePresenter extends BasePresenter<MyHouseContract.Model, MyHouseContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    private static final Integer STATUS_POST = 1;
    private static final Integer STATUS_BOOK = 2;
    private static final Integer STATUS_RENT = 3;

    @Inject
    public MyHousePresenter(MyHouseContract.Model model, MyHouseContract.View rootView) {
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
     * 加载我的房子
     */
    public void findMyHouse(){
        BmobQuery<Hotel> query = new BmobQuery<>("Hotel");
        query.addWhereEqualTo("host", LoginUtil.getInstance().getUser());
        query.findObjects(new FindListener<Hotel>() {
            @Override
            public void done(List<Hotel> list, BmobException e) {
                if (e == null){
                    if (list.size() != 0)
                        mRootView.findMyHouseResult(true, "加载成功", list);
                    else
                        mRootView.findMyHouseResult(true, "尚未有发布的房子", new ArrayList<>());
                }else
                    mRootView.findMyHouseResult(false, "加载失败", new ArrayList<>());
            }
        });
    }


    /**
     * 下架房子
     * @param hotel
     */
    public void removeHouse(Hotel hotel){
        hotel.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    mRootView.removeHouseResult(true, "成功下架房子", hotel);
                else
                    mRootView.removeHouseResult(false, "下架房子失败", hotel);
            }
        });
    }


    /**
     * 确认订单
     * @param hotel
     * @param position
     */
    public void receiveOrder(Hotel hotel, int position){
        hotel.setType(STATUS_RENT);
        hotel.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    mRootView.receiveOrderResult(true, "确认订单成功", position);
                else
                    mRootView.receiveOrderResult(false, "确认订单失败", position);
            }
        });
    }


    /**
     * 拒绝订单
     * @param hotel
     * @param position
     */
    public void rejectOrder(Hotel hotel, int position){
        OrderLoaderUtil.findOrder(hotel, LoginUtil.getInstance().getUser().getAccount())
            .findObjects(new FindListener<Order>() {
                @Override
                public void done(List<Order> list, BmobException e) {
                    if (e == null && list.size() == 1){
                        Order order = list.get(0);
                        order.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    hotel.setType(STATUS_POST);
                                    hotel.setAvailable(1);
                                    hotel.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null)
                                                mRootView.rejectOrderResult(true, "拒绝订单成功", position);
                                            else
                                                mRootView.rejectOrderResult(false, "拒绝订单失败", position);
                                        }
                                    });
                                } else
                                    mRootView.rejectOrderResult(false, "拒绝订单失败", position);
                            }
                        });
                    } else
                        mRootView.rejectOrderResult(false, "拒绝订单失败", position);
                }
            });



    }
}
