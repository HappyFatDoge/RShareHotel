package com.joker.module_order.mvp.view.holder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.OrderStateUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_order.R2;
import com.joker.module_order.mvp.view.adapter.HousingListAdapter;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class HousingListHolder extends BaseHolder<Order> {

    @BindView(R2.id.tv_history_order_num)
    TextView orderNumber;
    @BindView(R2.id.tv_housing_order_state)
    TextView orderState;
    @BindView(R2.id.bt_housing_sure)
    Button checkIn;
    @BindView(R2.id.bt_housing_more)
    Button moreOperation;
    @BindView(R2.id.tv_housing_order_name)
    TextView hotelName;
    @BindView(R2.id.tv_housing_order_checkin)
    TextView checkInTime;
    @BindView(R2.id.tv_housing_order_checkout)
    TextView checkOutTime;
    @BindView(R2.id.tv_housing_order_style)
    TextView orderType;
    @BindView(R2.id.tv_housing_order_price)
    TextView orderPrice;

    private HousingListAdapter.CheckInListener checkInListener;
    private HousingListAdapter.MoreOperationListener moreOperationListener;

    public HousingListHolder(View itemView, HousingListAdapter.CheckInListener checkInListener,
                             HousingListAdapter.MoreOperationListener moreOperationListener){
        this(itemView);
        this.checkInListener = checkInListener;
        this.moreOperationListener = moreOperationListener;
    }

    public HousingListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Order data, int position) {
        Hotel hotel = data.getHotel();
        orderNumber.setText(data.getObjectId());
        orderState.setText(OrderStateUtil.getState(data.getState()));
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInListener != null){
                    Log.d("data","入住");
                    checkInListener.checkIn(v, position);
                }
            }
        });
        moreOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moreOperationListener != null){
                    Log.d("data","更多操作");
                    moreOperationListener.operate(v, position);
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
