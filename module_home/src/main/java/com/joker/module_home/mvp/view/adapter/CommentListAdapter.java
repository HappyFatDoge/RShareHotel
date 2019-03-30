package com.joker.module_home.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Comment;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_home.R;
import com.joker.module_home.mvp.view.holder.CommentListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/30.
 */
public class CommentListAdapter extends DefaultAdapter<Comment> {

    public CommentListAdapter(){
        super(new ArrayList<>());
    }

    public CommentListAdapter(List<Comment> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Comment> getHolder(View v, int viewType) {
        return new CommentListHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.comment_list_item;
    }

    /**
     * 设置item list
     * @param list
     */
    public void setItems(List<Comment> list){
        this.mInfos = list;
        notifyDataSetChanged();
    }
}
