package com.joker.module_message.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Message;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.joker.module_message.R;
import com.joker.module_message.mvp.view.holder.MessageListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class MessageListAdapter extends DefaultAdapter<Message> {

    public MessageListAdapter(){
        super(new ArrayList<>());
    }

    public MessageListAdapter(List<Message> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Message> getHolder(View v, int viewType) {
        return new MessageListHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.message_item_layout;
    }

    /**
     * 设置数据
     * @param infos
     */
    public void setItems(List<Message> infos){
        this.mInfos = infos;
        notifyDataSetChanged();
    }

}
