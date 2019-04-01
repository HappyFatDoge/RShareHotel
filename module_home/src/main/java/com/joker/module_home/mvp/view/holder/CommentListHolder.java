package com.joker.module_home.mvp.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.beans.Comment;
import com.example.commonres.beans.User;
import com.example.commonres.utils.ImageUtil;
import com.jess.arms.base.BaseHolder;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import butterknife.BindView;

/**
 * @author Joker 2575490085@qq.com
 * Created on 2019/3/30.
 */
public class CommentListHolder extends BaseHolder<Comment> {

    @BindView(R2.id.iv_comment_user_icon)
    ImageView userIcon;
    @BindView(R2.id.tv_comment_username)
    TextView userName;
    @BindView(R2.id.tv_comment_date)
    TextView commentDate;
    @BindView(R2.id.tv_comment_content)
    TextView commentContent;

    public CommentListHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Comment data, int position) {
        User user = data.getUser();
        //设置user的头像
        userIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (user.getIcon() != null){
            ImageAware imageAware = new ImageViewAware(userIcon,false);
            ImageLoader.getInstance().displayImage(user.getIcon(),imageAware, ImageUtil.createOptions());
        }else
            userIcon.setImageResource(R.mipmap.login_head);
        userName.setText(user.getName());
        commentDate.setText(data.getCreatedAt());
        commentContent.setText(data.getContent());
    }
}
