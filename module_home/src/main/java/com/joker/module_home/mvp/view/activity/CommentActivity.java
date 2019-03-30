package com.joker.module_home.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.Comment;
import com.example.commonres.beans.Hotel;
import com.example.commonres.dialog.ProgresDialog;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerCommentComponent;
import com.joker.module_home.di.module.CommentModule;
import com.joker.module_home.mvp.contract.CommentContract;
import com.joker.module_home.mvp.presenter.CommentPresenter;
import com.joker.module_home.mvp.view.adapter.CommentListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 主页-> 搜索 -> 选择酒店-> 酒店详情 -> 评论
 */
@Route(path = RouterHub.HOME_COMMENTACTIVITY)
public class CommentActivity
        extends BaseActivity<CommentPresenter>
        implements CommentContract.View {

    @BindView(R2.id.comment_list)
    RecyclerView commentList;

    private CommentListAdapter commentListAdapter;
    private Hotel mHotel;
    private ProgresDialog progresDialog;

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
        mHotel = (Hotel) getIntent().getSerializableExtra("Hotel");
        initView();//初始化控件

        showLoading();
        mPresenter.getCommentList(mHotel);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        commentListAdapter = new CommentListAdapter();
        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setItemAnimator(new DefaultItemAnimator());
        commentList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        commentList.setAdapter(commentListAdapter);
    }


    /**
     * 加载评论的结果
     * @param result
     * @param list
     * @param tips
     */
    @Override
    public void getCommentListResult(Boolean result, List<Comment> list, String tips) {
        ToastUtil.makeText(this,tips);
        hideLoading();
        if (result)
            commentListAdapter.setItems(list);
        else
            commentListAdapter.setItems(new ArrayList<>());
    }


    @OnClick(R2.id.back)
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            killMyself();
    }

    @Override
    public void showLoading() {
        if (progresDialog == null)
            progresDialog = new ProgresDialog(this);
        progresDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progresDialog != null)
            progresDialog.dismiss();
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

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}
