package com.joker.module_personal.mvp.view.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.commonres.beans.Coupon;
import com.jess.arms.base.BaseHolder;
import com.joker.module_personal.R;
import com.joker.module_personal.R2;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/5.
 */
public class CouponListHolder extends BaseHolder<Coupon> {

    @BindView(R2.id.coupon_frame_layout)
    FrameLayout frameLayout;
    @BindView(R2.id.time_validate_tv)
    TextView time;

    public CouponListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Coupon data, int position) {
        //如果优惠券金额为200且没有过期
        if (data.getPrice().equals("200") && data.getState().equals("1")) {
            frameLayout.setBackgroundResource(R.mipmap.coupon_200);
        } else if (data.getPrice().equals("100") && data.getState().equals("1")){  //如果优惠券金额为100且没有过期
            frameLayout.setBackgroundResource(R.mipmap.coupon_100);
        }

        time.setText(data.getTime_validate());
    }
}
