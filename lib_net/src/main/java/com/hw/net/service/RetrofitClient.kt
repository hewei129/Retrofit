package com.hw.net.service


import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.hw.net.BuildConfig
import com.hw.net.bean.DeviceInfoBean
import com.hw.net.util.ContextUtil.getApplicationContext
import com.hw.net.util.DeviceInfo
import com.hw.net.util.SystemUtil
import com.tencent.mmkv.MMKV
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.File


/**
 * @author David
 * @date 2020/5/20  12:58
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */

open class RetrofitClient : BaseRetrofitClient() {


    private var context: Context = getApplicationContext()

    companion object {
       private var instance: RetrofitClient? = null
        fun getInstance(): RetrofitClient {
            if (instance == null) {
                synchronized(RetrofitClient::class.java) {
                    if (instance == null) {
                        instance = RetrofitClient()
                    }
                }
            }
            return instance!!
        }
        fun reset(){
            instance = null
        }
    }


    private var bean : DeviceInfoBean
    init {
         bean = DeviceInfoBean(SystemUtil.getIMEI(context),
            "Android",  android.os.Build.VERSION.RELEASE,
                    DeviceInfo.getPhoneModel(),
                    DeviceInfo.getAppVersionName(context),
            "app",
                    if (BuildConfig.DEBUG) "TEST" else "RELEASE")
    }
    private val cookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(context)
        )
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {//可自定义配置builder
        val httpCacheDirectory = File(context.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024L // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)
        builder.cache(cache)
            .cookieJar(cookieJar)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {

//                    val sharePrefrence = SharePrefrence()
                    val kv = MMKV.defaultMMKV()
                    var token = kv.decodeString("token")
                    var userId = kv.decodeString("user_id")
                    var tokenKey = kv.decodeString("token_key")
                    if(tokenKey.isNullOrEmpty()) tokenKey = "Authorization"
                    if (token.isNullOrEmpty()) token = ""
                    var authorization = token
                    if (authorization.isNullOrEmpty()) authorization = ""
                    if (userId.isNullOrEmpty()) userId = "0"

//                    val traceId =
//                        "Android" + "_" + bean.uuid + "_" + DeviceInfo.getStringToDate(DeviceInfo.dateToString()) + "_$userId"

                    val newRequest = chain.request().newBuilder()
                        .addHeader(tokenKey, authorization)
                        .addHeader("uuid", bean.uuid)
                        .addHeader("device", bean.device)
                        .addHeader("osVersion", bean.osVersion)
                        .addHeader("phoneType", bean.phoneType)
                        .addHeader("version", bean.version)
                        .addHeader("platform", bean.platform)
                        .addHeader("userId", userId.toString())
                        .build()
                    return chain.proceed(newRequest)

                }
            })
    }
}