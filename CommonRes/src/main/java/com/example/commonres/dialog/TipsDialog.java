package com.example.commonres.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonres.R;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/29.
 */
public class TipsDialog extends AlertDialog {

    private TextView tipsTitle,tipsContent;
    private Button confirm;
    private Button cancel;
    private OnConfirmListener confirmListener;
    private OnRCancelListener rCancelListener;


    public TipsDialog(Context context) {
        this(context, R.style.public_tips_dialog);
    }

    public TipsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public TipsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_tips_dialog);
        initView();
        initEvent();
    }


    private void initView(){
        tipsTitle = findViewById(R.id.title);
        tipsContent = findViewById(R.id.tipsContent);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
    }

    private void initEvent(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmListener != null)
                    confirmListener.onConfirm();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rCancelListener != null)
                    rCancelListener.onCancel();
            }
        });
    }

    @Override
    public void setTitle(int titleId) {
        //super.setTitle(titleId);
        String content=getContext().getResources().getString(titleId);
        setTitle(content);
    }

    @Override
    public void setTitle(CharSequence title) {
        //super.setTitle(title);
        tipsTitle.setText(title);
    }


    public void setConfirm(String confirm){
        this.confirm.setText(confirm);
    }

    public void setConfirm(int resId){
        this.confirm.setText(resId);
    }

    public void setTipsContent(CharSequence content){
        tipsContent.setText(content);
    }

    public void setTipsContent(int id){
        String content=getContext().getResources().getString(id);
        setTipsContent(content);
    }


    public void setOnConfirmListener(OnConfirmListener confirmListener){
        this.confirmListener=confirmListener;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public void setRCancelListener(OnRCancelListener rCancelListener) {
        this.rCancelListener = rCancelListener;
    }

    public interface OnConfirmListener{
        void onConfirm();
    }

    public interface OnRCancelListener{
        void onCancel();
    }
}
