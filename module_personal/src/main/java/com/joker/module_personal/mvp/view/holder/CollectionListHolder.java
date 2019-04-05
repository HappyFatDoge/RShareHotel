package com.joker.module_personal.mvp.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.Collection;
import com.example.commonres.beans.Hotel;
import com.example.commonres.utils.ImageUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_personal.R2;
import com.joker.module_personal.mvp.view.adapter.CollectionListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/5.
 */
public class CollectionListHolder extends BaseHolder<Collection> {

    private CollectionListAdapter.CancelCollectionListener cancelCollectionListener;
    private CollectionListAdapter.OrderListener orderListener;

    @BindView(R2.id.iv_collection_photo)
    ImageView hotelPhoto;
    @BindView(R2.id.tv_collection_name)
    TextView hotelName;
    @BindView(R2.id.tv_collection_address)
    TextView hotelAddress;
    @BindView(R2.id.tv_collection_mode)
    TextView hotelMode;
    @BindView(R2.id.tv_collection_style)
    TextView hotelStyle;
    @BindView(R2.id.iv_collection_like)
    ImageView collectionLike;
    @BindView(R2.id.tv_collection_price)
    TextView hotelPrice;
    @BindView(R2.id.btn_collection_book)
    Button book;

    public CollectionListHolder(View itemView,
                                CollectionListAdapter.CancelCollectionListener cancelCollectionListener,
                                CollectionListAdapter.OrderListener orderListener){
        this(itemView);
        this.cancelCollectionListener = cancelCollectionListener;
        this.orderListener = orderListener;
    }

    public CollectionListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Collection data, int position) {
        Hotel hotel = data.getHotel();
        hotelPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageAware imageAware = new ImageViewAware(hotelPhoto, false);
        ImageLoader.getInstance().displayImage(hotel.getUrl(),imageAware, ImageUtil.createOptions());

        hotelName.setText(hotel.getName());
        hotelAddress.setText(hotel.getAddress());
        hotelMode.setText(hotel.getHouseType() + "/" + hotel.getArea() + "m²");
        hotelStyle.setText(hotel.getMode());
        collectionLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelCollectionListener != null)
                    cancelCollectionListener.cancelCollection(v, position);
            }
        });

        hotelPrice.setText(hotel.getPrice());
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderListener != null)
                    orderListener.order(v, position);
            }
        });
    }
}
