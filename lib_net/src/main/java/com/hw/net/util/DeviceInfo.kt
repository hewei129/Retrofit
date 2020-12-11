package com.hw.net.util

import android.content.Context
import android.os.Build
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author hewei(David)
 * @date 2020/6/5  9:39 AM
 * @Copyright ©  Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */

class DeviceInfo {
    companion object{
        /**
         * 获取当前手机系统版本号
         *
         * @return 系统版本号
         */
        fun getSystemVersion(): String? {
            return Build.VERSION.RELEASE
        }

        /**
         * 获取手机品牌
         *
         * @return
         */
        fun getPhoneBrand(): String? {
            return Build.BRAND
        }

        /**
         * 获取手机型号
         *
         * @return
         */
        fun getPhoneModel(): String {
            return Build.MODEL
        }


        /**
         * 返回当前程序版本名
         */
        fun getAppVersionName(context: Context): String {
            var versionName = ""
            try { // ---get the package info---
                val pm = context.packageManager
                val pi = pm.getPackageInfo(context.packageName, 0)
                versionName = pi.versionName
                //            versioncode = pi.versionCode;
                if (versionName == null || versionName.length <= 0) {
                    return ""
                }
            } catch (e: Exception) {
                Log.e("VersionInfo", "Exception", e)
            }
            return versionName
        }


        var dateTimeFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA)
        /*将字符串转为时间戳*/
        fun getStringToDate(time: String?): Long {
            var date = Date()
            try {
                date = dateTimeFormat.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date.time
        }
        fun dateToString(): String {
            return try {
                dateTimeFormat.format(Date())
            } catch (e: Exception) {
                ""
            }
        }
    }

}