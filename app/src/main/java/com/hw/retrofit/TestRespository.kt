package com.hw.retrofit

import com.hw.healthpass.net.models.response.LoginResponse
import com.hw.net.service.ApiService
import com.hw.net.service.apiCall
import retrofit2.http.Body
import retrofit2.http.POST


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

    //    suspend fun getVersionInfo(): HwResponse<String> {
//        return apiCall {
////            delay(2000)
////            Log.e("main", "66666")
//            ApiClient().getVersionInfo()
//
//        }
//    }
    suspend fun login(body: LoginRequest): LoginResponse {
        return apiCall {
            ApiClients().login(body)
        }
    }

    interface Api {
//        @GET("/app/v1/versionApp/getVersionInfo")
//        suspend fun getVersionInfo(): HwResponse<String>

        @POST("/api/v1/employees/login")
        suspend fun login(@Body body: LoginRequest): LoginResponse
    }

}