package rs.raf.catapp.images.api

import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.catapp.images.api.model.ImageApiModel

interface ImageApi {
    @GET("images/search")
    suspend fun getAllBreedImages(
        @Query("breed_ids") breedId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10
    ): List<ImageApiModel>
}