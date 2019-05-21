package com.example.commonres.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.commonres.R;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/5/21.
 */
public class EnvironmentDialog extends Dialog {

    private TextView temperature,humidity,light;

    public EnvironmentDialog(@NonNull Context context) {
        super(context);
    }

    public EnvironmentDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EnvironmentDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_environment_dialog);
        initView();
    }

    private void initView(){
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        light = findViewById(R.id.light);
    }

    /**
     * 设置温度
     * @param temperature
     */
    public void setTemperature(String temperature){
        this.temperature.setText(temperature);
    }

    /**
     * 设置湿度
     * @param humidity
     */
    public void setHumidity(String humidity){
        this.humidity.setText(humidity);
    }

    /**
     * 设置光照
     * @param light
     */
    public void setLight(String light){
        this.light.setText(light);
    }
}
