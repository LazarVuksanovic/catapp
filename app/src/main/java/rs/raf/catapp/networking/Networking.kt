package rs.raf.catapp.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import rs.raf.catapp.networking.serialization.AppJson

/*
 * Order of okhttp interceptors is important. If logging was first,
 * it would not log the custom header.
 */

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor {
        val updatedRequest = it.request().newBuilder()
            .build()
        it.proceed(updatedRequest)
    }
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    )
    .build()


val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://rma.finlab.rs/")
    .client(okHttpClient)
    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
    .build()
