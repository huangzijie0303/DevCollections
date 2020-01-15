package com.zenchn.xzwy.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import com.zenchn.module.common.utils.LogUtils
import com.zenchn.xzwy.app.GlobalLifecycleObserver
import com.zenchn.xzwy.common.ConfigType
import com.zenchn.xzwy.model.local.configMapping
import java.util.*


object LanguageUtils {

//    private val LOCALE_CHINESE = Locale.SIMPLIFIED_CHINESE
//    private val LOCALE_TIBETAN = Locale("bo", "rCN")

    private val LANGUAGES = arrayOf(
            Locale.SIMPLIFIED_CHINESE,//中文
            Locale("bo", "rCN")//藏文
    )

    private var selectLanguageConf: Int by configMapping(ConfigType.LANGUAGE, 0)

    /**
     * 设置语言：如果之前有设置就遵循设置如果没设置过就跟随系统语言
     */
    fun applyLanguage(context: Context, position: Int, isRestart: Boolean = false) {
        val newLocale: Locale = LANGUAGES.getOrNull(position)
                ?: context.getCurrentLocale()
        val updateLocale = context.updateLocale(newLocale)
        if (updateLocale && isRestart) {
            //重启到MainActivity
            GlobalLifecycleObserver.restartApp()
        }
        //保存到sp
        selectLanguageConf = position
    }

    /**
     * 当系统语言发生改变的时候还是继续遵循用户设置的语言
     */
    fun attachBaseContext(context: Context): Context {
        val newLocale: Locale? = LANGUAGES.getOrNull(selectLanguageConf)
        if (newLocale != null) {
            context.setLanguage(newLocale)
        }
        return context
    }

    private fun Context.setLanguage(newLocale: Locale) {
        LogUtils.d("newLocale:${newLocale}")
        resources.configuration.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocale(newLocale)
                setLocales(LocaleList(newLocale))
                createConfigurationContext(this)
                Locale.setDefault(newLocale)
            } else {
                locale = newLocale
            }
            resources.updateConfiguration(this, resources.displayMetrics)
        }
    }

    //更新语言设置
    private fun Context.updateLocale(newLocale: Locale): Boolean {
        if (needUpdateLocale(newLocale)) {
            setLanguage(newLocale)
            return true
        }
        return false
    }


    /**
     * 获取当前的Locale
     * //7.0有多语言设置获取顶部的语言
     */
    private fun Context.getCurrentLocale(): Locale {
        return resources.configuration.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) locales.get(0)
            else locale
        }
    }


    /**
     * 判断需不需要更新
     */
    private fun Context.needUpdateLocale(newLocale: Locale?): Boolean {
        val currentLocale = getCurrentLocale()
        LogUtils.d("currentLocale:${currentLocale}")
        return newLocale != null && currentLocale != newLocale
    }

    val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            applyLanguage(activity, selectLanguageConf)
        }

        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityDestroyed(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {}

    }
}