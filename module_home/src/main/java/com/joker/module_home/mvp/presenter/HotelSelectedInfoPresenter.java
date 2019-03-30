package com.joker.module_home.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Collection;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.beans.User;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_home.mvp.contract.HotelSelectedInfoContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class HotelSelectedInfoPresenter extends BasePresenter<HotelSelectedInfoContract.Model, HotelSelectedInfoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Inject
    public HotelSelectedInfoPresenter(HotelSelectedInfoContract.Model model, HotelSelectedInfoContract.View rootView) {
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
     * 设置为收藏或者取消收藏
     * @param hotel
     * @param user
     * @param isCollect
     */
    public void setCollect(Hotel hotel, User user, boolean isCollect){
        if (!isCollect) {
            //收藏
            Collection collection = new Collection(user, hotel);
            collection.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null)
                        mRootView.setCollectResult(true,true,"已添加收藏");
                    else
                        mRootView.setCollectResult(false,false,"收藏失败");
                }
            });
        }else{
            //取消收藏
            BmobQuery<Collection> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("hotel",hotel);
            bmobQuery.addWhereEqualTo("user",user);
            bmobQuery.setLimit(1);
            bmobQuery.findObjects(new FindListener<Collection>() {
                @Override
                public void done(List<Collection> list, BmobException e) {
                    if (e == null)
                        list.get(0).delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null)
                                    mRootView.setCollectResult(true,false,"已取消收藏");
                                else
                                    mRootView.setCollectResult(false,true,"取消收藏失败");
                            }
                        });
                    else
                        mRootView.setCollectResult(false,false,"尚未收藏，数据错误");
                }
            });
        }
    }


    /**
     * 预定hotel
     * @param user
     * @param hotel
     * @param mCheckInDate
     * @param mCheckOutDate
     */
    public void createOrder(User user, Hotel hotel,String mCheckInDate,String mCheckOutDate,Integer days){
        Order order = new Order();
        order.setUser(user);
        order.setHotel(hotel);
        order.setDays(days);
        order.setHost(hotel.getHost());
        order.setPrice(hotel.getPrice() * days);
        order.setState(1);
        try {
            BmobDate bmobDateIn = new BmobDate(sdf.parse(mCheckInDate));
            order.setCheckInTime(bmobDateIn);
            BmobDate bmobDateOut = new BmobDate(sdf.parse(mCheckOutDate));
            order.setCheckOutTime(bmobDateOut);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        order.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    order.getHotel().setAvailable(0);
                    order.getHotel().update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null)
                                mRootView.orderResult(true,"预定成功", order);
                            else
                                mRootView.orderResult(false,"预定失败" + e.toString(), null);

                        }
                    });
                } else
                    mRootView.orderResult(false,"预定失败" + e.toString(), null);

            }
        });
    }


    /**
     * 获取并判断该hotel是否被该user收藏
     * @param hotel
     * @param user
     */
    public void isCollect(Hotel hotel, User user){
        BmobQuery<Collection> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("hotel", hotel);
        bmobQuery.addWhereEqualTo("user", user);
        bmobQuery.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null && list.size() == 1)
                    mRootView.isCollectResult(true);
                else
                    mRootView.isCollectResult(false);
            }
        });
    }

}
