package com.joker.module_personal.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Collection;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_personal.R;
import com.joker.module_personal.mvp.view.holder.CollectionListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/5.
 */
public class CollectionListAdapter extends DefaultAdapter<Collection> {

    private CancelCollectionListener cancelCollectionListener;
    private OrderListener orderListener;

    public CollectionListAdapter(){
        this(new ArrayList<>());
    }

    public CollectionListAdapter(List<Collection> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Collection> getHolder(View v, int viewType) {
        return new CollectionListHolder(v, cancelCollectionListener, orderListener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.collection_list_item;
    }

    /**
     * 设置item list
     * @param list
     */
    public void setItems(List<Collection> list){
        this.mInfos = list;
        notifyDataSetChanged();
    }

    /**
     * 移除item
     * @param item
     */
    public void removeItem(Collection item){
        mInfos.remove(item);
        notifyDataSetChanged();
    }

    /**
     * 取消收藏
     */
    public interface CancelCollectionListener{
        void cancelCollection(View view, int position);
    }

    /**
     *预定
     */
    public interface OrderListener{
        void order(View view, int position);
    }

    public void setCancelCollectionListener(CancelCollectionListener cancelCollectionListener) {
        this.cancelCollectionListener = cancelCollectionListener;
    }

    public void setOrderListener(OrderListener orderListener) {
        this.orderListener = orderListener;
    }
}
