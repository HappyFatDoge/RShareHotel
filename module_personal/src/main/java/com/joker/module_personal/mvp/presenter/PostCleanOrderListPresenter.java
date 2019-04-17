package com.joker.module_personal.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.User;
import com.example.commonres.utils.LoginUtil;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_personal.mvp.contract.PostCleanOrderListContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class PostCleanOrderListPresenter extends BasePresenter<PostCleanOrderListContract.Model, PostCleanOrderListContract.View> {
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
    public PostCleanOrderListPresenter(PostCleanOrderListContract.Model model, PostCleanOrderListContract.View rootView) {
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
     * 加载出租的房子
     */
    public void findRentHouse(){
        BmobQuery<Hotel> query = new BmobQuery<>("Hotel");
        query.addWhereNotEqualTo("host", LoginUtil.getInstance().getUser());
        query.addWhereEqualTo("type", 4);
        query.findObjects(new FindListener<Hotel>() {
            @Override
            public void done(List<Hotel> list, BmobException e) {
                if (e == null){
                    if (list.size() != 0)
                        mRootView.findRentHouseResult(true, "加载成功", list);
                    else
                        mRootView.findRentHouseResult(true, "尚未有发布的房子", new ArrayList<>());
                }else
                    mRootView.findRentHouseResult(false, "加载失败", new ArrayList<>());
            }
        });
    }


    /**
     * 接单
     * @param user
     * @param hotel
     * @param days
     */
    public void receiveOrder(User user, Hotel hotel,Integer days){
        CleanOrder order = new CleanOrder();
        order.setUser(user);
        order.setHotel(hotel);
        order.setDays(days);
        order.setHost(hotel.getHost());
        order.setPrice(hotel.getPrice() * days);
        order.setState(1);
        order.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
//                    order.getHotel().setType(STATUS_ORDER_CLEAN);
                    order.getHotel().setType(STATUS_CLEANING);
                    order.getHotel().update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null)
                                mRootView.orderResult(true,"接单成功", hotel);
                            else
                                mRootView.orderResult(false,"接单失败" + e.toString(), null);

                        }
                    });
                } else
                    mRootView.orderResult(false,"接单失败" + e.toString(), null);

            }
        });
    }
}
