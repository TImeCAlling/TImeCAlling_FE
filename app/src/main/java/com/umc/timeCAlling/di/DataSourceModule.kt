package com.umc.timeCAlling.di

import android.content.Context
import android.content.SharedPreferences
import com.umc.timeCAlling.data.datasource.AlarmDataSource
import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.data.datasource.MypageDataSource
import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.data.datasourceImpl.AlarmDataSourceImpl
import com.umc.timeCAlling.data.datasourceImpl.LoginDataSourceImpl
import com.umc.timeCAlling.data.datasourceImpl.MypageDataSourceImpl
import com.umc.timeCAlling.data.datasourceImpl.ScheduleDataSourceImpl
import com.umc.timeCAlling.data.datasourceImpl.TmapDataSourceImpl
import com.umc.timeCAlling.data.service.TmapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideTmapDataSource(tmapDataSourceImpl: TmapDataSourceImpl): TmapDataSource = tmapDataSourceImpl

    @Provides
    @Singleton
    fun provideScheduleDataSource(scheduleDataSourceImpl:ScheduleDataSourceImpl):ScheduleDataSource = scheduleDataSourceImpl

    @Provides
    @Singleton
    fun provideLoginDataSource(loginDataSourceImpl: LoginDataSourceImpl):LoginDataSource = loginDataSourceImpl

    @Provides
    @Singleton
    fun provideMypageDataSource(mypageDataSourceImpl: MypageDataSourceImpl):MypageDataSource = mypageDataSourceImpl

    @Provides
    @Singleton
    fun provideAlarmDataSource(alarmDataSourceImpl: AlarmDataSourceImpl): AlarmDataSource = alarmDataSourceImpl
}