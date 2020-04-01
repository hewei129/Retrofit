package com.hw.lib_net.api

import com.hw.lib_net.BuildConfig
import me.andydev.retrofit.lifecycle.RetrofitLifecycle
import java.lang.reflect.ParameterizedType
import java.util.concurrent.ConcurrentHashMap


/**
 * @author hewei(David)
 * @date 2019-09-23  13:40
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description 网络请求入口
 */

@Suppress("UNCHECKED_CAST")
abstract class ApiService<T> {

    private val sInterfaceImplementCache: MutableMap<Class<*>, Any?> = ConcurrentHashMap()
    private val apiHost = BuildConfig.API_HOST

    protected fun ApiClient(): T {

        val retrofit = RetrofitClient.getInstance().getRetrofit(apiHost, "")
        return retrofit.create(getType())
    }


    private fun getType(): Class<T> {
        val t = javaClass.genericSuperclass
        val p = (t as ParameterizedType).actualTypeArguments
        return p[0] as Class<T>
    }



}