package com.zenchn.houseinspecthelper.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresPermission;

import static android.Manifest.permission.VIBRATE;

/**
 * @author:Hzj
 * @date :2019/9/26/026
 * desc  ： 震动工具类
 * record：
 */
public class VibrateUtils {

    private static volatile Vibrator sVibrator;

    private VibrateUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 一次性震动
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     *
     * @param milliseconds The number of milliseconds to vibrate.
     */
    @RequiresPermission(VIBRATE)
    public static void vibrate(final long milliseconds, Context context) {
        Vibrator vibrator = getVibrator(context);
        if (vibrator == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(vibrationEffect);
        } else {
            vibrator.vibrate(milliseconds);
        }
    }

    /**
     * 创建波形振动
     *
     * @param pattern 交替开关定时的模式，从关闭开始。第一个值表示在打开振动器之前要等待的毫秒数,下一个值表示在关闭振动器之前保持振动器的毫秒数，随后的值交替执行。
     *  * @param repeat 振动重复的模式0的定时值将导致定时/振幅对被忽略。
     * @param repeat  振动重复的模式，如果您不想重复，则将索引放入计时数组中重复，或者-1。
     *                -1 为不重复
     *                0 为一直重复振动
     *                1 则是指从数组中下标为1的地方开始重复振动，重复振动之后结束
     *                2 从数组中下标为2的地方开始重复振动，重复振动之后结束
     */
    @RequiresPermission(VIBRATE)
    public static void vibrate(final long[] pattern, final int repeat, Context context) {
        Vibrator vibrator = getVibrator(context);
        if (vibrator == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, repeat);
            vibrator.vibrate(vibrationEffect);
        } else {
            vibrator.vibrate(pattern, repeat);
        }
    }

    /**
     * Cancel vibrate.
     * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
     */
    @RequiresPermission(VIBRATE)
    public static void cancel() {
        if (sVibrator != null) {
            sVibrator.cancel();
        }
    }

    private static Vibrator getVibrator(Context applicationContext) {
        if (sVibrator == null) {
            sVibrator = (Vibrator) applicationContext.getSystemService(Context.VIBRATOR_SERVICE);
        }
        return sVibrator;
    }
}
