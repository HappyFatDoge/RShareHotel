package com.joker.module_personal.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerPersonalComponent;
import com.joker.module_personal.di.module.PersonalModule;
import com.joker.module_personal.mvp.contract.PersonalContract;
import com.joker.module_personal.mvp.presenter.PersonalPresenter;
import com.joker.module_personal.mvp.view.activity.LoginActivity;
import com.joker.module_personal.mvp.view.activity.SettingActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 个人中心Fragment
 */
public class PersonalFragment extends BaseFragment<PersonalPresenter>
    implements PersonalContract.View {

    @BindView(R2.id.user_icon_iv)
    ImageView userIcon;
    @BindView(R2.id.user_name_tv)
    TextView userName;

    private Boolean isLogin = false;

    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerPersonalComponent
            .builder()
            .appComponent(appComponent)
            .personalModule(new PersonalModule(this))
            .build()
            .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public void onStart() {
        super.onStart();
        LoginUtil loginUtil = LoginUtil.getInstance();
        if (loginUtil.isLogin()){
            isLogin= true;
//            userIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            ImageAware imageAware = new ImageViewAware(userIcon, false);
//            ImageLoader.getInstance().displayImage(loginUtil.getUser().getIcon(),imageAware, ImageUtil.createOptions());
            userIcon.setImageResource(R.mipmap.login_head);
            userName.setText(loginUtil.getUser().getName());
            userName.setClickable(false);
        }
    }


    @OnClick({R2.id.user_name_tv,R2.id.right,
        R2.id.one_key_to_check_out_linear_layout,
        R2.id.customer_service_linear_layout,R2.id.my_wallet_linear_layout,
        R2.id.my_collection_linear_layout,R2.id.coupon_linear_layout,
        R2.id.credit_score_linear_layout,R2.id.post_house_ll,
        R2.id.ll_center_myhouse,R2.id.ll_personal_comfirm_order})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.right){//设置
            //ARouter不支持在fragment调用此方式
//            ARouter.getInstance()
//                .build(RouterHub.PERSONAL_SETTINGACTIVITY)
//                .navigation(getActivity(), 2);
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivityForResult(intent, 2);
        }else if (viewId == R.id.user_name_tv){//登录
//            ARouter.getInstance()
//                .build(RouterHub.PERSONAL_LOGINACTIVITY)
//                .navigation(getActivity(), 1);
            Intent login_intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(login_intent, 1);
        }else if (viewId == R.id.one_key_to_check_out_linear_layout){//一键退房

        }else if (viewId == R.id.customer_service_linear_layout)//客服
            Utils.navigation(getContext(), RouterHub.PERSONAL_CUSTOMERSERVICEACTIVITY);
        else if (viewId == R.id.my_wallet_linear_layout) {//钱包
            if (isLogin)
                Utils.navigation(getContext(), RouterHub.PERSONAL_WALLETACTIVITY);
            else
                ToastUtil.makeText(getContext(),"请先登录");
        }
        else if (viewId == R.id.my_collection_linear_layout) {//收藏
            if (isLogin)
                Utils.navigation(getContext(), RouterHub.PERSONAL_COLLECTIONACTIVITY);
            else
                ToastUtil.makeText(getContext(),"请先登录");
        }
        else if (viewId == R.id.coupon_linear_layout) {//优惠券
            if (isLogin)
                Utils.navigation(getContext(), RouterHub.PERSONAL_COUPONACTIVITY);
            else
                ToastUtil.makeText(getContext(),"请先登录");
        }else if (viewId == R.id.credit_score_linear_layout){//信用积分
            if (isLogin)
                Utils.navigation(getContext(), RouterHub.PERSONAL_CREDITSCOREACTIVITY);
            else
                ToastUtil.makeText(getContext(), "请先登录");
        }else if (viewId == R.id.post_house_ll){//发布房子
            if (isLogin)
                Utils.navigation(getContext(), RouterHub.PERSONAL_POSTHOUSEACTIVITY);
            else
                ToastUtil.makeText(getContext(), "请先登录");
        }else if (viewId == R.id.ll_center_myhouse){//我的房子
            if (isLogin)
                Utils.navigation(getContext(), RouterHub.PERSONAL_MYHOUSEACTIVITY);
            else
                ToastUtil.makeText(getContext(), "请先登录");
        }else if (viewId == R.id.ll_personal_comfirm_order){//接单
            if (isLogin)
                Utils.navigation(getContext(), RouterHub.PERSONAL_COMFIRMORDERACTIVITY);
            else
                ToastUtil.makeText(getContext(), "请先登录");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LoginUtil loginUtil = LoginUtil.getInstance();
        switch (resultCode) {
            case 1://登录返回
                if (data.getBooleanExtra("Login", false)) {
                    String account = data.getExtras().getString("Account");
                    userName.setText(account);//设置显示用户账号
                    userName.setClickable(false);//设置账号不可点击
//            userIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            ImageAware imageAware = new ImageViewAware(userIcon, false);
//            ImageLoader.getInstance().displayImage(loginUtil.getUser().getIcon(),imageAware, ImageUtil.createOptions());
                    userIcon.setImageResource(R.mipmap.login_head);
                    loginUtil.setLogin(true);
                }
                break;
            case 2://退出当前账号
                Log.d("data",String.valueOf(data.getBooleanExtra("Logout",false)));
                if (data.getBooleanExtra("Logout", false)) {
                    Log.i(TAG, "退出登录");
                    loginUtil.setLogin(false);
                    userName.setText("登录");//恢复登录前设置
                    userName.setClickable(true);
                    userIcon.setImageResource(R.mipmap.headpic);
                }
                break;
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
    }


    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {
    }
}
