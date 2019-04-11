package com.joker.module_personal.mvp.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.Hotel;
import com.example.commonres.utils.ImageUtil;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.jess.arms.base.BaseHolder;
import com.joker.module_personal.R2;
import com.joker.module_personal.mvp.view.adapter.PostCleanOrderListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/4/11.
 */
public class PostCleanOrderListHolder extends BaseHolder<Hotel> {

    @BindView(R2.id.hotel_photo)
    ImageView hotelPhoto;
    @BindView(R2.id.tv_name)
    TextView hotelName;
    @BindView(R2.id.tv_address)
    TextView hotelAddress;
    @BindView(R2.id.tv_price)
    TextView price;
    @BindView(R2.id.start_time)
    TextView startTime;
    @BindView(R2.id.end_time)
    TextView endTime;
    @BindView(R2.id.receive_order)
    Button receiveOrder;

    private SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
    private PostCleanOrderListAdapter.ReceiveOrderListener receiveOrderListener;

    public PostCleanOrderListHolder(View itemView,
                                    PostCleanOrderListAdapter.ReceiveOrderListener receiveOrderListener){
        this(itemView);
        this.receiveOrderListener = receiveOrderListener;
    }

    public PostCleanOrderListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Hotel data, int position) {
        hotelPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageAware imageAware = new ImageViewAware(hotelPhoto, false);
        ImageLoader.getInstance().displayImage(data.getUrl(), imageAware, ImageUtil.createOptions());

        hotelName.setText(data.getName());
        hotelAddress.setText(data.getCity() + "市" + data.getAddress());
        price.setText(data.getPrice().toString());
        try {
            startTime.setText(sp.format(sp.parse(data.getStartDate().getDate())));
            endTime.setText(sp.format(sp.parse(data.getEndDate().getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        receiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receiveOrderListener != null)
                    receiveOrderListener.receive(v, position);
            }
        });

    }
}
