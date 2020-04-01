package com.hw.retrofit.models.response


/**
 * @author hewei(David)
 * @date 2019-09-21  13:57
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */
data class HwResponse<T>(
    val `data`: T,
    val code: Int,
    val message: List<String>,
    val msg: List<String>
)