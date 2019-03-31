package com.joker.module_message.mvp.view.adapter;

import android.view.View;

import com.example.commonres.beans.Message;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

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
        return null;
    }

    @Override
    public int getLayoutId(int viewType) {
        return 0;
    }
}
