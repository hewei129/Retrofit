package com.hw.net.service

import android.annotation.SuppressLint
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hw.net.BuildConfig
import com.hw.net.R
import com.hw.net.config.apiHost
import com.hw.net.config.getCustomInterceptor
import com.hw.net.exception.ApiException
import com.hw.net.model.BaseResult
import com.hw.net.util.ContextUtil
import com.hw.net.util.ToastUtil
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
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
        builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        builder.readTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
        builder.writeTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
        handleBuilder(builder)
        builder.addInterceptor(if (getCustomInterceptor())  MyErrorInterceptor() else ErrorInterceptor() )
//        builder.protocols(Collections.singletonList(Protocol.HTTP_1_1))
        return builder.build()
    }
    var isCustomInterceptor = false
    //自定义拦截器
    internal class MyErrorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response: Response
            try {
                response = chain.proceed(request)
                if (response.code >= 400) {
                    val json = response.body?.string()
                    val bean = Gson().fromJson<BaseResult>(json, object : TypeToken<BaseResult>() {}.type)
                    if(response.code == 403){
                        //token failed
//                        ToastUtil.getInstance().toasts(bean.message)
                        LiveEventBus.get("logout").post("")
                    }
                    if(bean.message.isNotEmpty()) {
                        throw ApiException(bean.message)
                    }

                }
            }catch (e: Exception) {
                e.printStackTrace()
                if(e.message.isNullOrEmpty())
                    throw ApiException("Connected Error!")
                else throw ApiException(e.message)
            }
            return response
        }
    }

    //标准拦截器
    internal class ErrorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val context = ContextUtil.getApplicationContext()
            val response: Response
            try {
                response = chain.proceed(request)
                when (response.code) {

                    403 -> {
                        throw ApiException(context.resources.getString(R.string.warn_request5))
                    }
                    404 -> {
                        throw ApiException(context.resources.getString(R.string.warn_request6))
                    }
                    500 -> {
                        throw ApiException(context.resources.getString(R.string.warn_request2))
                    }
                    503 -> {
                        throw ApiException(context.resources.getString(R.string.warn_request7))
                    }
                    else -> {
                        if (response.code > 300) {
                            val message = response.body?.string()
                            if (TextUtils.isEmpty(message)) {
                                throw ApiException(context.resources.getString(R.string.warn_request2))
                            } else {
                                throw ApiException(message)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                when {
                    e.message == null || e.message == "" -> throw Exception(context.resources.getString(R.string.warn_request4))
                    e is SocketTimeoutException -> throw SocketTimeoutException(context.resources.getString(R.string.warn_request3))
                    e is UnknownHostException -> throw UnknownHostException(context.resources.getString(R.string.warn_request1))
                    e is ConnectException -> throw ConnectException(context.resources.getString(R.string.warn_request1))
                    else -> {
                        if (e.message != null && e.message!!.contains("Failed to connect to "))
                            throw ApiException(context.resources.getString(R.string.warn_request1))
                        else throw ApiException(context.resources.getString(R.string.warn_request2))
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

    private var baseUrl = apiHost()
    fun getRetrofit(): Retrofit {
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