package com.umc.timeCAlling.util

import com.umc.timeCAlling.R
import com.umc.timeCAlling.TimeCAllingApplication
import okhttp3.Interceptor
import okhttp3.Response

class TmapInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = with(chain){
        val newRequest = request().newBuilder()
            .addHeader("appKey", TimeCAllingApplication.getString(R.string.tmap_app_key))
            .addHeader("Content-Type", "application/json")
            .build()

        proceed(newRequest)
    }
}