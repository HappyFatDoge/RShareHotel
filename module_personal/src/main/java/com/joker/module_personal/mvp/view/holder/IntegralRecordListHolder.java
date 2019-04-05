package com.joker.module_personal.mvp.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.IntegralRecord;
import com.jess.arms.base.BaseHolder;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/5.
 */
public class IntegralRecordListHolder extends BaseHolder<IntegralRecord> {

    @BindView(R2.id.image_url_iv)
    ImageView hotelPhoto;
    @BindView(R2.id.home_address_tv)
    TextView hotelAddress;
    @BindView(R2.id.home_name_tv)
    TextView hotelName;
    @BindView(R2.id.integral_tv)
    TextView integral;
    @BindView(R2.id.time_tv)
    TextView time;


    public IntegralRecordListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(IntegralRecord data, int position) {
        if (position == 0) {
            hotelPhoto.setImageResource(R.mipmap.home1);
        } else if (position == 1) {
            hotelPhoto.setImageResource(R.mipmap.home2);
        } else if (position == 2) {
            hotelPhoto.setImageResource(R.mipmap.home3);
        } else if (position == 3) {
            hotelPhoto.setImageResource(R.mipmap.home4);
        }

        hotelAddress.setText(data.getHome_address());
        hotelName.setText(data.getHome_name());
        time.setText(data.getTime());
        if (data.getState().equals("1")) {
            integral.setText("+" + data.getIntegral());
        } else {
            integral.setText("-" + data.getIntegral());
            integral.setTextColor(0xFFFF0000);
        }
    }
}
