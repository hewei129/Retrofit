package com.hw.net.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @author David
 * @date 2020/7/1
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */
object NetUtils {
    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    fun isNetworkReachable(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm == null) {
            return false
        } else {
            val info = cm.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }
}