package com.hw.lib_net.retrofit

import com.hw.lib_net.util.ToastUtil
import com.hw.retrofit.models.response.HwResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


/**
 * @author hewei(David)
 * @date 2019-09-23  16:17
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */

//class ResponseFilter<T>(private val isNeedSeverError: Boolean) {
suspend fun <T> executeResponse(response: T, successBlock: suspend CoroutineScope.() -> Unit, errorBlock: suspend CoroutineScope.() -> Unit, isNeedSeverError: Boolean) {
    coroutineScope {
        if (response is HwResponse<*>) {
            if (response.code == 500) {
                run {
                    if (isNeedSeverError) {//为了特定的页面弹服务器错误页面
                        successBlock()
                    }
                }
            }
        }
        executeResponse(response, successBlock, errorBlock)

    }
}

suspend fun <T> executeResponse(response: T, successBlock: suspend CoroutineScope.() -> Unit, errorBlock: suspend CoroutineScope.() -> Unit) {
    coroutineScope {
        if (response is HwResponse<*>) {
            if (response.code != 200) {
                when (response.code) {
                    1001 -> {
                        run {
                            //TOKEN失效或者您的账号已在其他设备登录, 退出登陆到登陆页面
                            ToastUtil.getInstance().toasts("账号登录失效,请重新登录！")
                            delay(500)
                        }
                    }
                    1008 -> {
                        run {
                            //TOKEN失效或者您的账号已在其他设备登录, 退出登陆到登陆页面
                            ToastUtil.getInstance().toasts("您的账号已在其他设备登录！")
                            delay(500)

                        }
                    }
                    4001 -> {
                        run {
                            //为了区分抢单接单失败的弹框提示
                            successBlock()
                        }
                    }
                    else -> {
                        run {
                            errorBlock()
                            if (response.msg.isNotEmpty())
                                ToastUtil.getInstance().toasts(response.msg[0])
                        }
                    }

                }

            } else {//正常成功返回需要的结果
                successBlock()
            }
        }

    }
}


//}