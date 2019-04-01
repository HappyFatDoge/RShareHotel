package com.example.commonres.dialog.hodler;

import android.view.View;
import android.widget.TextView;

import com.example.commonres.R;
import com.example.commonres.beans.LockBean;
import com.jess.arms.base.BaseHolder;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class BluetoothListHolder extends BaseHolder<LockBean> {

    TextView lockName;

    public BluetoothListHolder(View itemView) {
        super(itemView);
        lockName = itemView.findViewById(R.id.lock_name);
    }

    @Override
    public void setData(LockBean data, int position) {
        lockName.setText(data.getLockName());
    }
}
