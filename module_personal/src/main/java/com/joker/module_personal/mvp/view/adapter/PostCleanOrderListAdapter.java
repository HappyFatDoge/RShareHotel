package com.joker.module_personal.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Hotel;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.fragment.PostCleanOrderListFragment;
import com.joker.module_personal.mvp.view.holder.PostCleanOrderListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/11.
 */
public class PostCleanOrderListAdapter extends DefaultAdapter<Hotel> {

    private ReceiveOrderListener receiveOrderListener;

    public PostCleanOrderListAdapter(){
        this(new ArrayList<>());
    }

    public PostCleanOrderListAdapter(List<Hotel> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Hotel> getHolder(View v, int viewType) {
        return new PostCleanOrderListHolder(v, receiveOrderListener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.post_clean_order_list_item;
    }


    public void setItems(List<Hotel> list){
        this.mInfos = list;
        notifyDataSetChanged();
    }

    public void removeItem(Hotel hotel){
        mInfos.remove(hotel);
        notifyDataSetChanged();
    }

    public interface ReceiveOrderListener{
        void receive(View view, int position);
    }

    public void setReceiveOrderListener(ReceiveOrderListener receiveOrderListener) {
        this.receiveOrderListener = receiveOrderListener;
    }
}
