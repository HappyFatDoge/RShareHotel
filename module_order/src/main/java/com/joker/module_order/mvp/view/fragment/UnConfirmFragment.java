package com.joker.module_order.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.Order;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerUnConfirmComponent;
import com.joker.module_order.di.module.UnConfirmModule;
import com.joker.module_order.mvp.contract.UnConfirmContract;
import com.joker.module_order.mvp.presenter.UnConfirmPresenter;

import com.joker.module_order.R;
import com.joker.module_order.mvp.view.adapter.UnConfirmListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 订单Fragment -> 待付款订单Fragment
 */
public class UnConfirmFragment extends BaseFragment<UnConfirmPresenter>
        implements UnConfirmContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView unConfirmList;

    private ProgressDialog progressDialog;
    private static final Integer STATE_UNCONFIRM = 1;
    private UnConfirmListAdapter unConfirmListAdapter;

    public static UnConfirmFragment newInstance() {
        UnConfirmFragment fragment = new UnConfirmFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerUnConfirmComponent
            .builder()
            .appComponent(appComponent)
            .unConfirmModule(new UnConfirmModule(this))
            .build()
            .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_un_confirm, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        unConfirmListAdapter = new UnConfirmListAdapter();
        unConfirmList.setLayoutManager(new LinearLayoutManager(getContext()));
        unConfirmList.setItemAnimator(new DefaultItemAnimator());
        unConfirmList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        unConfirmList.setAdapter(unConfirmListAdapter);

        unConfirmListAdapter.setPayButtonClickListener(new PayOrderListener());
        unConfirmListAdapter.setDeleteButtonClickListener(new DeleteOrderListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LoginUtil.getInstance().isLogin())
            mPresenter.getUnConfirmOrders(STATE_UNCONFIRM, LoginUtil.getInstance().getUser().getAccount());
    }

    /**
     * 获取待付款订单列表结果
     * @param result
     * @param tips
     * @param orderList
     */
    @Override
    public void getUnConfirmOrdersResult(Boolean result, String tips, List<Order> orderList) {
        Log.d("UnConfirmFragment",tips);
        if (result)
            unConfirmListAdapter.setItems(orderList);
        else
            unConfirmListAdapter.setItems(new ArrayList<>());
    }

    /**
     * 取消订单结果
     * @param result
     * @param tips
     * @param order
     */
    @Override
    public void cancelOrderResult(Boolean result, String tips, Order order) {
        ToastUtil.makeText(getContext(), tips);
        if (result)
            unConfirmListAdapter.removeItem(order);
    }

    /**
     * 点击付款按钮，进入订单详情页面，进行付款
     */
    class PayOrderListener implements UnConfirmListAdapter.PayButtonClickListener{
        @Override
        public void pay(View view, int position) {
            ARouter.getInstance()
                    .build(RouterHub.ORDER_ORDERDETAILACTIVITY)
                    .withSerializable("Order", unConfirmListAdapter.getItem(position))
                    .navigation(getContext());
        }
    }

    /**
     * 删除订单监听器
     */
    class DeleteOrderListener implements UnConfirmListAdapter.DeleteButtonClickListener{
        @Override
        public void deleteItem(View view, int position) {
            TipsDialog tipsDialog = new TipsDialog(getContext());
            tipsDialog.show();
            tipsDialog.setTitle("取消订单");
            tipsDialog.setTipsContent("确定取消订单？");
            tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
                @Override
                public void onCancel() {
                    tipsDialog.dismiss();
                }
            });
            tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    tipsDialog.dismiss();
                    mPresenter.cancelOrder(unConfirmListAdapter.getItem(position));
                }
            });
        }
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
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
