@file:Suppress("LABEL_NAME_CLASH")

package com.hw.lib_net.coroutine

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.hw.lib_net.util.ToastUtil
import com.hw.lib_net.util.isNetworkReachable
import com.hw.lib_net.view.LoadingView
import kotlinx.coroutines.*


/**
 * @author hewei(David)
 * @date 2019-09-25  13:37
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */
@SuppressLint("MissingPermission")
fun launch(block: suspend CoroutineScope.() -> Unit, context: Context) {
    try {
        GlobalScope.launch(Dispatchers.Main) {
            if (!isNetworkReachable(context)) {
                ToastUtil.getInstance().toasts("网络异常，请检查网络！")
                return@launch
            }
            tryCatch(block)
        }
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
}


@SuppressLint("MissingPermission")
fun launch(block: suspend CoroutineScope.() -> Unit, context: Context, job: Job) {
    try {
        GlobalScope.launch(Dispatchers.Main + job) {
            if (!isNetworkReachable(context)) {
                ToastUtil.getInstance().toasts("网络异常，请检查网络！")
                return@launch
            }
            tryCatch(block)
        }
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
}


@SuppressLint("MissingPermission")
fun launch(block: suspend CoroutineScope.() -> Unit, context: Context, isNeedLoadingView: Boolean, job: Job) {
    try {
        GlobalScope.launch(Dispatchers.Main + job) {
            if (!isNetworkReachable(context)) {
                ToastUtil.getInstance().toasts("网络异常，请检查网络！")
                return@launch
            }
            if(isNeedLoadingView) {
                val loadview = LoadingView(context)
                if (!loadview.isShow())
                    loadview.show()
                tryCatch(block)
                loadview.dismiss()
            }else
                tryCatch(block)
        }
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
}


private suspend fun tryCatch(tryBlock: suspend CoroutineScope.() -> Unit) {
    coroutineScope {
        try {
            tryBlock()
        } catch (e: Throwable) {
            if(!e.message.toString().startsWith("Job"))
                ToastUtil.getInstance().toasts(e.message.toString())
        }
    }
}


