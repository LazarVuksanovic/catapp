package rs.raf.catapp.images.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import rs.raf.catapp.images.api.ImageApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun provideImagesApi(retrofit: Retrofit): ImageApi = retrofit.create()
}