package com.joker.module_order.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Order;
import com.example.commonservice.BluetoothChatService;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_order.mvp.contract.HousingContract;
import com.joker.module_order.mvp.util.OrderLoaderUtil;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@FragmentScope
public class HousingPresenter extends BasePresenter<HousingContract.Model, HousingContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public HousingPresenter(HousingContract.Model model, HousingContract.View rootView) {
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
     * 获取待入住订单列表
     * @param orderState
     * @param account
     */
    public void getCheckInOrders(Integer orderState, String account){
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
                        mRootView.getCheckInOrdersResult(true, tips, list);
                    }else
                        mRootView.getCheckInOrdersResult(false, "订单加载失败", null);
                }
            });
    }


    /**
     * 发送蓝牙数据包，进行开锁——"a"，关锁——"b"
     * @param message
     */
    public void sendMessage(String message, BluetoothChatService bluetoothChatService) {
        if (bluetoothChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            mRootView.sendMessageResult(false, "蓝牙未连接,门锁操作失败");
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            bluetoothChatService.write(send);
            mRootView.sendMessageResult(true, "人脸验证成功,门锁已打开");
        }
    }
}
