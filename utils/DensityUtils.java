/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zenchn.farmhouseinspection.library.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 单位转换工具类
 *
 * @author hzj
 */
public class DensityUtils {

    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    public static final float SCALED_DENSITY = Resources.getSystem().getDisplayMetrics().scaledDensity;


    /**
     * 获取DisplayMetrics对象
     *
     * @return
     */
    public static DisplayMetrics getDisPlayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    /**
     * 获取屏幕的宽度（像素）
     *
     * @return
     */
    public static int getScreenWidth() {
        return getDisPlayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的高（像素）
     *
     * @return
     */
    public static int getScreenHeight() {
        return getDisPlayMetrics().heightPixels;
    }

    /**
     * dp转px，保证尺寸大小不变
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        return Math.round(dpValue * DENSITY);
    }

    /**
     * px转dp，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dp(float pxValue) {
        return Math.round(pxValue / DENSITY);
    }

    /**
     * px转sp，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        return Math.round(pxValue / SCALED_DENSITY);
    }

    /**
     * sp转px，保证尺寸大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        return Math.round(spValue * SCALED_DENSITY);
    }

    /**
     * get screen density
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.density;
    }


    /**
     * get screen density dpi
     *
     * @param context
     * @return
     */
    public static int getDensityDpi(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.densityDpi;
    }
    
    /**
     * is landscape
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * is portrait
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取状态栏高度——方法1
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
