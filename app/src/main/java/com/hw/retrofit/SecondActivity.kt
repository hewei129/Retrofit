package com.hw.retrofit

import android.app.Activity
import android.os.Bundle
import com.hw.net.glide.imgLoad
import kotlinx.android.synthetic.main.activity_second.*


/**
 * @author hewei(David)
 * @date 2020/3/25  10:55 AM
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description 测试带https前缀的图片地址
 */

class SecondActivity : Activity() {
    private val pic_url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.tukexw.com%2Fimg%2F88f553ad691dd61f.jpg&refer=http%3A%2F%2Fimg.tukexw.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613118834&t=6f982cc71d24ab1f390c640b54aaef4e"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        imgLoad(this, pic_url, iv_photo)
    }
}