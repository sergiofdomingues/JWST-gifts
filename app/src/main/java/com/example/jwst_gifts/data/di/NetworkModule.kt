package com.example.jwst_gifts.data.di

import com.example.jwst_gifts.data.network.service.JWSTService
import com.example.jwst_gifts.data.network.response.ErrorResponse
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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
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
    fun provideOkhttpClient(interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor = interceptor)
            .addInterceptor(interceptor = JWSTInterceptor())
            .connectTimeout(30, SECONDS)
            .readTimeout(30, SECONDS)
            .callTimeout(30, SECONDS)

        return builder.build()
    }

    @Provides
    fun provideJWSTService(retrofit: Retrofit.Builder): JWSTService =
        retrofit
            .build()
            .create(JWSTService::class.java)

    @Provides
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