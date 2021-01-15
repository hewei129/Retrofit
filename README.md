# Retrofit
Retrofit+Coroutine+Okhttp 由kotlin编码封装的一套网络请求框架，支持跟随activity、fragment生命周期销毁而取消，支持显示loading加载框等. 
新增封装图片库Glide，支持加载带https前缀的网络图片地址等

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
	        implementation 'com.github.hewei129:Retrofit:2.2.8'
	}
	
Step 3. Attention：

	在application初始化时或者在调用网络之前的Activity中优先加载mmkv， 直接调用initMMKV(application) //因为库里依赖了MMKV库存储token等字段
	
Step 4. Set the baseUrl：
	
	initHost("主机地址")//包含http和端口等
	/**
	  setToken("")//登录成功后设置token， 支持自定义token的key（setToken(key, value)
	**/

Step 5. Create a Respository :

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
    
Step 6. How do it:
      
      用协程请求接口示例
       
       fun getVersionInfo(){
              launch ({
                  val result = withContext(Dispatchers.IO) {
                      TestRespository().getVersionInfo()
                  }
              executeResponse(result, 
	      		{
                      		result.data //正常返回成功后处理
                  	}, 
			{
				//异常报错处理
		  	})
              },context, true, job) //job = SupervisorJob()， ⚠️此处的job一般在VieModel或者BaseActivity中维护，具体使用可以参考事例MainActivity代码
          }
       
Step 7. How use it load image:
       
      注意：由于封装Glide用到了Antation, 需要在app 的build.gradle中配置：
      
      新增 id 'kotlin-kapt' 如下：
      
      plugins {
	    id 'com.android.application'
	    ..
	    id 'kotlin-kapt'
      }
      
      或者：
      
      apply plugin: 'com.android.application'
      apply plugin: 'kotlin-kapt'
      
      然后新增依赖库：
      
	dependencies {
		..
		kapt 'com.github.bumptech.glide:compiler:4.8.0'
	}
	
      最后调用代码：
      
      1. imgLoad(context, pic_url, iv_photo)
      2. imgLoad(context, pic_url, iv_photo, R.drawable.ic_default)
	
	
	

       
       

