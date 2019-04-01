package com.joker.module_home.mvp.presenter;

import android.app.Application;
import android.util.Log;

import com.example.commonres.beans.Comment;
import com.example.commonres.beans.Hotel;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_home.mvp.contract.CommentContract;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class CommentPresenter extends BasePresenter<CommentContract.Model, CommentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CommentPresenter(CommentContract.Model model, CommentContract.View rootView) {
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
     * 获取评论
     * @param hotel
     */
    public void getCommentList(Hotel hotel){
        BmobQuery<Comment> query = new BmobQuery<>("Comment");
        query.setLimit(30);
        query.addWhereEqualTo("hotel", hotel);
        query.include("user");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null)
                    mRootView.getCommentListResult(true,list,"加载评论成功");
                else
                    mRootView.getCommentListResult(false,null,"加载评论失败");
            }
        });
    }
}
