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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonres.beans.IntegralRecord;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerAllIntegralRecordComponent;
import com.joker.module_personal.mvp.contract.AllIntegralRecordContract;
import com.joker.module_personal.mvp.presenter.AllIntegralRecordPresenter;

import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.adapter.IntegralRecordListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/05/2019 16:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
/**
 * 个人中心Fragment -> 信用积分 -> 使用记录 -> 使用记录fragment
 */
public class AllIntegralRecordFragment extends BaseFragment<AllIntegralRecordPresenter>
        implements AllIntegralRecordContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView integralRecordList;

    private IntegralRecordListAdapter integralRecordListAdapter;

    public static AllIntegralRecordFragment newInstance() {
        AllIntegralRecordFragment fragment = new AllIntegralRecordFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerAllIntegralRecordComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_integral_record, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        List<IntegralRecord> list = new ArrayList<IntegralRecord>();
        IntegralRecord integralRecord = new IntegralRecord("1", "", "香港浅水湾", "英式小别墅!", "2017-6-23", "300", "1");
        IntegralRecord integralRecord1 = new IntegralRecord("3", " ", "四川大凉山", "十六山度假别墅", "2017-4-25", "200", "1");
        IntegralRecord integralRecord2 = new IntegralRecord("2", " ", "青岛如歌岛向华区", "如歌岛度假村别墅", "2017-5-01", "300", "1");
        IntegralRecord integralRecord3 = new IntegralRecord("4", " ", "海南海滨区", "银天大酒店", "2017-4-15", "400", "0");

        list.add(integralRecord);
        list.add(integralRecord1);
        list.add(integralRecord2);
        list.add(integralRecord3);

        integralRecordListAdapter = new IntegralRecordListAdapter(list);
        integralRecordList.setLayoutManager(new LinearLayoutManager(getContext()));
        integralRecordList.setItemAnimator(new DefaultItemAnimator());
        integralRecordList.setAdapter(integralRecordListAdapter);
        integralRecordList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
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
