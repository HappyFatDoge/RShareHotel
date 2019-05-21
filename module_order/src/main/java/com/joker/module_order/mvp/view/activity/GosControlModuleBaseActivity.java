package com.joker.module_order.mvp.view.activity;

import android.util.Log;

import com.example.commonres.utils.HexStrUtils;
import com.example.commonres.utils.ToastUtil;
import com.jess.arms.mvp.IPresenter;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/5/21.
 */
public abstract class GosControlModuleBaseActivity<P extends IPresenter> extends GosBaseActivity<P> {

    /*
     * ===========================================================
     * 以下key值对应开发者在云端定义的数据点标识名
     * ===========================================================
     */
    // 数据点"继电器"对应的标识名
    protected static final String KEY_ESP8266_RELAY = "ESP8266_relay";
    // 数据点"蜂鸣器"对应的标识名
    protected static final String KEY_ESP8266_BUZZER = "ESP8266_buzzer";
    // 数据点"彩灯亮度"对应的标识名
    protected static final String KEY_RGB_BRIGHTNESS = "RGB_Brightness";
    // 数据点"彩灯红色"对应的标识名
    protected static final String KEY_RGB_RED = "RGB_red";
    // 数据点"彩灯蓝色"对应的标识名
    protected static final String KEY_RGB_BLUE = "RGB_blue";
    // 数据点"彩灯绿色"对应的标识名
    protected static final String KEY_RGB_GREEN = "RGB_green";
    // 数据点"温度"对应的标识名
    protected static final String KEY_ESP8266_TEMP = "ESP8266_temp";
    // 数据点"湿度"对应的标识名
    protected static final String KEY_ESP8266_HUM = "ESP8266_hum";
    // 数据点"光照"对应的标识名
    protected static final String KEY_ESP8266_LIGHT = "ESP8266_light";

    /*
     * ===========================================================
     * 以下数值对应开发者在云端定义的可写数值型数据点增量值、数据点定义的分辨率、seekbar滚动条补偿值
     * _ADDITION:数据点增量值
     * _RATIO:数据点定义的分辨率
     * _OFFSET:seekbar滚动条补偿值
     * APP与设备定义的协议公式为：y（APP接收的值）=x（设备上报的值）* RATIO（分辨率）+ADDITION（增量值）
     * 由于安卓的原生seekbar无法设置最小值，因此代码中增加了一个补偿量OFFSET
     * 实际上公式中的：x（设备上报的值）=seekbar的值+补偿值
     * ===========================================================
     */
    // 数据点"彩灯亮度"对应seekbar滚动条补偿值
    protected static final int RGB_BRIGHTNESS_OFFSET = 0;
    // 数据点"彩灯亮度"对应数据点增量值
    protected static final int RGB_BRIGHTNESS_ADDITION = 0;
    // 数据点"彩灯亮度"对应数据点定义的分辨率
    protected static final int RGB_BRIGHTNESS_RATIO = 1;

    // 数据点"彩灯红色"对应seekbar滚动条补偿值
    protected static final int RGB_RED_OFFSET = 0;
    // 数据点"彩灯红色"对应数据点增量值
    protected static final int RGB_RED_ADDITION = 0;
    // 数据点"彩灯红色"对应数据点定义的分辨率
    protected static final int RGB_RED_RATIO = 1;

    // 数据点"彩灯蓝色"对应seekbar滚动条补偿值
    protected static final int RGB_BLUE_OFFSET = 0;
    // 数据点"彩灯蓝色"对应数据点增量值
    protected static final int RGB_BLUE_ADDITION = 0;
    // 数据点"彩灯蓝色"对应数据点定义的分辨率
    protected static final int RGB_BLUE_RATIO = 1;

    // 数据点"彩灯绿色"对应seekbar滚动条补偿值
    protected static final int RGB_GREEN_OFFSET = 0;
    // 数据点"彩灯绿色"对应数据点增量值
    protected static final int RGB_GREEN_ADDITION = 0;
    // 数据点"彩灯绿色"对应数据点定义的分辨率
    protected static final int RGB_GREEN_RATIO = 1;


