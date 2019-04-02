package com.joker.module_order.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Order;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_order.R;
import com.joker.module_order.mvp.view.holder.HistoryOrderListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/2.
 */
public class HistoryOrderListAdapter extends DefaultAdapter<Order> {

    private DeleteHistoryOrderListener mDeleteHistoryOrderListener;

    public HistoryOrderListAdapter(){
        this(new ArrayList<>());
    }

    public HistoryOrderListAdapter(List<Order> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Order> getHolder(View v, int viewType) {
        return new HistoryOrderListHolder(v, mDeleteHistoryOrderListener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.order_history_order_item;
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
     * 删除订单item
     * @param order
     */
    public void removeItem(Order order){
        mInfos.remove(order);
        notifyDataSetChanged();
    }

    public interface DeleteHistoryOrderListener{
        void delete(View view, int position);
    }

    public void setDeleteHistoryOrderListener(DeleteHistoryOrderListener deleteHistoryOrderListener) {
        mDeleteHistoryOrderListener = deleteHistoryOrderListener;
    }
}
