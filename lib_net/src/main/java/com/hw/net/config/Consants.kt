package com.hw.net.config

import android.app.Application
import com.hw.net.BuildConfig
import com.hw.net.service.RetrofitClient
import com.tencent.mmkv.MMKV


/**
 * @author hewei(David)
 * @date 2020/6/10  3:27 PM
 * @Copyright ©  Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */
fun initMMKV(application: Application){
    MMKV.initialize(application)
}

fun apiHost(): String{
    val urlApi = MMKV.defaultMMKV().decodeString("url_api")
    return if(!urlApi.isNullOrEmpty()) urlApi
    else ""
//    else BuildConfig.API_HOST
}

fun initHost(apiUrl: String){
    RetrofitClient.reset()
    MMKV.defaultMMKV().encode("url_api", apiUrl)
}

fun setToken(token: String){
    MMKV.defaultMMKV().encode("token", token)
}
fun setToken(key: String, token: String){//支持自定义token的key
    MMKV.defaultMMKV().encode("token_key", key)
    MMKV.defaultMMKV().encode("token", token)
}

fun setUserId(userId: String){
    MMKV.defaultMMKV().encode("user_id", userId)
}

fun setCustomInterceptor(flag: Boolean){
    RetrofitClient.reset()
    MMKV.defaultMMKV().encode("is_custom_interceptor", flag)
}

fun getCustomInterceptor(): Boolean{
    return MMKV.defaultMMKV().decodeBool("is_custom_interceptor")
}

