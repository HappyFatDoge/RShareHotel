package com.joker.module_personal.mvp.view.fragment;

import android.content.Context;
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
import com.example.commonres.beans.Hotel;
import com.example.commonres.dialog.ProgressDialog;
import com.example.commonres.dialog.TipsDialog;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerRentHouseComponent;
import com.joker.module_personal.mvp.contract.RentHouseContract;
import com.joker.module_personal.mvp.presenter.RentHousePresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.activity.MyHouseActivity;
import com.joker.module_personal.mvp.view.activity.PostHouseActivity;
import com.joker.module_personal.mvp.view.adapter.MyHouseListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 14:09
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
/**
 * 个人中心Fragment -> 我的房子 -> 出租的房子的fragment
 */
public class RentHouseFragment extends BaseFragment<RentHousePresenter>
        implements RentHouseContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView rentHouseList;

    private MyHouseListAdapter rentHouseListAdapter;

    private static final Integer STATUS_POST = 1;
    private static final Integer STATUS_BOOK = 2;
    private static final Integer STATUS_RENT = 3;
    private static final String TAG = "RentHouseFragment";

    public static RentHouseFragment newInstance() {
        RentHouseFragment fragment = new RentHouseFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerRentHouseComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rent_house, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        rentHouseListAdapter = new MyHouseListAdapter();
        rentHouseList.setLayoutManager(new LinearLayoutManager(getContext()));
        rentHouseList.setItemAnimator(new DefaultItemAnimator());
        rentHouseList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        rentHouseList.setAdapter(rentHouseListAdapter);

        rentHouseListAdapter.setEditHotelListener(new EditHotelListener());
        rentHouseListAdapter.setOperationListener(new OperationListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.findMyRentHouse();
    }

    /**
     * 加载出租的房子结果
     * @param result
     * @param tips
     * @param list
     */
    @Override
    public void findMyRentHouseResult(Boolean result, String tips, List<Hotel> list) {
//        ToastUtil.makeText(getContext(), tips);
        Log.d(TAG, tips);
        rentHouseListAdapter.setItems(list);
    }


    /**
     * 下架房子结果
     * @param result
     * @param tips
     * @param hotel
     */
    @Override
    public void removeRentHouseResult(Boolean result, String tips, Hotel hotel) {
        ToastUtil.makeText(getContext(), tips);
        if (result)
            rentHouseListAdapter.removeItem(hotel);
    }


    /**
     * 接收订单结果
     * @param result
     * @param tips
     * @param position
     */
    @Override
    public void receiveOrderResult(Boolean result, String tips, int position) {
        ToastUtil.makeText(getContext(), tips);
        if (result)
            rentHouseListAdapter.notifyItemChanged(position);
    }


    /**
     * 拒绝订单结果
     * @param result
     * @param tips
     * @param position
     */
    @Override
    public void rejectOrderResult(Boolean result, String tips, int position) {
        ToastUtil.makeText(getContext(), tips);
        if (result)
            rentHouseListAdapter.notifyItemChanged(position);
    }


    /**
     * 编辑发布中的房子
     */
    class EditHotelListener implements MyHouseListAdapter.EditHotelListener{
        @Override
        public void edit(View view, int position) {
            Intent intent = new Intent(getActivity(), PostHouseActivity.class);
            intent.putExtra("Hotel", rentHouseListAdapter.getItem(position));
            startActivityForResult(intent, 1);
        }
    }


    /**
     * 房子的状态以及操作
     */
    class OperationListener implements MyHouseListAdapter.OperationListener{
        @Override
        public void operate(View view, int position) {
            Hotel hotel = rentHouseListAdapter.getItem(position);
            if (hotel.getType() == STATUS_POST) //下架房子
                mPresenter.removeHouse(hotel);
            else if (hotel.getType() == STATUS_BOOK) { //接收/取消订单
                TipsDialog tipsDialog = new TipsDialog(getContext());
                tipsDialog.show();
                tipsDialog.setTitle("订单操作");
                tipsDialog.setTipsContent("接收或者取消订单");
                tipsDialog.setRCancelListener(new TipsDialog.OnRCancelListener() {
                    @Override
                    public void onCancel() {  //取消订单
                        tipsDialog.dismiss();
                        mPresenter.rejectOrder(hotel, position);
                    }
                });
                tipsDialog.setOnConfirmListener(new TipsDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {  //接收订单
                        tipsDialog.dismiss();
                        mPresenter.receiveOrder(hotel, position);
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1://编辑发布的房子结果返回
                if (data.getBooleanExtra("update", false))
                    rentHouseListAdapter.notifyDataSetChanged();
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
