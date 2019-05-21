package com.joker.module_order.mvp.view.activity;

import android.content.Context;
import android.content.Intent;

import com.example.commonres.dialog.TipsDialog;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.jess.arms.mvp.IPresenter;

import java.util.Timer;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/5/20.
 */
public abstract class GosConfigModuleBaseActivity<P extends IPresenter>
    extends GosBaseActivity<P>{

    /**
     * 推出提示
     *
     * @param context 当前上下文
     */
    protected void quitAlert(Context context, final Intent intent) {
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.show();
        tipsDialog.setTitle("提示");
        tipsDialog.setTipsContent("确定放弃配置吗?");
        tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
            @Override
            public void onCancel() {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                tipsDialog.dismiss();
                startActivity(intent);
            }
        });
    }

    /**
     * 推出提示
     *
     * @param context 当前上下文
     */
    protected void quitAlert(Context context) {
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.show();
        tipsDialog.setTitle("提示");
        tipsDialog.setTipsContent("确定放弃配置吗?");
        tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
            @Override
            public void onCancel() {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                tipsDialog.dismiss();
                finish();
            }
        });
    }

    /**
     * 推出提示
     *
     * @param context 当前上下文
     */
    protected void quitAlert(Context context, final Intent intent, String content) {
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.show();
        tipsDialog.setTitle("提示");
        tipsDialog.setTipsContent(content);
        tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
            @Override
            public void onCancel() {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                tipsDialog.dismiss();
                GizWifiSDK.sharedInstance().stopDeviceOnboarding();
                startActivity(intent);
            }
        });
    }


    /**
     * 退出提示
     *
     * @param context 当前上下文
     * @param timer   已开启定时器
     */
    protected void quitAlert(Context context, final Timer timer) {
        TipsDialog tipsDialog = new TipsDialog(this);
        tipsDialog.show();
        tipsDialog.setTitle("提示");
        tipsDialog.setTipsContent("确定放弃配置吗?");
        tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
            @Override
            public void onCancel() {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (timer != null)
                    timer.cancel();
                tipsDialog.dismiss();
                finish();
            }
        });
    }
}
