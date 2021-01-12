package com.hw.retrofit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hw.net.config.initHost
import com.hw.net.config.initMMKV
import com.hw.net.config.setCustomInterceptor
import com.hw.net.service.executeResponse
import com.hw.net.service.launch
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*


/**
 * @author hewei(David)
 * @date 2020-03-23  16:46
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMMKV(application)
        create()
        setContentView(R.layout.activity_main)
        Log.e("main", "111111")
        setCustomInterceptor(true)
        initHost("https://172.16.12.150")//第一次设置初始化主机地址
//        setToken("")//登录成功后设置token
        launch({
            val result = withContext(Dispatchers.IO) {
                TestRespository().login(LoginRequest("hewei@xinke86.com", "123456"))
            }
            Log.e("main", "333333")
            executeResponse(result, {
                Log.e("result=",  result.Authentication)

                delay(2000)
                Log.e("main", "444444")
            }, {
                delay(2000)

                Log.e("main", "55555")
            })
        }, this, true, job)
        Log.e("main", "222222")

        startActivity(Intent(this, SecondActivity::class.java))

    }

    lateinit var job: Job
    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()

    }

    fun create() {
        job = SupervisorJob()
    }

    fun destroy() {
        job.cancel()
        Log.e("main", "cancel")
    }


}
