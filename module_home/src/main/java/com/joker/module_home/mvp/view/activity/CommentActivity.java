package com.joker.module_home.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_home.di.component.DaggerCommentComponent;
import com.joker.module_home.di.module.CommentModule;
import com.joker.module_home.mvp.contract.CommentContract;
import com.joker.module_home.mvp.presenter.CommentPresenter;

import com.joker.module_home.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 主页-> 搜索 -> 选择酒店-> 酒店详情 -> 评论
 */
@Route(path = RouterHub.HOME_COMMENTACTIVITY)
public class CommentActivity extends BaseActivity<CommentPresenter> implements CommentContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommentComponent
            .builder()
            .appComponent(appComponent)
            .commentModule(new CommentModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_comment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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
