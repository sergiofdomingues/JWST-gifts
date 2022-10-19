package com.example.jwst_gifts.data.di.modules

import com.example.jwst_gifts.data.di.qualifiers.CustomInterceptor
import com.example.jwst_gifts.data.di.qualifiers.LogInterceptor
import com.example.jwst_gifts.data.remote.response.ErrorResponse
import com.example.jwst_gifts.data.remote.service.JWSTService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://api.jwstapi.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        @CustomInterceptor jWSTInterceptor: JWSTInterceptor,
        @LogInterceptor logInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor = logInterceptor)
            .addInterceptor(interceptor = jWSTInterceptor)
            .connectTimeout(30, SECONDS)
            .readTimeout(30, SECONDS)
            .callTimeout(30, SECONDS)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideJWSTService(retrofit: Retrofit.Builder): JWSTService =
        retrofit
            .build()
            .create(JWSTService::class.java)

    @Provides
    @Singleton
    @LogInterceptor
    fun provideLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        //if (BuildConfig.DEBUG) {
        // development build
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        //} else {
        // production build
        //    interceptor.level = HttpLoggingInterceptor.Level.BASIC
        //}
        return interceptor
    }

    @Provides
    @Singleton
    @CustomInterceptor
    fun provideJWSTInterceptor(): JWSTInterceptor = JWSTInterceptor()

    class JWSTInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("X-API-KEY", "a0ae2084-467b-4ece-9b7b-34ab3f54de30")
                .build()
            return chain.proceed(request)
        }
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideJsonErrorAdapter(moshi: Moshi): JsonAdapter<ErrorResponse> =
        moshi.adapter(ErrorResponse::class.java)
}