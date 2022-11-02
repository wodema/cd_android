package com.dzq.coursedesign_android.utils

import android.os.CountDownTimer
import android.widget.TextView

class CountDownTimerUtil(
    private val textView: TextView,
    private val millisInFuture: Long,
    private val countDownInterval: Long
    ): CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisUntilFinished: Long) {
        textView.isClickable = false; //设置不可点击
        textView.text = "${millisUntilFinished / 1000}s";
    }

    override fun onFinish() {
        textView.text = "获取验证码";
        textView.isClickable = true;//重新获得点击
    }


}