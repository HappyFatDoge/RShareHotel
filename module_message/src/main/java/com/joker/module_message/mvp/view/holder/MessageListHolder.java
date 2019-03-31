package com.joker.module_message.mvp.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.Message;
import com.example.commonres.utils.ImageUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_message.R;
import com.joker.module_message.R2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/3/31.
 */
public class MessageListHolder extends BaseHolder<Message> {

    @BindView(R2.id.image_url_iv)
    ImageView image;
    @BindView(R2.id.title_tv)
    TextView title;
    @BindView(R2.id.time_tv)
    TextView time;
    @BindView(R2.id.easy_content_tv)
    TextView easyContent;


    public MessageListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Message data, int position) {
        //设置message icon
        image.setScaleType(ImageView.ScaleType.CENTER);
        if (data.getImage_url() == null)
            image.setImageResource(R.mipmap.message_icon2);
        else{
            ImageAware imageAware = new ImageViewAware(image,false);
            ImageLoader.getInstance().displayImage(data.getImage_url(),imageAware, ImageUtil.createOptions());
        }
        title.setText(data.getTitle());
        time.setText(data.getTime());
        easyContent.setText(data.getEasy_content());
    }
}
