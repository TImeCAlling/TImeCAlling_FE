package com.umc.timeCAlling.di

import com.umc.timeCAlling.data.service.LoginService
import com.umc.timeCAlling.data.service.MypageService
import com.umc.timeCAlling.data.service.ScheduleService
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
    fun provideTmapService(@Named("tmap") retrofit: Retrofit): TmapService {
        return retrofit.create(TmapService::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleService(@Named("default")retrofit: Retrofit): ScheduleService{
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideLoginService(@Named("default") retrofit: Retrofit): LoginService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideMypageService(@Named("default") retrofit: Retrofit): MypageService {
        return retrofit.buildService()
    }
}