    /*
     * ===========================================================
     * 以下变量对应设备上报类型为布尔、数值、扩展数据点的数据存储
     * ===========================================================
     */
    // 数据点"继电器"对应的存储数据
    protected static boolean data_ESP8266_relay;
    // 数据点"蜂鸣器"对应的存储数据
    protected static boolean data_ESP8266_buzzer;
    // 数据点"彩灯亮度"对应的存储数据
    protected static int data_RGB_Brightness;
    // 数据点"彩灯红色"对应的存储数据
    protected static int data_RGB_red;
    // 数据点"彩灯蓝色"对应的存储数据
    protected static int data_RGB_blue;
    // 数据点"彩灯绿色"对应的存储数据
    protected static int data_RGB_green;
    // 数据点"温度"对应的存储数据
    protected static int data_ESP8266_temp;
    // 数据点"湿度"对应的存储数据
    protected static int data_ESP8266_hum;
    // 数据点"光照"对应的存储数据
    protected static int data_ESP8266_light;

    /*
     * ===========================================================
     * 以下key值对应设备硬件信息各明细的名称，用与回调中提取硬件信息字段。
     * ===========================================================
     */
    protected static final String WIFI_HARDVER_KEY = "wifiHardVersion";
    protected static final String WIFI_SOFTVER_KEY = "wifiSoftVersion";
    protected static final String MCU_HARDVER_KEY = "mcuHardVersion";
    protected static final String MCU_SOFTVER_KEY = "mcuSoftVersion";
    protected static final String WIFI_FIRMWAREID_KEY = "wifiFirmwareId";
    protected static final String WIFI_FIRMWAREVER_KEY = "wifiFirmwareVer";
    protected static final String PRODUCT_KEY = "productKey";

    @SuppressWarnings("unchecked")
    protected void getDataFromReceiveDataMap(ConcurrentHashMap<String, Object> dataMap) {
        // 已定义的设备数据点，有布尔、数值和枚举型数据

        if (dataMap.get("data") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            for (String dataKey : map.keySet()) {
                if (dataKey.equals(KEY_ESP8266_RELAY)) {
                    data_ESP8266_relay = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_ESP8266_BUZZER)) {
                    data_ESP8266_buzzer = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_RGB_BRIGHTNESS)) {

                    data_RGB_Brightness = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_RGB_RED)) {

                    data_RGB_red = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_RGB_BLUE)) {

                    data_RGB_blue = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_RGB_GREEN)) {

                    data_RGB_green = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_ESP8266_TEMP)) {

                    data_ESP8266_temp = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_ESP8266_HUM)) {

                    data_ESP8266_hum = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_ESP8266_LIGHT)) {

                    data_ESP8266_light = (Integer) map.get(dataKey);
                }
            }
        }

        StringBuilder sBuilder = new StringBuilder();

        // 已定义的设备报警数据点，设备发生报警后该字段有内容，没有发生报警则没内容
        if (dataMap.get("alerts") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");
            for (String alertsKey : map.keySet()) {
                if ((Boolean) map.get(alertsKey)) {
                    sBuilder.append("报警:" + alertsKey + "=true" + "\n");
                }
            }
        }

        // 已定义的设备故障数据点，设备发生故障后该字段有内容，没有发生故障则没内容
        if (dataMap.get("faults") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("faults");
            for (String faultsKey : map.keySet()) {
                if ((Boolean) map.get(faultsKey)) {
                    sBuilder.append("故障:" + faultsKey + "=true" + "\n");
                }
            }
        }

        if (sBuilder.length() > 0) {
            sBuilder.insert(0, "[设备故障或报警]\n");
            ToastUtil.makeText(this, sBuilder.toString().trim());
        }

        // 透传数据，无数据点定义，适合开发者自行定义协议自行解析
        if (dataMap.get("binary") != null) {
            byte[] binary = (byte[]) dataMap.get("binary");
            Log.i("", "Binary data:" + HexStrUtils.bytesToHexString(binary));
        }
    }



    /**
     *Description:显示格式化数值，保留对应分辨率的小数个数，比如传入参数（20.3656，0.01），将返回20.37
     *@param date 传入的数值
     *@param scale 保留多少位小数
     *@return
     */
    protected String formatValue(double date, Object scale) {
        if (scale instanceof Double) {
            DecimalFormat df = new DecimalFormat(scale.toString());
            return df.format(date);
        }
        return Math.round(date) + "";
    }
}
