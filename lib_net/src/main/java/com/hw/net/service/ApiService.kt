package com.hw.net.service


import com.hw.net.config.apiHost
import java.lang.reflect.ParameterizedType


/**
 * @author David
 * @date 2020/5/20  12:58
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description 网络请求入口
 */

abstract class ApiService<T>  {

    private var baseUrl = apiHost()

    protected fun ApiClients(): T {
        return RetrofitClient.getInstance().getRetrofit(baseUrl)
            .create(getType())
    }

    private fun getType(): Class<T> {
        val t = javaClass.genericSuperclass
        val p = (t as ParameterizedType).actualTypeArguments
        return p[0] as Class<T>
    }




}