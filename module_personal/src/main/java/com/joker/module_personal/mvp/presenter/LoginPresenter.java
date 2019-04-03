package com.joker.module_personal.mvp.presenter;

import android.app.Application;
import android.util.Log;

import com.example.commonres.beans.User;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.joker.module_personal.mvp.contract.LoginContract;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
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
     * 登录
     * @param account
     * @param password
     */
    public void login(String account, String password){
        //获取密码进行比对
        BmobQuery<User> query = new BmobQuery<>();
        //查询Bmob中username字段叫account的数据
        query.addWhereEqualTo("account", account);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(1);
        //执行查询方法
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) //没有创建账号
                        mRootView.loginResult(false, "账号不存在", null);
                    else {
                        if (list.get(0).getAccount().equals(account)) {
                            Log.i("LoginActivity", "账号" + account);
                            if (list.get(0).getPassword().equals(password))
                                mRootView.loginResult(true,"登录成功", list.get(0));
                            else
                                mRootView.loginResult(false, "密码错误", null);
                        }
                    }
                } else
                    Log.i("LoginActivity", e.toString());
            }
        });
    }
}
