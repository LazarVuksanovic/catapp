package rs.raf.catapp.breeds.list.model

import kotlinx.serialization.SerialName
import rs.raf.catapp.breeds.api.model.Image
import rs.raf.catapp.breeds.api.model.Weight

data class BreedUiModel(
    val id: String = "",
    val name: String = "",
    val altNames: String,
    val origin: String = "",
    val wikipediaUrl: String = "",
    val description: String = "",
    val temperament: String,
    val image: Image?,
    val lifeSpan: String,
    val weight: Weight,
    val rare: Int,
    val affectionLevel: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val sheddingLevel: Int,
    val childFriendly: Int,
)