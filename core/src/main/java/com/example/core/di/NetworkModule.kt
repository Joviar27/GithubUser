package com.example.core.di

import com.example.core.BuildConfig
import com.example.core.data.remote.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TOKEN = BuildConfig.TOKEN
    private const val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @Singleton
    @Named(Type.AUTH_INTERCEPTOR)
    fun provideAuthInterceptor(): Interceptor =
        Interceptor{chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization", "token $TOKEN")
            chain.proceed(requestBuilder.build())
        }

    @Provides
    @Singleton
    @Named(Type.LOGGING_INTERCEPTOR)
    fun provideLoggingInterceptor(): Interceptor =
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named(Type.AUTH_INTERCEPTOR) authInterceptor: Interceptor,
        @Named(Type.LOGGING_INTERCEPTOR) loggingInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitApiService(
        client: OkHttpClient
    ): ApiService { val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    object Type{
        const val AUTH_INTERCEPTOR = "auth-interceptor"
        const val LOGGING_INTERCEPTOR = "loggin-interceptor"
    }
}