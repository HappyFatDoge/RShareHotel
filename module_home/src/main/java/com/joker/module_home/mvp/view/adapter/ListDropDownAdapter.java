package com.joker.module_home.mvp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.joker.module_home.R;
import com.joker.module_home.mvp.view.holder.ListDropDownHolder;

import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class ListDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ListDropDownAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListDropDownHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ListDropDownHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_drop_down_item, null);
            viewHolder = new ListDropDownHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ListDropDownHolder viewHolder) {
        viewHolder.getText().setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.getText().setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                viewHolder.getText().setBackgroundResource(R.color.check_bg);
            } else {
                viewHolder.getText().setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.getText().setBackgroundResource(R.color.white);
            }
        }
    }
}

