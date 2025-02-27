package com.example.appinstagram.di

import com.example.appinstagram.repositories.MainRepositoryImpl
import com.example.appinstagram.repositories.LoginRepositoryImpl
import com.example.appinstagram.repositories.MainRepository
import org.koin.dsl.module

val RepositoryModule = module {
    listOf(
        single { LoginRepositoryImpl(get()) },
        single<MainRepository> { MainRepositoryImpl(get()) })
}