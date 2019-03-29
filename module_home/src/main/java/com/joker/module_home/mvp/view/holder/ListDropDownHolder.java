package com.joker.module_home.mvp.view.holder;

import android.view.View;
import android.widget.TextView;

import com.joker.module_home.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class ListDropDownHolder {

    @BindView(R2.id.text)
    TextView mText;

    public ListDropDownHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public TextView getText(){
        return mText;
    }
}
