package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.flyco.tablayout.SegmentTabLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerIntegralRecordComponent;
import com.joker.module_personal.mvp.contract.IntegralRecordContract;
import com.joker.module_personal.mvp.presenter.IntegralRecordPresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.fragment.AllIntegralRecordFragment;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/05/2019 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
/**
 * 个人中心Fragment -> 信用积分 -> 使用记录
 */
@Route(path = RouterHub.PERSONAL_INTEGRALRECORDACTIVITY)
public class IntegralRecordActivity extends BaseActivity<IntegralRecordPresenter>
        implements IntegralRecordContract.View {

    @BindView(R2.id.tab_layout)
    SegmentTabLayout tabLayout;


    private String[] tabText = {"全部", "收入", "支出"};

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerIntegralRecordComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_integral_record;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArrayList<Fragment> list = new ArrayList<Fragment>();
        list.add(AllIntegralRecordFragment.newInstance());
        list.add(AllIntegralRecordFragment.newInstance());
        list.add(AllIntegralRecordFragment.newInstance());

        tabLayout.setTabData(tabText,this,R.id.frame_layout,list);
    }

    @OnClick(R2.id.back)
    public void onClicked(){
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
