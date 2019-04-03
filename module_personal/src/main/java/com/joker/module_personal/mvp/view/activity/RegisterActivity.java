package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.User;
import com.example.commonres.utils.ImageUtil;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerRegisterComponent;
import com.joker.module_personal.mvp.contract.RegisterContract;
import com.joker.module_personal.mvp.presenter.RegisterPresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.util.ResImagePathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;


import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.internal.Util;

import static com.jess.arms.utils.Preconditions.checkNotNull;


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

/**
 * 个人中心Fragment -> 登录 -> 新用户注册
 */
@Route(path = RouterHub.PERSONAL_REGISTERACTIVITY)
public class RegisterActivity extends BaseActivity<RegisterPresenter>
        implements RegisterContract.View {

    @BindView(R2.id.user_icon)
    ImageView userIcon;
    @BindView(R2.id.et_register_name)
    TextView userName;
    @BindView(R2.id.et_register_account)
    TextView userTel;
    @BindView(R2.id.et_register_verification)
    TextView verificationCode;
    @BindView(R2.id.et_register_password)
    TextView passwordTextView;
    @BindView(R2.id.et_register_password2)
    TextView confirmPassword;
    @BindView(R2.id.bt_register_verification)
    Button sendVerficationCode;
    @BindView(R2.id.register_btn)
    Button registerButton;

    String userIconPath;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_register;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        userIconPath = ResImagePathUtil.getPath(getResources(), R.mipmap.login_head);
    }

    /**
     * 发送验证码请求结果
     * @param result
     * @param tips
     */
    @Override
    public void getVerificationResult(Boolean result, String tips) {
        if (result){
            sendVerficationCode.setText("已发送");
            sendVerficationCode.setClickable(false);
        }else
            ToastUtil.makeText(this, tips);
    }

    /**
     * 创建账户结果
     * @param result
     * @param tips
     */
    @Override
    public void createAccountResult(Boolean result, String tips, User user) {
        ToastUtil.makeText(this, tips);
        if (result){
            LoginUtil.getInstance().setUser(user);
            LoginUtil.getInstance().setLogin(true);
            //保存登录信息
            SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Account", user.getAccount());
            editor.putBoolean("isLogin", true);
            editor.commit();
            Utils.navigation(this, RouterHub.PERSONAL_FACEREGISTERACTIVITY);
            killMyself();
        }else
            registerButton.setClickable(true);
    }

    @Override
    public Resources getViewResources() {
        return this.getResources();
    }


    @OnClick({R2.id.back,R2.id.set_user_icon_layout,
            R2.id.bt_register_verification,R2.id.register_btn})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)//返回
            killMyself();
        else if (viewId == R.id.set_user_icon_layout){//选择照片
            Intent intent = new Intent(this, PhotoSelectorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("limit", 1);//number是选择图片的数量
            startActivityForResult(intent, 0);
        }else if (viewId == R.id.bt_register_verification) {//获取验证码并验证
            ToastUtil.makeText(this, "获取验证码中...");
            mPresenter.getVerification(userTel.getText().toString().trim());
        }else if (viewId == R.id.register_btn){//注册
            String name = userName.getText().toString().trim();
            String tel = userTel.getText().toString().trim();
            String password = passwordTextView.getText().toString().trim();
            String confirmPaw = confirmPassword.getText().toString().trim();
            String code = sendVerficationCode.getText().toString().trim();
            if (!name.equals("") && !tel.equals("") &&
                    !password.equals("") && !confirmPaw.equals("")){
                if (password.equals(confirmPaw)){
                    ToastUtil.makeText(this,"账户注册中...");
                    registerButton.setClickable(false);
                    mPresenter.createAccount(tel,name,password,code,userIconPath);
                }else{
                    ToastUtil.makeText(this,"两次输入密码不一致");
                    passwordTextView.setText("");
                    confirmPassword.setText("");
                }
            }else
                ToastUtil.makeText(this, "请完善信息");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0://选择照片返回
                if (data != null){
                    List<String> path = (List<String>) data.getSerializableExtra("photos");
                    userIconPath = path.get(0);
                    userIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    Drawable drawable = Drawable.createFromPath(userIconPath);
                    userIcon.setImageDrawable(drawable);
                }
        }
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
