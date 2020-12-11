package com.hw.net.bean


/**
 * @author hewei(David)
 * @date 2020/6/5  10:08 AM
 * @Copyright ©  Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */

data class DeviceInfoBean(val uuid: String, val device: String,
                     val osVersion: String,
                     val phoneType: String,
                     val version: String,
                     val platform: String,
                     val X_Ca_Stage: String
)