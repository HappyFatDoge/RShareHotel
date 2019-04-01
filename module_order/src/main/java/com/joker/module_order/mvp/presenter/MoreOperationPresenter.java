package com.joker.module_order.mvp.presenter;

import android.app.Application;
import android.os.Message;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_order.mvp.contract.MoreOperationContract;

import java.io.PrintWriter;
import java.net.Socket;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class MoreOperationPresenter extends BasePresenter<MoreOperationContract.Model, MoreOperationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public MoreOperationPresenter(MoreOperationContract.Model model, MoreOperationContract.View rootView) {
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
     * 根据传输的信息对家具进行智能控制
     * @param type
     * @param output
     * @param failTips
     * @param successTips
     * @param mPrintWriterClient
     */
    public void sendMessageAndControl(int type, String output,
                                       String failTips, String successTips,
                                       PrintWriter mPrintWriterClient, Socket mSocketClient) {
        if (mSocketClient != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mPrintWriterClient.print(output);
                        mPrintWriterClient.flush();
                    } catch (Exception e) {
                        Message message = Message.obtain();
                        message.what = -1;
                        message.obj = failTips;
                        mRootView.sendMessageResult(false,null, message);
                        return;
                    }
                    Message message = Message.obtain();
                    message.what = type;
                    message.obj = successTips;
                    mRootView.sendMessageResult(true,null,message);
                }
            }).start();
        }else
            mRootView.sendMessageResult(false,"未连接wifi",null);
    }
}
