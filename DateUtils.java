package com.xinhuamm.sdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * Created by Xinyi on 2016/9/11/011.
 */

public class DateUtils {
    /**
     * 校验时间是否早于当前
     *
     * @return
     */
    public static boolean checkTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");    //设置时间格式
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = sdf.format(curDate);
        Date d1 = null;
        try {
            d1 = sdf.parse(str);//当前时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = sdf.parse(time);//选择的时间
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long del = d2.getTime() - d1.getTime();
//        ToastUtils.showShort(getActivity(), "checkTime: " + d2.getTime() + "相差值" + del);
//        Log.d("test", "选择的时间: " + d2.getTime() +"当前时间"+d1.getTime()+ "相差值" + del+"========curDate"+curDate+"str"+str);
        if (del > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getCurrentTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");    //设置时间格式
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return sdf.format(curDate);
    }

    /**
     * 获取系统当前时间之后几分钟的时间
     *@param minuteAfter 当前时间之后分钟数
     * @return
     */
    public static String getCurrentTimeStringAfter(int minuteAfter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");    //设置时间格式
        Date curDate = new Date(System.currentTimeMillis()+minuteAfter*60*1000);//获取当前时间之后几分钟的时间
        return sdf.format(curDate);
    }


    /**
     * 把string类型的时间转成long类型
     *
     * @param timestr
     * @return
     */
    public static long parseTimeStringToLong(String timestr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(timestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 把long类型时间格式化成yyyy-MM-dd HH:mm形式
     *
     * @param timeLong
     * @return
     */
    public static String parseTimeLongToString(long timeLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeFormat = sdf.format(new Date(timeLong));
        return timeFormat;
    }

    public static String getYearMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(new Date());
    }

    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());

    }

    /**
     * 把long类型时长格式化成分秒
     *
     * @param duration
     * @return
     */
    public static String parseLength(long duration) {
        String time = "";

        long minute = duration / 60000;
        long seconds = duration % 60000;

        long second = Math.round((float) seconds / 1000);

        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";

        if (second < 10) {
            time += "0";
        }
        time += second;

        return time;
    }

}
