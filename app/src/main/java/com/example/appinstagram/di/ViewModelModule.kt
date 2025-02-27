package com.example.appinstagram.di

import com.example.appinstagram.viewmodel.LoginViewModel
import com.example.appinstagram.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    listOf(
        viewModel { LoginViewModel(get()) },
        viewModel { MainViewModel(get()) }
    )
}