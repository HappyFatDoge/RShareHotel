package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerAboutUsComponent;
import com.joker.module_personal.di.module.AboutUsModule;
import com.joker.module_personal.mvp.contract.AboutUsContract;
import com.joker.module_personal.mvp.presenter.AboutUsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 设置 -> 关于共享租房
 */
@Route(path = RouterHub.PERSONAL_ABOUTUSACTIVITY)
public class AboutUsActivity extends BaseActivity<AboutUsPresenter> implements AboutUsContract.View {

    @BindView(R2.id.piechart_sum)
    PieChart mPieChart;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAboutUsComponent
            .builder()
            .appComponent(appComponent)
            .aboutUsModule(new AboutUsModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_about_us;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //getOrderCount();//获取进三个月成交量，问题所在用户可以删除订单

        //统计图
        Description des = new Description();
        des.setText("近3个月成交量占比");
        mPieChart.setDescription(des);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, "四月"));
        entries.add(new PieEntry(26.7f, "五月"));
        entries.add(new PieEntry(24.0f, "六月"));
        PieDataSet set = new PieDataSet(entries, "月份");
        PieData data = new PieData(set);
        mPieChart.setData(data);
        mPieChart.invalidate();
    }

    @OnClick(R2.id.back)
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            killMyself();
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
