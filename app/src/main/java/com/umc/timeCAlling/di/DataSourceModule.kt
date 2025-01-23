package com.umc.timeCAlling.di

import android.content.Context
import android.content.SharedPreferences
import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.data.datasourceImpl.ScheduleDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideScheduleDataSource(scheduleDataSourceImpl:ScheduleDataSourceImpl):ScheduleDataSource = scheduleDataSourceImpl
}