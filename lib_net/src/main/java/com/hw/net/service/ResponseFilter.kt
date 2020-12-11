package com.hw.net.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.jeremyliao.liveeventbus.LiveEventBus
import com.hw.net.model.BaseResult
import com.hw.net.util.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


/**
 * @author David
 * @date 2020/5/20  12:58
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */
suspend fun <T> executeResponse(
    response: T,
    bean: Class<*>,
    successBlock: suspend CoroutineScope.() -> Unit,
    errorBlock: suspend CoroutineScope.() -> Unit
) {
    coroutineScope {
        if (response is BaseResult) {
//            response.code = -3000
            if (response.code != 200 && response.code != 0) {
                when (response.code) {
                    403 -> {
                        run {
                            //TOKEN失效或者您的账号已在其他设备登录, 退出登陆到登陆页面
                            ToastUtil.getInstance().toasts("Session invalid, please login again！")
                            delay(1000)
                            LiveEventBus.get("logout").post("")
                        }
                    }
                    else -> {
                        run {
                            errorBlock()
                            if(response.result.toString().isNotEmpty() && response.result.toString() != "null")
                                ToastUtil.getInstance().toasts(response.result.toString())
//                            if (response.message?.size ?: 0 > 0)
//                                ToastUtil.getInstance().toasts(response.message?.get(0)!!)
                        }
                    }

                }

            } else {//正常成功返回需要的结果
                response.result?.let {
                    if(response.result is LinkedTreeMap<*,*>){
                        val gson: Gson = GsonBuilder().enableComplexMapKeySerialization().create()
                        val jsonString: String = gson.toJson(response.result)
                        response.result = gson.fromJson(jsonString, bean)
                    }else if(response.result is List<*>){
                        val list = ArrayList<Any>()
                        for (i in (response.result as List<*>).indices){
                            val gson: Gson = GsonBuilder().enableComplexMapKeySerialization().create()
                            val jsonString: String = gson.toJson((response.result as List<*>)[i])
                            list.add(gson.fromJson(jsonString, bean))
                        }
                        response.result = list
                    }

                }

                successBlock()

            }
        }

    }
}

suspend fun <T> executeResponse(
    response: T,
    successBlock: suspend CoroutineScope.() -> Unit,
    errorBlock: suspend CoroutineScope.() -> Unit
) {
    coroutineScope {
        if (response is BaseResult) {
//            response.code = -3000
            if (response.code != 200) {
                when (response.code) {
                    403 -> {
                        run {
                            //TOKEN失效或者您的账号已在其他设备登录, 退出登陆到登陆页面
                            ToastUtil.getInstance().toasts("Session invalid, please login again！")
                            delay(1000)
                            LiveEventBus.get("logout").post("")
                        }
                    }
                    else -> {
                        run {
                            errorBlock()
                            if(response.result.toString().isNotEmpty() && response.result.toString() != "null")
                                ToastUtil.getInstance().toasts(response.result.toString())
//                            if (response.message?.size ?: 0 > 0)
//                                ToastUtil.getInstance().toasts(response.message?.get(0)!!)
                        }
                    }

                }

            } else {//正常成功返回需要的结果

                successBlock()

            }
        }

    }
}



