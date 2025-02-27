package com.example.appinstagram.lib_base.utils
import android.content.Context


class BaseContext private constructor() {
    // Chỉ tạo ra context 1 lần
    private lateinit var mContext: Context
    fun init(context: Context) {
        mContext = context
    }

    fun getContext(): Context {
        return mContext
    }

    companion object {
        val instance = Singleton.holder
        object Singleton {
            val holder = BaseContext()
        }

    }

}