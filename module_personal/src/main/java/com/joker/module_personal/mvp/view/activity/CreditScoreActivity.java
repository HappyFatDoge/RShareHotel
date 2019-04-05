package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.view.AboutIntegralPopupWindow;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerCreditScoreComponent;
import com.joker.module_personal.di.module.CreditScoreModule;
import com.joker.module_personal.mvp.contract.CreditScoreContract;
import com.joker.module_personal.mvp.presenter.CreditScorePresenter;

import com.joker.module_personal.R;


import butterknife.OnClick;
import okhttp3.internal.Util;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 信用积分
 */
@Route(path = RouterHub.PERSONAL_CREDITSCOREACTIVITY)
public class CreditScoreActivity extends BaseActivity<CreditScorePresenter>
        implements CreditScoreContract.View {

    //定义手势检测器实例
    private GestureDetector detector;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCreditScoreComponent
            .builder()
            .appComponent(appComponent)
            .creditScoreModule(new CreditScoreModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_credit_score;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //创建手势检测器
        detector = new GestureDetector(this,new GestureListener());
    }


    //将该activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return detector.onTouchEvent(me);
    }


    /**
     * 手势检测器
     */
    class GestureListener implements GestureDetector.OnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        /**
         * 滑屏监测
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float minMove = 120;         //最小滑动距离
            float minVelocity = 0;      //最小滑动速度
            float beginX = e1.getX();
            float endX = e2.getX();
            float beginY = e1.getY();
            float endY = e2.getY();

            if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {   //上滑

                //显示关于积分的弹出窗口
                AboutIntegralPopupWindow aboutIntegralPopupWindow = new AboutIntegralPopupWindow(CreditScoreActivity.this);
                aboutIntegralPopupWindow.showAtLocation(findViewById(R.id.parent_linear_lay), Gravity.BOTTOM, 0, 0);          //是的弹窗在其父布局的底部，坐标为0,0开始
            }

            return false;
        }
    }


    @OnClick({R2.id.back,R2.id.right})
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.back) //返回
            killMyself();
        else if (viewId == R.id.right) //使用记录
            Utils.navigation(this, RouterHub.PERSONAL_INTEGRALRECORDACTIVITY);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
