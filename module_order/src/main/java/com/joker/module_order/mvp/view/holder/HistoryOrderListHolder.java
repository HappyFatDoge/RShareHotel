package com.joker.module_order.mvp.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.OrderStateUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_order.R2;
import com.joker.module_order.mvp.view.adapter.HistoryOrderListAdapter;

import butterknife.BindView;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/2.
 */
public class HistoryOrderListHolder extends BaseHolder<Order> {

    @BindView(R2.id.tv_history_order_num)
    TextView orderNumber;
    @BindView(R2.id.tv_history_order_state)
    TextView orderState;
    @BindView(R2.id.iv_history_del)
    ImageView deleteHistory;
    @BindView(R2.id.tv_history_order_name)
    TextView hotelName;
    @BindView(R2.id.tv_history_order_checkin)
    TextView checkInTime;
    @BindView(R2.id.tv_history_order_checkout)
    TextView checkOutTime;
    @BindView(R2.id.tv_history_order_amount)
    TextView hotelType;
    @BindView(R2.id.tv_history_order_price)
    TextView orderPrice;

    private HistoryOrderListAdapter.DeleteHistoryOrderListener mDeleteHistoryOrderListener;

    public HistoryOrderListHolder(View itemView,
                                  HistoryOrderListAdapter.DeleteHistoryOrderListener deleteHistoryOrderListener){
        this(itemView);
        this.mDeleteHistoryOrderListener = deleteHistoryOrderListener;
    }

    public HistoryOrderListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Order data, int position) {
        Hotel hotel = data.getHotel();
        orderNumber.setText(data.getObjectId());
        orderState.setText(OrderStateUtil.getState(data.getState()));
        deleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteHistoryOrderListener != null)
                    mDeleteHistoryOrderListener.delete(v, position);
            }
        });
        hotelName.setText(hotel.getName());
        checkInTime.setText(data.getCheckInTime().getDate());
        checkOutTime.setText(data.getCheckOutTime().getDate());
        hotelType.setText(hotel.getMode() + "/" + hotel.getHouseType()
            + "/" + hotel.getArea() + "m²/共" + data.getDays() + "天");
        orderPrice.setText(data.getPrice().toString());
    }
}
