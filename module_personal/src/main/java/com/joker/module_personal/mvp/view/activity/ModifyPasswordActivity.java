package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerModifyPasswordComponent;
import com.joker.module_personal.di.module.ModifyPasswordModule;
import com.joker.module_personal.mvp.contract.ModifyPasswordContract;
import com.joker.module_personal.mvp.presenter.ModifyPasswordPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment -> 设置 -> 修改密码
 */
@Route(path = RouterHub.PERSONAL_MODIFYPSSSWORDACTIVITY)
public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter>
    implements ModifyPasswordContract.View {


    @BindView(R2.id.et_modify_pw)
    EditText mExPasswordEditText;
    @BindView(R2.id.et_modify_password)
    EditText mPasswordEditText1;
    @BindView(R2.id.et_modify_password2)
    EditText mPasswordEditText2;
    @BindView(R2.id.btn_modify)
    Button mModifyButton;

    private ProgressDialog mProgressDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerModifyPasswordComponent
            .builder()
            .appComponent(appComponent)
            .modifyPasswordModule(new ModifyPasswordModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_modify_password;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }



    @OnClick({R2.id.back,R2.id.btn_modify})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back)
            killMyself();
        else if (viewId == R.id.btn_modify) {
            showLoading();
            String orignPaw = mExPasswordEditText.getText().toString();
            String newPaw = mPasswordEditText1.getText().toString();
            String confirmPaw = mPasswordEditText2.getText().toString();
            mPresenter.modifyPassword(orignPaw, newPaw, confirmPaw);
        }
    }


    /**
     * 修改密码结果
     * @param result
     * @param tips
     */
    @Override
    public void modifyPasswordResult(Boolean result, String tips) {
        hideLoading();
        ToastUtil.makeText(this, tips);
        if (result){
            SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
            editor.putBoolean("isLogin", false);
            editor.commit();
            Intent back = new Intent();
            back.putExtra("modify",true);
            setResult(1,back);
            killMyself();
        }else{
            mExPasswordEditText.setText("");
            mPasswordEditText1.setText("");
            mPasswordEditText2.setText("");
        }
    }


    @Override
    public void showLoading() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
        mProgressDialog.setText("请稍等...");
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
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
