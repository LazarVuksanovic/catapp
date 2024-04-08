package rs.raf.catapp.breeds.repository

import kotlinx.coroutines.delay
import rs.raf.catapp.breeds.api.BreedsApi
import rs.raf.catapp.breeds.api.model.BreedApiModel
import rs.raf.catapp.breeds.domain.BreedData
import rs.raf.catapp.networking.retrofit
import kotlin.time.Duration.Companion.seconds


object BreedRepository {

    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

    private var breedsCached: List<BreedApiModel> = emptyList();

    suspend fun fetchAllBreeds(): List<BreedApiModel> {
        breedsCached = breedsApi.getAllBreeds()
        return breedsCached
    }

    fun fetchBreedDetails(breedId: String) : BreedApiModel? {
        return breedsCached.find { it.id == breedId }
    }
}
