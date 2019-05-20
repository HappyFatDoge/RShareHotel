package com.example.commonres.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.R;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/28.
 */
public class CommonToolBar extends FrameLayout {

    private ImageView back;
    private TextView title;
    private TextView right;
    private ImageView rightImage;

    private boolean backVisible;
    private boolean rightTextViewVisible;
    private boolean rightImageViewVisible;
    private String titleText;
    private String rightText;
    private int rightId;

    private BackListener mBackListener;
    private RightListener mRightListener;

    public CommonToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.public_common_toolbar, this);

        initAttrs(context,attrs);
        initView();
    }

    private void initView(){
        back = this.findViewById(R.id.back);
        title = this.findViewById(R.id.title);
        right = this.findViewById(R.id.right);
        rightImage = this.findViewById(R.id.right_image);

        if (!backVisible)
            back.setVisibility(INVISIBLE);
        else{
            back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBackListener != null)
                        mBackListener.back(v);
                }
            });
        }

        if (rightTextViewVisible) {
            right.setText(rightText);
            right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRightListener != null)
                        mRightListener.clicked(v);
                }
            });
        } else
            right.setVisibility(INVISIBLE);
        if (rightImageViewVisible) {
            rightImage.setImageResource(rightId);
            rightImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRightListener != null)
                        mRightListener.clicked(v);
                }
            });
        } else
            rightImage.setVisibility(INVISIBLE);

        title.setText(titleText);
    }


    /**
     * 获取属性设置
     * @param context
     * @param attributeSet
     */
    private void initAttrs(Context context, AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CommonToolBar);

        backVisible = typedArray.getBoolean(R.styleable.CommonToolBar_backVisible,false);
        rightTextViewVisible = typedArray.getBoolean(R.styleable.CommonToolBar_rightTextViewVisible,false);
        rightImageViewVisible = typedArray.getBoolean(R.styleable.CommonToolBar_rightImageViewVisible,false);
        titleText = typedArray.getString(R.styleable.CommonToolBar_title);
        rightText = typedArray.getString(R.styleable.CommonToolBar_rightText);
        rightId = typedArray.getResourceId(R.styleable.CommonToolBar_rightSrcCompat,R.mipmap.left_arrow1);

        typedArray.recycle();
    }


    public void setBackListener(BackListener backListener) {
        mBackListener = backListener;
    }

    public void setRightListener(RightListener rightListener) {
        mRightListener = rightListener;
    }

    public interface BackListener{
        void back(View view);
    }

    public interface RightListener{
        void clicked(View view);
    }
}
