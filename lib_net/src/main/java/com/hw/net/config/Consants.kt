package com.hw.net.config

import com.hw.net.BuildConfig
import com.tencent.mmkv.MMKV


/**
 * @author hewei(David)
 * @date 2020/6/10  3:27 PM
 * @Copyright ©  Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */

fun apiHost(): String{
    val urlApi = MMKV.defaultMMKV().decodeString("url_api")
    return if(!urlApi.isNullOrEmpty()) urlApi
    else BuildConfig.API_HOST
}

fun initHost(apiUrl: String){
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

