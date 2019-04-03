package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.User;
import com.example.commonres.utils.KeyBoardUtil;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerLoginComponent;
import com.joker.module_personal.di.module.LoginModule;
import com.joker.module_personal.mvp.contract.LoginContract;
import com.joker.module_personal.mvp.presenter.LoginPresenter;

import com.joker.module_personal.R;


import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 登录
 */
@Route(path = RouterHub.PERSONAL_LOGINACTIVITY)
public class LoginActivity extends BaseActivity<LoginPresenter>
        implements LoginContract.View {

    @BindView(R2.id.login_account_et)
    EditText loginAccount;
    @BindView(R2.id.login_pwd_et)
    EditText loginPassword;
    @BindView(R2.id.login_layout)
    LinearLayout loginLayout;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
            .builder()
            .appComponent(appComponent)
            .loginModule(new LoginModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //监听软键盘回车键
        loginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.d("data","Listener");
                Log.d("data",String.valueOf(i));
                //点击软键盘完成按钮，关闭软键盘
                if (i == EditorInfo.IME_ACTION_DONE) {
                    KeyBoardUtil.hideKeyboard(loginLayout);
                    Log.d("data","done");
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R2.id.back_iv,R2.id.btn_login,
            R2.id.register_btn,R2.id.forget_password,
            R2.id.login_by_qq,R2.id.login_by_wechat})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back_iv) {//返回
            KeyBoardUtil.hideKeyboard(loginLayout);
            Intent back = new Intent();
            back.putExtra("Login", false);
            setResult(1, back);
            killMyself();
        }else if (viewId == R.id.btn_login) {//登录
            String account = loginAccount.getText().toString();
            String password = loginPassword.getText().toString();
            if (!account.equals("") && !password.equals(""))
                mPresenter.login(account, password);
            else
                ToastUtil.makeText(this,"账户/密码不能为空");
        }else if (viewId == R.id.register_btn){//注册
            Utils.navigation(this,RouterHub.PERSONAL_REGISTERACTIVITY);
            killMyself();
        }else if (viewId == R.id.forget_password){//忘记密码操作

        }else if (viewId == R.id.login_by_qq) {//qq登录

        }else if (viewId == R.id.login_by_wechat){//微信登录

        }
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent();
        back.putExtra("Login", false);
        setResult(1, back);
        super.onBackPressed();
    }

    /**
     * 登录结果
     * @param result
     * @param tips
     * @param user
     */
    @Override
    public void loginResult(Boolean result, String tips, User user) {
        ToastUtil.makeText(this, tips);
        KeyBoardUtil.hideKeyboard(loginLayout);
        if (result) {//登录成功
            LoginUtil.getInstance().setUser(user);
            String account = loginAccount.getText().toString();
            SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Account", account);
            editor.putBoolean("isLogin", true);
            editor.commit();
            Intent intent = new Intent();
            intent.putExtra("Account", account);
            intent.putExtra("Login", true);
            this.setResult(1, intent);
            this.killMyself();
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
