@file:Suppress("LABEL_NAME_CLASH")

package com.hw.net.service

import android.annotation.SuppressLint
import android.content.Context
import com.hw.net.util.NetUtils
import com.hw.net.util.ToastUtil
import com.hw.net.view.LoadingView
import kotlinx.coroutines.*

/**
 * @author David
 * @date 2020/5/20  12:58
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */

fun launch(
    block: suspend CoroutineScope.() -> Unit,
    isNeedNetError: Boolean,
    context: Context,
    job: Job
) {
    try {
        GlobalScope.launch(Dispatchers.Main + job) {
            val loadview = LoadingView(context)
            if (isNeedNetError) {
                if (!loadview.isShow())
                    loadview.show()

                if (!NetUtils.isNetworkReachable(context)) {
//                    if (ActivityController.instance?.currentActivity() == null) return@launch
//                    var bundle = ActivityController.instance?.currentActivity()?.intent?.extras
//                    if (bundle == null) bundle = Bundle()
//                    val tittle = ActivityController.instance?.currentActivity()?.title?.toString()
//                    if (!IsEmpty.string(tittle))
//                        bundle.putString(KeysUtils.KEY_Activity_tittle, tittle)
//                    val it = Intent(ActivityController.instance?.currentActivity(), NetWorkErrActivity::class.java)
//                    it.putExtras(bundle)
//                    it.putExtra(KeysUtils.KEY_FROM_ACTIVTY, ActivityController.instance?.currentActivity()!!.javaClass)
//                    ActivityController.jump(ActivityController.instance?.currentActivity()!!, it)
//                    ActivityController.instance?.currentActivity()?.finish()
                    loadview.dismiss()
                    return@launch
                }
            } else {
                if (!NetUtils.isNetworkReachable(context)) {
                    ToastUtil.getInstance().toasts("网络异常，请检查网络！")
                    loadview.dismiss()
                    return@launch
                }
            }
            tryCatch(block)
            loadview.dismiss()
        }
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
}

@SuppressLint("MissingPermission")
fun launch(block: suspend CoroutineScope.() -> Unit, context: Context, job: Job) {
    try {
        GlobalScope.launch(Dispatchers.Main + job) {
            if (!NetUtils.isNetworkReachable(context)) {
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
fun launch(block: suspend CoroutineScope.() -> Unit, context: Context) {
    try {
        GlobalScope.launch(Dispatchers.Main) {
            if (!NetUtils.isNetworkReachable(context)) {
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
            if (!NetUtils.isNetworkReachable(context)) {
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

