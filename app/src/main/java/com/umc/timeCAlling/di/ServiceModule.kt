package com.umc.timeCAlling.di

import com.umc.timeCAlling.data.service.LoginService
import com.umc.timeCAlling.data.service.TestService
import com.umc.timeCAlling.data.service.TmapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }

    @Provides
    @Singleton
    fun provideTestService(@Named("default") retrofit: Retrofit): TestService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    @Named("tmap")
    fun provideTmapRouteService(@Named("tmap") retrofit: Retrofit): TmapService {
        return retrofit.create(TmapService::class.java)
    }

    @Provides
    @Singleton
    @Named("kakaoLogin")
    fun provideKakaoLoginService(@Named("kakaoLogin") retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }
}