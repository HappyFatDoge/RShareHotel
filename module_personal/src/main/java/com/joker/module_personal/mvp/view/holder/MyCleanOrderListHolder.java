package com.joker.module_personal.mvp.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonres.beans.CleanOrder;
import com.example.commonres.beans.Hotel;
import com.jess.arms.base.BaseHolder;
import com.joker.module_personal.R2;
import com.joker.module_personal.mvp.view.adapter.MyCleanOrderListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/11.
 */
public class MyCleanOrderListHolder extends BaseHolder<CleanOrder> {

    @BindView(R2.id.tv_history_order_num)
    TextView orderNum;
    @BindView(R2.id.tv_housing_order_name)
    TextView hotelName;
    @BindView(R2.id.tv_housing_order_checkin)
    TextView startTime;
    @BindView(R2.id.tv_housing_order_checkout)
    TextView endTime;
    @BindView(R2.id.tv_housing_order_style)
    TextView hotelType;
    @BindView(R2.id.tv_housing_order_price)
    TextView price;
    @BindView(R2.id.bt_housing_sure)
    Button cleaning;
    @BindView(R2.id.bt_housing_more)
    Button finish;


    private SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
    private MyCleanOrderListAdapter.CleaningListener cleaningListener;
    private MyCleanOrderListAdapter.EndCleanListened endCleanListened;

    public MyCleanOrderListHolder(View itemView,
                                  MyCleanOrderListAdapter.CleaningListener cleaningListener,
                                  MyCleanOrderListAdapter.EndCleanListened endCleanListened){
        this(itemView);
        this.cleaningListener = cleaningListener;
        this.endCleanListened = endCleanListened;
    }

    public MyCleanOrderListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(CleanOrder data, int position) {
        Hotel hotel = data.getHotel();
        orderNum.setText(data.getObjectId());
        hotelName.setText(hotel.getName());
        try {
            startTime.setText(sp.format(sp.parse(hotel.getStartDate().getDate())));
            endTime.setText(sp.format(sp.parse(hotel.getEndDate().getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hotelType.setText(hotel.getMode() + "/" + hotel.getHouseType()
                + "/" + hotel.getArea() + "m²/共" + data.getDays() + "天");
        price.setText(data.getPrice().toString());

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endCleanListened != null)
                    endCleanListened.finish(v, position);
            }
        });

        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cleaningListener != null)
                    cleaningListener.cleaning(v, position);
            }
        });
    }
}
