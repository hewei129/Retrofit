package com.hw.lib_net.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.AnimationDrawable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.hw.lib_net.R


/**
 * @author hewei(David)
 * @date 2020-03-18  16:51
 * @Copyright ©  Shanghai Yejia Digital Technology Co., Ltd.
 * @description
 */

class LoadingView {
    private var mDialog: MyDialog? = null
    private var iv_route: ImageView? = null
    private var animationDrawable: AnimationDrawable? = null
    private var detail_tv: TextView? = null
    private var view: LinearLayout? = null
    private var context: Context?

    constructor(context: Context?) {
        this.context = context
        init()
    }

    private fun init() {
        if (context == null) return
        mDialog = MyDialog(context, R.style.myDialog)
        view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null) as LinearLayout
        iv_route = view?.findViewById(R.id.iv_route)
        mDialog?.setContentView(view!!)
        initAnim()
        mDialog?.setCanceledOnTouchOutside(false)
    }

    fun dismiss() {
        if (null != mDialog) {
            if (null != animationDrawable && animationDrawable!!.isRunning) animationDrawable!!.stop()
            if (!(context as Activity?)!!.isDestroyed) {
                mDialog?.dismiss()
            }
        }
    }

    private fun initAnim() {
        iv_route?.setBackgroundResource(R.drawable.loading)
        animationDrawable = iv_route?.background as AnimationDrawable
    }

    fun show() {
        if (null != mDialog && !mDialog?.isShowing!!) {
            if (null != animationDrawable && !animationDrawable!!.isRunning) animationDrawable!!.start()
            mDialog?.show()
        }
    }

    fun show(title: String?) {
        if (null != mDialog && !mDialog?.isShowing!!) {
            if (null != animationDrawable && !animationDrawable!!.isRunning) animationDrawable!!.start()
            mDialog?.show()
        }
        if (null != detail_tv) {
            detail_tv!!.text = title
        }
    }

    fun isShow(): Boolean {
        return mDialog != null && mDialog?.isShowing!!
    }


    internal class MyDialog : Dialog {
        constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context!!, cancelable, cancelListener) {}
        constructor(context: Context?) : super(context!!) {}
        constructor(context: Context?, themeResId: Int) : super(context!!, themeResId) {}

        override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
            return if (keyCode == KeyEvent.KEYCODE_HOME) {
                true
            } else super.onKeyDown(keyCode, event)
        }
    }

}
