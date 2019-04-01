package com.joker.module_order.mvp.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonres.beans.Hotel;
import com.example.commonres.beans.Order;
import com.jess.arms.base.BaseHolder;
import com.joker.module_order.R2;
import com.joker.module_order.mvp.view.adapter.UnCommentListAdapter;

import butterknife.BindView;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class UnCommentListHolder extends BaseHolder<Order> {

    @BindView(R2.id.tv_comment_order_name)
    TextView hotelName;
    @BindView(R2.id.tv_comment_order_checkin)
    TextView checkInTime;
    @BindView(R2.id.tv_comment_order_checkout)
    TextView checkOutTime;
    @BindView(R2.id.bt_comment_comment)
    Button comment;

    private UnCommentListAdapter.CommentListener mCommentListener;

    public UnCommentListHolder(View itemView, UnCommentListAdapter.CommentListener commentListener){
        this(itemView);
        this.mCommentListener = commentListener;
    }

    public UnCommentListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Order data, int position) {
        Hotel hotel = data.getHotel();
        hotelName.setText(hotel.getName());
        checkInTime.setText(data.getCheckInTime().getDate());
        checkOutTime.setText(data.getCheckOutTime().getDate());
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentListener != null)
                    mCommentListener.comment(v, position);
            }
        });
    }
}
