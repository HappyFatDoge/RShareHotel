package com.example.commonres.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonres.R;

public class AirConditioningControlDialog extends Dialog {

    //降温
    private ImageView downTemperature;
    //升温
    private ImageView upTemperature;
    //温度显示
    private TextView temperature;
    //制冷
    private ImageView refrigeration;
    //制热
    private ImageView heating;
    //除湿
    private ImageView dehumidification;
    //风速
    private ImageView windSpeed;
    //自动
    private ImageView automatic;
    //开关
    private ImageView switchOfAirConditioning;

    //接口实例
    private DownTemperatureListener mDownTemperatureListener;
    private UpTemperatureListener mUpTemperatureListener;
    private RefrigerationListener mRefrigerationListener;
    private HeatingListener mHeatingListener;
    private DehumidificationListener mDehumidificationListener;
    private WindSpeedListener mWindSpeedListener;
    private AutomaticListener mAutomaticListener;
    private SwitchListener mSwitchListener;


    public AirConditioningControlDialog(@NonNull Context context) {
        super(context);
    }

    public AirConditioningControlDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AirConditioningControlDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_air_conditioning_control_dialog);

        initView();
    }


    public void initView(){
        downTemperature = findViewById(R.id.down_temperature);
        upTemperature = findViewById(R.id.up_temperature);
        temperature = findViewById(R.id.temperature);
        refrigeration = findViewById(R.id.refrigeration_iv);
        heating = findViewById(R.id.heating_iv);
        dehumidification = findViewById(R.id.dehumidification_iv);
        windSpeed = findViewById(R.id.wind_speed_iv);
        automatic = findViewById(R.id.automatic_iv);
        switchOfAirConditioning = findViewById(R.id.switch_iv);


        downTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownTemperatureListener != null)
                    mDownTemperatureListener.downTemperature(v);
            }
        });

        upTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUpTemperatureListener != null)
                    mUpTemperatureListener.upTemperature(v);
            }
        });

        refrigeration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRefrigerationListener != null)
                    mRefrigerationListener.refrigeration(v);
            }
        });

        heating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeatingListener != null)
                    mHeatingListener.heating(v);
            }
        });

        dehumidification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDehumidificationListener != null)
                    mDehumidificationListener.dehumidification(v);
            }
        });

        windSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWindSpeedListener != null)
                    mWindSpeedListener.windSpeed(v);
            }
        });

        automatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAutomaticListener != null)
                    mAutomaticListener.automatic(v);
            }
        });

        switchOfAirConditioning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitchListener != null)
                    mSwitchListener.switchOfAirConditioning(v);
            }
        });
    }


    public void setDownTemperatureListener(DownTemperatureListener downTemperatureListener) {
        mDownTemperatureListener = downTemperatureListener;
    }

    public void setUpTemperatureListener(UpTemperatureListener upTemperatureListener) {
        mUpTemperatureListener = upTemperatureListener;
    }

    public void setRefrigerationListener(RefrigerationListener refrigerationListener) {
        mRefrigerationListener = refrigerationListener;
    }

    public void setHeatingListener(HeatingListener heatingListener) {
        mHeatingListener = heatingListener;
    }

    public void setDehumidificationListener(DehumidificationListener dehumidificationListener) {
        mDehumidificationListener = dehumidificationListener;
    }

    public void setWindSpeedListener(WindSpeedListener windSpeedListener) {
        mWindSpeedListener = windSpeedListener;
    }

    public void setAutomaticListener(AutomaticListener automaticListener) {
        mAutomaticListener = automaticListener;
    }

    public void setSwitchListener(SwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

    /**
     * 设置当前温度
     * @param temperature
     */
    public void setTemperature(String temperature){
        this.temperature.setText(temperature);
    }


    /**
     * 降温监听接口
     */
    public interface DownTemperatureListener{
        void downTemperature(View view);
    }

    /**
     * 升温监听接口
     */
    public interface UpTemperatureListener{
        void upTemperature(View view);
    }

    /**
     * 制冷监听接口
     */
    public interface RefrigerationListener{
        void refrigeration(View view);
    }

    /**
     * 制热监听接口
     */
    public interface HeatingListener{
        void heating(View view);
    }

    /**
     * 除湿监听接口
     */
    public interface DehumidificationListener{
        void dehumidification(View view);
    }

    /**
     * 风速监听接口
     */
    public interface WindSpeedListener{
        void windSpeed(View view);
    }

    /**
     * 自动监听接口
     */
    public interface AutomaticListener{
        void automatic(View view);
    }

    /**
     * 开关监听接口
     */
    public interface SwitchListener{
        void switchOfAirConditioning(View view);
    }

}
