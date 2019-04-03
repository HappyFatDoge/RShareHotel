package com.joker.module_personal.mvp.presenter;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.commonres.beans.User;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.joker.module_personal.mvp.contract.RegisterContract;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/03/2019 11:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class RegisterPresenter extends BasePresenter<RegisterContract.Model, RegisterContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public RegisterPresenter(RegisterContract.Model model, RegisterContract.View rootView) {
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
     * 获取验证码
     * @param account
     */
    public void getVerification(String account){
        try {
            if (account != null) {
                BmobSMS.requestSMSCode(account, "verify",
                        new QueryListener<Integer>() {
                            @Override
                            public void done(Integer integer, BmobException e) {
                                if (e == null) //短信发送成功
                                    mRootView.getVerificationResult(true,null);
                                else
                                    mRootView.getVerificationResult(false,"验证码获取失败");
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 注册账户
     * @param account
     * @param name
     * @param password
     * @param code
     */
    public void createAccount(String account,String name,String password,String code){
        //验证验证码是否正确
        if (code != null) {
            BmobSMS.verifySmsCode(account, code, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {//验证通过
                        BmobQuery<User> query = new BmobQuery<>();
                        //查询Bmob中account字段叫account的数据
                        query.addWhereEqualTo("account", account);
                        query.setLimit(1);      //只需返回一个数据，因为也只有一条数据
                        //执行查询方法
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> object, BmobException e) {
                                if (e == null) {
                                    if (object.size() == 0)  {//保存注册信息，创建账号
                                        final User user = new User();
                                        user.setName(name);
                                        user.setAccount(account);
                                        user.setPassword(password);
                                        user.setFaceRegister(false);
                                        user.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null)
                                                    mRootView.createAccountResult(true,"注册成功，请进行人脸注册",user);
                                                else
                                                    mRootView.createAccountResult(false,"用户注册失败", null);
                                            }
                                        });
                                    } else {
                                        User user = object.get(0);
                                        //获得account的信息
                                        if (account == object.get(0).getAccount())
                                            mRootView.createAccountResult(false,"账户已存在", null);
                                    }

                                } else
                                    mRootView.createAccountResult(false,"服务器繁忙，用户注册失败", null);
                            }
                        });

                    } else //验证失败
                        mRootView.createAccountResult(false,"验证码错误", null);
                }
            });
        } else
            mRootView.createAccountResult(false,"请输入验证码", null);
    }
}
