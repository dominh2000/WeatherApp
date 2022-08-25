package com.example.weatherkotlin.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.weatherkotlin.const.BASE_META_WEATHER_URL
import com.example.weatherkotlin.const.BASE_OPEN_WEATHER_URL
import com.example.weatherkotlin.data.dataSources.database.ApplicationRoomDatabase
import com.example.weatherkotlin.data.dataSources.network.OpenWeatherApiServices
import com.example.weatherkotlin.data.dataSources.network.WeatherApiServices
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    val password = "Hello123"

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext ctx: Context
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(ctx).build())
            .build()

    @Provides
    @Singleton
    fun getDatabase(
        @ApplicationContext ctx: Context,
    ): ApplicationRoomDatabase =
        ApplicationRoomDatabase.getDatabase(
            ctx,
            password
        )

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun getMetaWeatherApiServices(
        moshi: Moshi,
        client: OkHttpClient
    ): WeatherApiServices =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_META_WEATHER_URL)
            .client(client)
            .build()
            .create(WeatherApiServices::class.java)

    @Provides
    @Singleton
    fun getOpenWeatherApiServices(
        moshi: Moshi,
        client: OkHttpClient
    ): OpenWeatherApiServices =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_OPEN_WEATHER_URL)
            .client(client)
            .build()
            .create(OpenWeatherApiServices::class.java)
}