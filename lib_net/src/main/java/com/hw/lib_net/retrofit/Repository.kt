package com.hw.retrofit.net_work.retrofit



/**
 * @author hewei(David)
 * @date 2019-09-21  13:57
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */
suspend fun <T : Any> apiCall(call: suspend () -> T): T {
    return call.invoke()
}