package com.umc.timeCAlling.di

import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.data.repositoryImpl.TestRepositoryImpl
import com.umc.timeCAlling.data.repositoryImpl.TmapRepositoryImpl
import com.umc.timeCAlling.data.service.TestService
import com.umc.timeCAlling.domain.repository.TestRepository
import com.umc.timeCAlling.domain.repository.TmapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    // 스코프 애노테이션이 있음
    // 해당하는 Hilt 컴포넌트의 수명동안 매 요청에 동일 인스턴스를 반환
    // 다음의 경우 viewModel의 수명동안 동일 인스턴스를 반환
    @ViewModelScoped
    @Provides
    fun providesTestRepository(
        testService: TestService
    ): TestRepository = TestRepositoryImpl(testService)

    @Provides
    @ViewModelScoped
    fun providesTmapRouteRepository(tmapDataSource: TmapDataSource): TmapRepository {
        return TmapRepositoryImpl(tmapDataSource)
    }
}