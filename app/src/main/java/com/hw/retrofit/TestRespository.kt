package com.hw.retrofit

import android.util.Log
import com.hw.lib_net.api.ApiService
import com.hw.retrofit.models.response.HwResponse
import com.hw.retrofit.net_work.retrofit.apiCall
import kotlinx.coroutines.delay
import retrofit2.http.GET


/**
 * @author hewei(David)
 * @date 2019-09-23  15:00
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description  用协程请求接口示例
 *
 * fun getVersionInfo(){
        launch ({
            val result = withContext(Dispatchers.IO) {
                TestRespository().getVersionInfo()
            }
        executeResponse(result, {
                result.data
            }, {})
        },true)
    }
 */

class TestRespository : ApiService<TestRespository.Api>() {

    suspend fun getVersionInfo(): HwResponse<String> {
        return apiCall {
            delay(2000)
            Log.e("main", "66666")
            ApiClient().getVersionInfo()

        }
    }

    interface Api {
        @GET("/app/v1/versionApp/getVersionInfo")
        suspend fun getVersionInfo(): HwResponse<String>
    }

}