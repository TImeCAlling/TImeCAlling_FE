package com.umc.timeCAlling.di

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.umc.timeCAlling.R
import com.umc.timeCAlling.TimeCAllingApplication
import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.util.AuthInterceptor
import com.umc.timeCAlling.util.TmapInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import dagger.Lazy

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val NETWORK_EXCEPTION_OFFLINE_CASE = "network status is offline"
    const val NETWORK_EXCEPTION_BODY_IS_NULL = "result.json body is null"

    @Provides
    @Singleton
    fun providesConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder()
                .setLenient()
                .create()
        )
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor(
        sharedPreferences: SharedPreferences,
        loginDataSource: LoginDataSource
    ): AuthInterceptor {
        return AuthInterceptor(sharedPreferences, loginDataSource)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        authInterceptor: Lazy<AuthInterceptor>
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().apply {
            addInterceptor { chain -> authInterceptor.get().intercept(chain) }
            addInterceptor(loggingInterceptor)
            addInterceptor(TmapInterceptor())
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    @Named("default")
    fun providesRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TimeCAllingApplication.getString(R.string.base_url))
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("tmap")
    fun providesTmapRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://apis.openapi.sk.com/")
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }
}
