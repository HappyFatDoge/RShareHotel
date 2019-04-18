package com.joker.module_personal.mvp.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.Hotel;
import com.example.commonres.utils.ImageUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_personal.R2;
import com.joker.module_personal.mvp.view.adapter.MyHouseListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import butterknife.BindView;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/8.
 */
public class MyHouseListHolder extends BaseHolder<Hotel> {

    @BindView(R2.id.hotel_photo)
    ImageView hotelPhoto;
    @BindView(R2.id.tv_name)
    TextView hotelName;
    @BindView(R2.id.tv_address)
    TextView hotelAddress;
    @BindView(R2.id.tv_price)
    TextView hotelPrice;
    @BindView(R2.id.house_type)
    TextView hotelType;
    @BindView(R2.id.edit_button)
    Button edit;
    @BindView(R2.id.status_button)
    Button status;
    @BindView(R2.id.price_desc)
    TextView priceDesc;

    private static final Integer STATUS_POST = 1;
    private static final Integer STATUS_BOOK = 2;
    private static final Integer STATUS_RENT = 3;
    private static final Integer STATUS_POST_CLEAN = 4;
    private static final Integer STATUS_ORDER_CLEAN = 5;
    private static final Integer STATUS_CLEANING = 6;

    private MyHouseListAdapter.EditHotelListener mEditHotelListener;
    private MyHouseListAdapter.OperationListener mOperationListener;

    public MyHouseListHolder(View itemView,
                             MyHouseListAdapter.EditHotelListener editHotelListener,
                             MyHouseListAdapter.OperationListener operationListener){
        this(itemView);
        mEditHotelListener = editHotelListener;
        mOperationListener = operationListener;
    }

    public MyHouseListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Hotel data, int position) {
        hotelPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageAware imageAware = new ImageViewAware(hotelPhoto, false);
        ImageLoader.getInstance().displayImage(data.getUrl(), imageAware, ImageUtil.createOptions());
        hotelName.setText(data.getName());
        hotelAddress.setText(data.getAddress());
        hotelPrice.setText(data.getPrice().toString());
        hotelType.setText(data.getMode() + "/" + data.getHouseType() + "/" + data.getArea() + "M²");
        if (data.getType() == STATUS_POST && data.getAvailable() == 1) {
            priceDesc.setText("租金：");
            status.setText("下架");
            status.setEnabled(true);
            edit.setEnabled(true);
        } else if (data.getType() == STATUS_BOOK) {
            priceDesc.setText("租金：");
            status.setText("待确认");
            status.setEnabled(true);
            edit.setEnabled(false);
        } else if (data.getType() == STATUS_RENT) {
            priceDesc.setText("租金：");
            status.setText("出租中...");
            status.setEnabled(false);
            edit.setEnabled(false);
        }else if (data.getType() == STATUS_POST_CLEAN){
            priceDesc.setText("佣金：");
            status.setText("撤回");
            status.setEnabled(true);
            edit.setEnabled(true);
        }else if (data.getType() == STATUS_ORDER_CLEAN){
            priceDesc.setText("佣金：");
            status.setText("待确认");
            status.setEnabled(true);
            edit.setEnabled(false);
        } else if (data.getType() == STATUS_CLEANING) {
            priceDesc.setText("佣金：");
            status.setText("清洁中...");
            status.setEnabled(false);
            edit.setEnabled(false);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditHotelListener != null)
                    mEditHotelListener.edit(v, position);
            }
        });



        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOperationListener != null)
                    mOperationListener.operate(v, position);
            }
        });
    }
}
