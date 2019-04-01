package com.joker.module_home.mvp.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.Hotel;
import com.example.commonres.utils.ImageUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_home.R2;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import butterknife.BindView;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class FindRecyclerViewHolder extends BaseHolder<Hotel> {

    @BindView(R2.id.iv_find_hotel)
    ImageView photo;
    @BindView(R2.id.tv_name)
    TextView hotelName;
    @BindView(R2.id.tv_address)
    TextView hotelAddress;
    @BindView(R2.id.tv_find_mode)
    TextView hotelMode;
    @BindView(R2.id.tv_find_housetype)
    TextView houseType;
    @BindView(R2.id.tv_find_area)
    TextView hotelArea;
    @BindView(R2.id.tv_price)
    TextView hotelPrice;

    public FindRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Hotel data, int position) {
        //设置图片
        photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageAware imageAware = new ImageViewAware(photo, false);
        ImageLoader.getInstance().displayImage(data.getUrl(), imageAware, ImageUtil.createOptions());

        hotelName.setText(data.getName());
        hotelAddress.setText(data.getAddress());
        hotelMode.setText(data.getMode());
        houseType.setText(data.getHouseType());
        hotelArea.setText(data.getArea().toString());
        hotelPrice.setText(data.getPrice().toString());
    }
}
