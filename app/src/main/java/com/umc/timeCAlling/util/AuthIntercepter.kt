package com.umc.timeCAlling.util

import android.content.SharedPreferences
import com.auth0.jwt.JWT
import com.umc.timeCAlling.data.datasource.LoginDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sharedPreferences.getString("jwt", "") ?: ""

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        Timber.d("AuthInterceptor - Request Headers:")

        return chain.proceed(request)
    }
}