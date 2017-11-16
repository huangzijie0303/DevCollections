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
    
     private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param previosTime
     * @return
     */
    public static String getFormerlyTime(String previosTime) {
        Long oldTime = Long.valueOf(previosTime);
        if (oldTime == 0) {
            return "";
        }
        long now = new Date().getTime();
        long diff = now - oldTime * 1000;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }


    /**
     * 获取小时
     * @return
     */
    public static int getNowHour() {
        int hour = 0;
        try {
            SimpleDateFormat formatHour = new SimpleDateFormat("HH");
            hour = Integer.valueOf(formatHour.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hour;
    }



    /**
     * 把标准格式yyyy-MM-dd HH:mm日期转成时间戳,单位秒
     * @param time
     * @return long
     */
    public static long getTimeToTimeStamp(String time) {
        long timeStamp = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(time);
            timeStamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp / 1000;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 将倒计时的时间格式化成分:秒"mm:ss"的格式
     * @param millisInfuture 剩余倒计时时长,单位:毫秒
     * @return
     */
    public static String formatCountTimer(long millisInfuture){
        String secondStr = "";
        String minStr = "";
        long remainTime = millisInfuture / 1000;
        long second = remainTime % 60;
        long min = remainTime / 60;
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = second + "";
        }

        if (min < 10) {
            minStr = "0" + min;
        } else {
            minStr = min + "";
        }
        return minStr+":"+secondStr;
    }

}
