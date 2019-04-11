package com.joker.module_personal.mvp.view.fragment;

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

import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerPostCleanOrderListComponent;
import com.joker.module_personal.mvp.contract.PostCleanOrderListContract;
import com.joker.module_personal.mvp.presenter.PostCleanOrderListPresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.adapter.PostCleanOrderListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

/**
 * 个人中心Fragment -> 清洁接单 -> 被发布的清洁列表fragment
 */
public class PostCleanOrderListFragment extends BaseFragment<PostCleanOrderListPresenter>
        implements PostCleanOrderListContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView postCleanOrderList;

    private PostCleanOrderListAdapter postCleanOrderListAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Integer mDays;

    public static PostCleanOrderListFragment newInstance() {
        PostCleanOrderListFragment fragment = new PostCleanOrderListFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerPostCleanOrderListComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_clean_order_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        postCleanOrderListAdapter = new PostCleanOrderListAdapter();
        postCleanOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        postCleanOrderList.setAdapter(postCleanOrderListAdapter);
        postCleanOrderList.setItemAnimator(new DefaultItemAnimator());
        postCleanOrderList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));

        postCleanOrderListAdapter.setReceiveOrderListener(new ReceiveOrderListener());
        //设置点击item，进入发布清洁的详情界面
        postCleanOrderListAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.findRentHouse();
    }

    /**
     * 点击接单，接受清洁订单
     */
    class ReceiveOrderListener implements PostCleanOrderListAdapter.ReceiveOrderListener{
        @Override
        public void receive(View view, int position) {
            Hotel hotel = postCleanOrderListAdapter.getItem(position);
            try {
                mDays = getCountDays(sdf.parse(hotel.getStartDate().getDate()),sdf.parse(hotel.getEndDate().getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mPresenter.receiveOrder(LoginUtil.getInstance().getUser(), hotel, mDays);
        }
    }


    /**
     * 接单结果
     * @param result
     * @param tips
     * @param hotel
     */
    @Override
    public void orderResult(Boolean result, String tips, Hotel hotel) {
        ToastUtil.makeText(getContext(), tips);
        if (result)
            postCleanOrderListAdapter.removeItem(hotel);
    }


    /**
     * 加载发布的订单
     * @param result
     * @param tips
     * @param hotel
     */
    @Override
    public void findRentHouseResult(Boolean result, String tips, List<Hotel> hotel) {
        Log.d(TAG, tips);
        postCleanOrderListAdapter.setItems(hotel);
    }

    /**
     * 计算预定天数
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer getCountDays(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
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
