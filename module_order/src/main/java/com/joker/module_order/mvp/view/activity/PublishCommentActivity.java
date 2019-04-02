package com.joker.module_order.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerPublishCommentComponent;
import com.joker.module_order.di.module.PublishCommentModule;
import com.joker.module_order.mvp.contract.PublishCommentContract;
import com.joker.module_order.mvp.presenter.PublishCommentPresenter;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 订单Fragment -> 待评价订单Fragment -> 评价
 */
@Route(path = RouterHub.ORDER_PUBLISHCOMMENTACTIVITY)
public class PublishCommentActivity extends BaseActivity<PublishCommentPresenter>
        implements PublishCommentContract.View {

    //评论内容
    @BindView(R2.id.et_comment)
    EditText commentContentView;
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

    private Order mOrder;
    private HashMap<String, Integer> commentHasMap = new HashMap<>();

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
        mOrder = (Order) getIntent().getSerializableExtra("Order");
    }

    @OnClick({R2.id.tv_publish_comment,R2.id.back})
    public void click(View view){
        int viewId = view.getId();
        if (view.getTag() != null){
            String tag = (String) view.getTag();
            int index = Integer.parseInt(tag);
            LinearLayout layout = (LinearLayout) view.getParent();
            int layoutId = layout.getId();
            if (layoutId == R.id.environment_comment_ll){//周围环境评分
                for (int i = 0; i < environmentScore.getChildCount(); i++) {
                    ImageView imageView = (ImageView) environmentScore.getChildAt(i);
                    if (i < index) {
                        commentHasMap.put("environment", index);
                        imageView.setImageResource(R.mipmap.comment_like);
                    } else
                        imageView.setImageResource(R.mipmap.comment_no_like);
                }
            }else if (layoutId == R.id.hotel_condition_comment_ll){//房子状况评分
                for (int i = 0; i < hotelConditionScore.getChildCount(); i++) {
                    ImageView imageView = (ImageView) hotelConditionScore.getChildAt(i);
                    if (i < index) {
                        commentHasMap.put("condition", index);
                        imageView.setImageResource(R.mipmap.comment_like);
                    } else
                        imageView.setImageResource(R.mipmap.comment_no_like);
                }
            }else if (layoutId == R.id.service_comment_ll) {//服务态度评分
                for (int i = 0; i < serviceScore.getChildCount(); i++) {
                    ImageView imageView = (ImageView) serviceScore.getChildAt(i);
                    if (i < index) {
                        commentHasMap.put("service", index);
                        imageView.setImageResource(R.mipmap.comment_like);
                    } else
                        imageView.setImageResource(R.mipmap.comment_no_like);
                }
            }
        }

        if (viewId == R.id.back){//返回，取消评论
            Intent intent = new Intent();
            intent.putExtra("Comment", false);
            setResult(1, intent);
            killMyself();
        }else if (viewId == R.id.tv_publish_comment){//提交评论
            String commentContent = commentContentView.getText().toString().trim();
            if (commentContent.equals(""))
                ToastUtil.makeText(this,"评论内容不能为空");
            else {
                if (commentHasMap.get("condition") == null || commentHasMap.get("service") == null ||
                        commentHasMap.get("environment") == null)
                    ToastUtil.makeText(this,"请打评分");
                else
                    mPresenter.publishComment(mOrder,commentContent,commentHasMap);
            }
        }
    }


    /**
     * 发表评论结果
     * @param result
     * @param tips
     */
    @Override
    public void publishCommentResult(Boolean result, String tips) {
        if (result){
//            ToastUtil.makeText(this, tips);
            Intent intent = new Intent();
            intent.putExtra("Comment", true);
            setResult(1,intent);
            killMyself();
        }else
            Log.d("PublishComment", tips);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("Comment", false);
        setResult(1, intent);
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
