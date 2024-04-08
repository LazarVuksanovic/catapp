package rs.raf.catapp.breeds.api

import retrofit2.http.GET
import retrofit2.http.Path
import rs.raf.catapp.breeds.api.model.BreedApiModel

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>
}