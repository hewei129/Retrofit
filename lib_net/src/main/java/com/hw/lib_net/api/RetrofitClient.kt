package com.hw.lib_net.api

import android.content.Context
import android.content.SharedPreferences
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.hw.lib_net.retrofit.BaseRetrofitClient
import com.hw.lib_net.util.ContextUtil
import com.hw.lib_net.util.ContextUtil.getApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException


/**
 * @author hewei(David)
 * @date 2019-09-21  14:49
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description  retrofit 实现类
 */

open class RetrofitClient(private val context: Context) : BaseRetrofitClient() {


    companion object {
        private var instance: RetrofitClient? = null
        fun getInstance(): RetrofitClient {
            if (instance == null) {
                synchronized(RetrofitClient::class.java) {
                    if (instance == null) {
                        instance = RetrofitClient(getApplicationContext())
                    }
                }
            }
            return instance!!
        }
    }

    private val cookieJar by lazy { PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context)) }
    override fun handleBuilder(builder: OkHttpClient.Builder) {//可自定义配置builder
        builder.cookieJar(cookieJar)
                .addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                                .addHeader("device", "Android")
                                .addHeader("osVersion", android.os.Build.VERSION.RELEASE)
                                .addHeader("platform", "app")
                                //cache for 30 days
                                .addHeader("Cache-Control", "max-age=" + 3600 * 24 * 30)
                                .build()
                        val response: Response
                        try {
                            response = chain.proceed(newRequest)
                        } catch (e: Exception) {
                            throw (e)
                        }
                        return response

                    }
                })


    }


}