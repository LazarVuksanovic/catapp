package rs.raf.catapp.breeds.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import rs.raf.catapp.breeds.api.BreedsApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedsModule {
    @Provides
    @Singleton
    fun provideBreedsApi(retrofit: Retrofit): BreedsApi = retrofit.create()
}