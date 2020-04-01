package com.hw.retrofit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hw.lib_net.coroutine.launch
import com.hw.lib_net.retrofit.executeResponse
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
        create()
        setContentView(R.layout.activity_main)
        Log.e("main", "111111")
        launch ({
            val result = withContext(Dispatchers.IO) {
                TestRespository().getVersionInfo()
            }
//            delay(2000)
//            if(job?.isCancelled) return@launch
            Log.e("main", "333333")
            executeResponse(result, {
                result.data
                Log.e("main", "444444")
            }, {
                Log.e("main", "55555")})
        },this, true, job)
        Log.e("main", "222222")

        startActivity(Intent(this, SecondActivity::class.java))

    }

    lateinit var job: Job

    override fun onDestroy() {
        super.onDestroy()
        destroy()

    }
    fun create() {
        job = Job()
    }

    fun destroy() {
        job.cancel()
        Log.e("main", "cancel")
    }



}
