package com.joker.module_order.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Order;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_order.R;
import com.joker.module_order.mvp.view.holder.HousingListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class HousingListAdapter extends DefaultAdapter<Order> {

    private CheckInListener checkInListener;
    private MoreOperationListener moreOperationListener;

    public HousingListAdapter(){
        this(new ArrayList<>());
    }

    public HousingListAdapter(List<Order> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Order> getHolder(View v, int viewType) {
        return new HousingListHolder(v, checkInListener, moreOperationListener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.order_housing_item;
    }

    /**
     * 设置item list
     * @param list
     */
    public void setItems(List<Order> list){
        this.mInfos = list;
        notifyDataSetChanged();
    }

    /**
     * 移除item
     * @param order
     */
    public void removeItem(Order order){
        mInfos.remove(order);
        notifyDataSetChanged();
    }

    public interface CheckInListener{
        void checkIn(View view, int position);
    }

    public interface MoreOperationListener{
        void operate(View view, int position);
    }

    public void setCheckInListener(CheckInListener checkInListener) {
        this.checkInListener = checkInListener;
    }

    public void setMoreOperationListener(MoreOperationListener moreOperationListener) {
        this.moreOperationListener = moreOperationListener;
    }
}
