package com.severo.challenge.framework.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    @Suppress("MagicNumber")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url

        return chain.proceed(
            request.newBuilder()
                .url(requestUrl)
                .build()
        )
    }
}