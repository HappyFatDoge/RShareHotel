package com.joker.module_order.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerPublishCommentComponent;
import com.joker.module_order.di.module.PublishCommentModule;
import com.joker.module_order.mvp.contract.PublishCommentContract;
import com.joker.module_order.mvp.presenter.PublishCommentPresenter;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PublishCommentActivity extends BaseActivity<PublishCommentPresenter> implements PublishCommentContract.View {

    //评论内容
    @BindView(R2.id.et_comment)
    EditText commentContent;
    //环境评分
    @BindView(R2.id.environment_comment_ll)
    LinearLayout environmentScore;
    //房子评分
    @BindView(R2.id.hotel_condition_comment_ll)
    LinearLayout hotelConditionScore;
    //服务态度评分
    @BindView(R2.id.service_comment_ll)
    LinearLayout serviceScore;
    //提交按钮
    @BindView(R2.id.tv_publish_comment)
    Button publishComment;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPublishCommentComponent
            .builder()
            .appComponent(appComponent)
            .publishCommentModule(new PublishCommentModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_publish_comment;
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
