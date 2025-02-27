package com.example.appinstagram.lib_base.base

import com.example.appinstagram.lib_base.utils.StatusBarKt
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * Create by liwen on 2020-05-18
 */
abstract class BaseActivity<T : ViewModel, M : ViewDataBinding>() : AppCompatActivity() {

    lateinit var mViewModel: T
    lateinit var mViewBinding: M

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarKt.fitSystemBar(this)

        mViewBinding = DataBindingUtil.setContentView(this, getLayoutResId())

        initViewModel()
        initData()
        initView()

    }

    abstract fun initData()

    abstract fun initView()

    abstract fun getLayoutResId(): Int


    @SuppressLint("NewApi")
    private fun initViewModel() {

        val types = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        mViewModel = ViewModelProvider(this).get<T>(types[0] as Class<T>)

//        val clazz: KClass<T> = ((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>).kotlin
//        mViewModel = getViewModel<T>(clazz)
//        mViewModel = getViewModel<T>()

    }


}