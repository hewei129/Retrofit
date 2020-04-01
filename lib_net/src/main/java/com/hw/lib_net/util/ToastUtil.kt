package com.hw.lib_net.util

import android.content.Context
import android.widget.Toast


/**
 * @author hewei(David)
 * @date 2020-03-23  15:12
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */


class ToastUtil {
    companion object{
        private var toastUtil: ToastUtil ?= null
        fun getInstance(): ToastUtil{
            if(null == toastUtil)
                toastUtil = ToastUtil()
            return toastUtil as ToastUtil
        }
    }

    var context :Context ?= null

    init {
        context = ContextUtil.getApplicationContext()
    }

    fun toasts(txt: String){
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show()
    }
}

