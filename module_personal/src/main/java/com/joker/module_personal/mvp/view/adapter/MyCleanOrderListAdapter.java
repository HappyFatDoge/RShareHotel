package com.joker.module_personal.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Order;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.holder.MyCleanOrderListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/11.
 */
public class MyCleanOrderListAdapter extends DefaultAdapter<CleanOrder> {


    private CleaningListener cleaningListener;
    private EndCleanListened endCleanListened;

    public MyCleanOrderListAdapter(){
        this(new ArrayList<>());
    }

    public MyCleanOrderListAdapter(List<CleanOrder> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CleanOrder> getHolder(View v, int viewType) {
        return new MyCleanOrderListHolder(v, cleaningListener, endCleanListened);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.my_clean_order_list_item;
    }


    /**
     * 设置item list
     * @param list
     */
    public void setItems(List<CleanOrder> list){
        this.mInfos = list;
        notifyDataSetChanged();
    }

    /**
     * 移除item
     * @param order
     */
    public void removeItem(CleanOrder order){
        mInfos.remove(order);
        notifyDataSetChanged();
    }


    public interface CleaningListener{
        void cleaning(View view, int position);
    }

    public interface EndCleanListened{
        void finish(View view, int position);
    }

    public void setCleaningListener(CleaningListener cleaningListener) {
        this.cleaningListener = cleaningListener;
    }

    public void setEndCleanListened(EndCleanListened endCleanListened) {
        this.endCleanListened = endCleanListened;
    }
}
