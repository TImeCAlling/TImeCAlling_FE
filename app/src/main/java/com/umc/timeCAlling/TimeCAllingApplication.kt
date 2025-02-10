package com.umc.timeCAlling

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.v2.auth.BuildConfig
import com.umc.timeCAlling.util.network.NetworkConnectionChecker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// @HiltAndroidApp : Hilt 사용시 반드시 선행 되어야 하는 부분, 모든 의존성 주입의 시작점
@HiltAndroidApp
class TimeCAllingApplication : Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super<Application>.onCreate()
        context = applicationContext
        networkConnectionChecker = NetworkConnectionChecker(context)

        // SharedPreferences에서 Access Token, Refresh Token 불러오기
        loadAuthToken()

        // 카카오 SDK 초기화
        val kakaoAppKey = getString(R.string.kakao_app_key)
        KakaoSdk.init(context, kakaoAppKey)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun loadAuthToken() {
        val spf = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val accessToken = spf.getString("jwt", null)
        val refreshToken = spf.getString("refreshToken", null)

        Timber.d("앱 실행 시 Access Token: $accessToken")
        Timber.d("앱 실행 시 Refresh Token: $refreshToken")
    }

    override fun onStop(owner: LifecycleOwner) {
        networkConnectionChecker.unregister()
        super.onStop(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        networkConnectionChecker.register()
        super.onStart(owner)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getString(@StringRes stringResId: Int): String {
            return context.getString(stringResId)
        }

        private lateinit var networkConnectionChecker: NetworkConnectionChecker
        fun isOnline() = networkConnectionChecker.isOnline()
    }
}