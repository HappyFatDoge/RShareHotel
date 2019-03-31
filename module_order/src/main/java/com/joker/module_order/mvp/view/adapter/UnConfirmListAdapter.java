package com.joker.module_order.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Order;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_order.R;
import com.joker.module_order.mvp.view.holder.UnConfirmListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class UnConfirmListAdapter extends DefaultAdapter<Order> {

    private PayButtonClickListener payButtonClickListener;
    private DeleteButtonClickListener deleteButtonClickListener;

    public UnConfirmListAdapter(){
        super(new ArrayList<>());
    }

    public UnConfirmListAdapter(List<Order> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Order> getHolder(View v, int viewType) {
        return new UnConfirmListHolder(v,payButtonClickListener,deleteButtonClickListener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.order_un_confirm_item;
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

    public interface PayButtonClickListener{
        void pay(View view, int position);
    }

    public interface DeleteButtonClickListener{
        void deleteItem(View view, int position);
    }

    public void setPayButtonClickListener(PayButtonClickListener payButtonClickListener) {
        this.payButtonClickListener = payButtonClickListener;
    }

    public void setDeleteButtonClickListener(DeleteButtonClickListener deleteButtonClickListener) {
        this.deleteButtonClickListener = deleteButtonClickListener;
    }
}
