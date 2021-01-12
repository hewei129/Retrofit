package com.hw.healthpass.net.models.response


/**
 * @author hewei(David)
 * @date 2020/12/11  10:14 AM
 * @Copyright ©  Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */

open class BaseResponse(){
    /**
     * 状态码
     */
    var code = 0
    var result: Any ?= null
    val message: String? = null
}