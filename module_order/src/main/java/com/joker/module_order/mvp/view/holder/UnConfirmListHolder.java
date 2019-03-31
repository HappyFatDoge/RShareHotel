package com.joker.module_order.mvp.view.holder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.OrderStateUtil;
import com.example.commonres.utils.ToastUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_order.R2;
import com.joker.module_order.mvp.view.adapter.UnConfirmListAdapter;

import butterknife.BindView;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class UnConfirmListHolder extends BaseHolder<Order> {

    @BindView(R2.id.tv_history_order_num)
    TextView orderNumber;
    @BindView(R2.id.tv_order_state)
    TextView orderState;
    @BindView(R2.id.bt_uncomfirm_pay)
    Button pay;
    @BindView(R2.id.iv_uncomfirm_cancel)
    ImageButton deleteOrder;
    @BindView(R2.id.tv_order_name)
    TextView hotelName;
    @BindView(R2.id.tv_order_checkintime)
    TextView checkInTime;
    @BindView(R2.id.tv_order_checkouttime)
    TextView checkOutTime;
    @BindView(R2.id.tv_order_amount)
    TextView orderType;
    @BindView(R2.id.tv_order_price)
    TextView orderPrice;

    private UnConfirmListAdapter.PayButtonClickListener payButtonClickListener;
    private UnConfirmListAdapter.DeleteButtonClickListener deleteButtonClickListener;

    public UnConfirmListHolder(View itemView,
                               UnConfirmListAdapter.PayButtonClickListener payButtonClickListener,
                               UnConfirmListAdapter.DeleteButtonClickListener deleteButtonClickListener){
        this(itemView);
        this.payButtonClickListener = payButtonClickListener;
        this.deleteButtonClickListener = deleteButtonClickListener;
    }

    public UnConfirmListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Order data, int position) {
        Hotel hotel = data.getHotel();
        orderNumber.setText(data.getObjectId());
        orderState.setText(OrderStateUtil.getState(data.getState()));
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payButtonClickListener != null) {
                    Log.d("data","付款");
                    payButtonClickListener.pay(v, position);
                }
            }
        });
        deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteButtonClickListener != null) {
                    Log.d("data","取消订单");
                    deleteButtonClickListener.deleteItem(v, position);
                }
            }
        });
        hotelName.setText(hotel.getName());
        checkInTime.setText(data.getCheckInTime().getDate());
        checkOutTime.setText(data.getCheckOutTime().getDate());
        orderType.setText(hotel.getMode() + "/" + hotel.getHouseType()
            + "/" + hotel.getArea() + "m²/共" + data.getDays() + "天");
        orderPrice.setText(data.getPrice().toString());
    }
}
