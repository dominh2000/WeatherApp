package com.example.weatherkotlin.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SqlCipherPasswordString

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenWeatherApiKeyString