package com.hw.net.service

import android.annotation.SuppressLint
import android.text.TextUtils
import com.hw.net.BuildConfig
import com.hw.net.exception.ApiException
import com.tencent.mmkv.MMKV

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author David
 * @date 2020/7/1
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description    retrofit父类
 */

@Suppress("UNREACHABLE_CODE")
abstract class BaseRetrofitClient {

    companion object {
        internal const val CONNECT_TIME_OUT: Long = 10
        internal const val READ_WRITE_TIME_OUT: Long = 10
    }

    private val client: OkHttpClient
    get() {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        builder.addNetworkInterceptor(logging)
        builder.sslSocketFactory(getSSLSocketFactory(), object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        builder.hostnameVerifier(getHostnameVerifier())
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        builder.readTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
        builder.writeTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
        handleBuilder(builder)
        builder.addInterceptor(ErrorInterceptor())
        return builder.build()
    }

    //自定义拦截器
    internal class ErrorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response: Response
            try {
                response = chain.proceed(request)
//                val token = response.headers["Authorization"]
//                if(!token.isNullOrEmpty()) {
////                    Log.e("toekn", "token=$token")
//                    val kv = MMKV.defaultMMKV()
//                    kv.encode("token", token)
//                }

//                throw ApiException("服务器内部错误!")
//                throw Exception("Canceled!")
                when (response.code) {
                    403 -> {
                        throw ApiException("禁止访问!")
                    }
                    404 -> {
                        throw ApiException("链接错误")
                    }
                    500 -> {
                        throw ApiException("服务器内部错误!")
                    }
                    503 -> {
                        throw ApiException("服务器升级中!")
                    }
                    else -> {
                        if (response.code > 300) {
                            val message = response.body?.string()
                            if (TextUtils.isEmpty(message)) {
                                throw ApiException("服务器内部错误!")
                            } else {
                                throw ApiException(message)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                when {
                    e.message == null || e.message == "" -> throw Exception("请求失败，请重试!")
                    e is SocketTimeoutException -> throw SocketTimeoutException("连接超时，请检查网络设置稍后再试！")
                    e is UnknownHostException -> throw UnknownHostException("无法连接到服务器，请重试")
                    e is ConnectException -> throw ConnectException("无法连接到服务器，请重试!")
                    else -> {
                        if (e.message != null && e.message!!.contains("Failed to connect to "))
                            throw ApiException("无法连接到服务器，请重试!")
                        else throw ApiException("服务器内部错误!")
                    }
                }
            }
            return response


        }
    }

    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)//开放抽象方法，提供builder

    //获取这个SSLSocketFactory
    private fun getSSLSocketFactory(): SSLSocketFactory {
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, getTrustManager(), SecureRandom())
            return sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    //获取TrustManager
    private fun getTrustManager(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
    }

    //获取HostnameVerifier
    private fun getHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { s, _ -> baseUrl?.contains(s)!! }
    }

    private var baseUrl: String? = null
    fun getRetrofit(baseUrl: String): Retrofit {
        check(!empty(baseUrl)) { "baseUrl can not be null" }
        this.baseUrl = baseUrl
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    protected var token: String? = null
    fun getRetrofit(baseUrl: String, token: String?): Retrofit {
        check(!empty(baseUrl)) { "baseUrl can not be null" }
        this.baseUrl = baseUrl
        this.token = token
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    private fun empty(baseUrl: String?): Boolean {
        return baseUrl == null || baseUrl.isNullOrEmpty()
    }
}