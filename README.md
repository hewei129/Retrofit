# Retrofit
Retrofit+Coroutine+Okhttp 由kotlin编码封装的一套网络请求框架，支持跟随activity、fragment生命周期销毁而取消

使用说明
Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.hewei129:Retrofit:1.0.0'
	}
  

Step 3. Create a Respository :

    class TestRespository : ApiService<TestRespository.Api>() {

      suspend fun getVersionInfo(): HwResponse<String> {
          return apiCall {
              ApiClient().getVersionInfo()
          }
      }

      interface Api {
          @GET("/app/v1/versionApp/getVersionInfo")
          suspend fun getVersionInfo(): HwResponse<String>
      }
    }
    
Step 4. How do it:
      
      用协程请求接口示例
       
       fun getVersionInfo(){
              launch ({
                  val result = withContext(Dispatchers.IO) {
                      TestRespository().getVersionInfo()
                  }
              executeResponse(result, {
                      result.data
                  }, {})
              },true)
          }
       


