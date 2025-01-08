package com.umc.timeCAlling.di

import com.google.gson.GsonBuilder
import com.umc.timeCAlling.R
import com.umc.timeCAlling.TimeCAllingApplication
import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.data.datasourceImpl.TmapDataSourceImpl
import com.umc.timeCAlling.data.service.TmapService
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

// @Module: 모듈은 Hilt에게 특정 객체를 만드는 방법을 알려주는 클래스이다.
@Module
// @InstallIn : 해당 컴포넌트의 모듈이 설치 되게 한다.
//별도로 Scope를 지정해주면 해당하는 HiltComponent의 수명동안 같은 인스턴스를 공유해 바인딩이 요청될 때마다 같은 인스턴스를 제공할 수 있다.
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val NETWORK_EXCEPTION_OFFLINE_CASE = "network status is offline"
    const val NETWORK_EXCEPTION_BODY_IS_NULL = "result.json body is null"

    // @Provides : 모듈 클래스 내에 해당 객체 생성
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
    fun providesOkHttpClient(
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            addInterceptor(TmapInterceptor())
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
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
            .baseUrl("https://apis.openapi.sk.com/") // Tmap API 기본 URL
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providesTmapRouteDataSource(@Named("tmap") tmapService: TmapService): TmapDataSource {
        return TmapDataSourceImpl(tmapService)
    }
}