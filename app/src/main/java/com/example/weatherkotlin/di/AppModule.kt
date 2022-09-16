package com.example.weatherkotlin.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.weatherkotlin.R
import com.example.weatherkotlin.const.BASE_META_WEATHER_URL
import com.example.weatherkotlin.const.BASE_OPEN_WEATHER_URL
import com.example.weatherkotlin.data.dataSources.database.ApplicationRoomDatabase
import com.example.weatherkotlin.data.dataSources.database.OpenWeatherDao
import com.example.weatherkotlin.data.dataSources.database.TaskDao
import com.example.weatherkotlin.data.dataSources.database.WeatherDao
import com.example.weatherkotlin.data.dataSources.network.OpenWeatherApiServices
import com.example.weatherkotlin.data.dataSources.network.WeatherApiServices
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @SqlCipherPasswordString
    @Provides
    @Singleton
    fun provideSQLCipherPassword(
        @ApplicationContext ctx: Context
    ): String =
        ctx.resources.getString(R.string.sqlite_password)

    @OpenWeatherApiKeyString
    @Provides
    @Singleton
    fun provideOpenWeatherApiKey(
        @ApplicationContext ctx: Context
    ): String =
        ctx.resources.getString(R.string.open_weather_api_key)

    @Provides
    @Singleton
    fun getDatabase(
        @ApplicationContext ctx: Context,
        @SqlCipherPasswordString sqlCipherPassword: String
    ): ApplicationRoomDatabase =
        ApplicationRoomDatabase.getDatabase(
            ctx,
            sqlCipherPassword
        )

    @Provides
    @Singleton
    fun provideMetaWeatherDao(
        database: ApplicationRoomDatabase
    ): WeatherDao =
        database.weatherDao()

    @Provides
    @Singleton
    fun provideOpenWeatherDao(
        database: ApplicationRoomDatabase
    ): OpenWeatherDao =
        database.openWeatherDao()

    @Provides
    @Singleton
    fun provideTaskDao(
        database: ApplicationRoomDatabase
    ): TaskDao =
        database.taskDao()

    @Provides
    @Singleton
    fun provideInterceptor(
        @OpenWeatherApiKeyString apiKey: String
    ): Interceptor =
        Interceptor { chain ->
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("appid", apiKey)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext ctx: Context,
        interceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(ChuckerInterceptor.Builder(ctx).build())
            .build()

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