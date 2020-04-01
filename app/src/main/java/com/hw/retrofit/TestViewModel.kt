package com.hw.retrofit

import android.content.Context
import com.hw.lib_net.coroutine.launch
import com.hw.lib_net.retrofit.executeResponse
import com.hw.retrofit.TestRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author hewei(David)
 * @date 2020-03-23  16:28
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */

class TestViewModel {

    fun test(context: Context){
        launch ({
            val result = withContext(Dispatchers.IO) {
                TestRespository().getVersionInfo()
            }
            executeResponse(result, {
                result.data
            }, {})
        },context)
    }
}