package com.example.commonres.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/28.
 */
public class DateUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getTomorrow() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        return sdf.format(date);
    }

    public static String getDayNextMonth() {
        Date date = new Date();//取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE + 1, 1);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取后天的日期
     * @return
     */
    public static String getAcquired(){
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 2);//把日期往后增加两天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推两天的结果
        return sdf.format(date);
    }


}
