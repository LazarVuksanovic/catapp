package rs.raf.catapp.breeds.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import rs.raf.catapp.breeds.db.Breed
import rs.raf.catapp.breeds.list.model.BreedUiModel


@Serializable
data class BreedApiModel(
    val id: String,
    val name: String,
    @SerialName("alt_names") val altNames: String = "",
    val description: String,
    val origin: String,
    val temperament: String,
    @SerialName("life_span") val lifeSpan: String,
    val weight: Weight,
    val rare: Int,
    @SerialName("affection_level") val affectionLevel: Int,
    @SerialName("dog_friendly") val dogFriendly: Int,
    @SerialName("energy_level") val energyLevel: Int,
    @SerialName("shedding_level") val sheddingLevel: Int,
    @SerialName("child_friendly") val childFriendly: Int,
    val image: ImageApi? = null,
    @SerialName("wikipedia_url") val wikipediaUrl: String = "",
)

@Serializable
data class Weight(
    val imperial: String?,
    val metric: String?
)

@Serializable
data class ImageApi(
    val id: String,
    val width: Int?,
    val height: Int?,
    val url: String?
)

//fun BreedApiModel.asBreedUiModel() = BreedUiModel(
//    id = this.id,
//    name = this.name,
//    altNames = this.altNames,
//    origin = this.origin,
//    wikipediaUrl = this.wikipediaUrl,
//    description = this.description,
//    temperament = this.temperament,
////    image = this.image,
//    image = null,
//    lifeSpan = this.lifeSpan,
//    weight = this.weight.metric,
//    rare = this.rare,
//    affectionLevel = this.affectionLevel,
//    dogFriendly = this.dogFriendly,
//    energyLevel = this.energyLevel,
//    sheddingLevel = this.sheddingLevel,
//    childFriendly = this.childFriendly
//)
