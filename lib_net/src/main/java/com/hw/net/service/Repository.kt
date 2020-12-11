package com.hw.net.service


/**
 * @author David
 * @date 2020/5/20  12:58
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */
suspend fun <T : Any> apiCall(call: suspend () -> T): T {
    return call.invoke()
}