package com.joker.module_order.mvp.presenter;

import android.app.Application;

import com.example.commonres.beans.Comment;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_order.mvp.contract.PublishCommentContract;

import java.text.DecimalFormat;
import java.util.HashMap;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class PublishCommentPresenter extends BasePresenter<PublishCommentContract.Model, PublishCommentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public PublishCommentPresenter(PublishCommentContract.Model model, PublishCommentContract.View rootView) {
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
     * 发布评论
     * @param mOrder
     * @param commentContent
     * @param commentHasMap
     */
    public void publishComment(Order mOrder, String commentContent,
                               HashMap<String,Integer> commentHasMap){
        Comment comment = new Comment();
        comment.setOrder(mOrder);
        comment.setHotel(mOrder.getHotel());
        comment.setUser(mOrder.getUser());
        comment.setContent(commentContent);
        final Double grade = (commentHasMap.get("environment") + commentHasMap.get("condition") + commentHasMap.get("service")) / 3.0;
        comment.setGrade(grade);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mOrder.setState(5);
                    mOrder.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Hotel hotel = new Hotel();
                                Hotel oldHotel = mOrder.getHotel();
                                hotel.setObjectId(oldHotel.getObjectId());
                                hotel.increment("comment");
                                Double hotelGrade = (oldHotel.getComment() * oldHotel.getGrade() + grade) / (oldHotel.getComment() + 1);
                                DecimalFormat df = new DecimalFormat("0.0");
                                hotel.setGrade(Double.parseDouble(df.format(hotelGrade)));
                                hotel.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null)
                                            mRootView.publishCommentResult(true,"评论成功");
                                        else
                                            mRootView.publishCommentResult(false,"评论增加失败" + e.toString());
                                    }
                                });
                            } else
                                mRootView.publishCommentResult(false,"发表评论失败" + e.toString());
                        }
                    });
                } else
                    mRootView.publishCommentResult(false,"发表评论失败" + e.toString());
            }
        });
    }
}
