package com.example.commonres.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.commonres.R;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/26.
 */
public class PasswordConfirmDialog extends Dialog {

    private EditText password;
    private Button confirm;
    private Button cancel;

    private ConfirmClickedListener confirmClickedListener;
    private CancelClickedListener cancelClickedListener;

    public PasswordConfirmDialog(@NonNull Context context) {
        this(context,R.style.public_password_dialog);
    }

    public PasswordConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PasswordConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_password_confirm_dialog);

        initView();
    }

    private void initView(){

        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmClickedListener != null)
                    confirmClickedListener.confirmed(view);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelClickedListener != null)
                    cancelClickedListener.canceled(view);
            }
        });

        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {
        if (cancelClickedListener != null)
            cancelClickedListener.canceled(cancel);
    }

    public interface ConfirmClickedListener{
        void confirmed(View view);
    }

    public interface CancelClickedListener{
        void canceled(View view);
    }

    public void setConfirmClickedListener(ConfirmClickedListener confirmClickedListener) {
        this.confirmClickedListener = confirmClickedListener;
    }

    public void setCancelClickedListener(CancelClickedListener cancelClickedListener) {
        this.cancelClickedListener = cancelClickedListener;
    }


    public String getPassword(){
        return password.getText().toString();
    }

    public void resetPassword(){
        password.setText("");
    }
}
