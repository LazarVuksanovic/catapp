package rs.raf.catapp.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.images.db.Image

@Entity
data class Breed(
    @PrimaryKey
    val id: String,
    val name: String,
    val origin: String,
    val wikipediaUrl: String,
    val description: String,
    val sheddingLevel: Int,
    val dogFriendly: Int,
    val energyLevel: Int,
    val childFriendly: Int,
    val affectionLevel: Int,
    val lifeSpan: String,
    val altNames: String,
    val image: String?,
    val temperament: String,
    val rare: Int,
    val weight: String?,
) {
    fun asBreedUiModel(): BreedUiModel {
        return BreedUiModel(
            id = this.id,
            name = this.name,
            altNames = this.altNames,
            origin = this.origin,
            wikipediaUrl = this.wikipediaUrl,
            description = this.description,
            temperament = this.temperament,
            image = null,
            lifeSpan = this.lifeSpan,
            rare = this.rare,
            affectionLevel = this.affectionLevel,
            dogFriendly = this.dogFriendly,
            energyLevel = this.energyLevel,
            sheddingLevel = this.sheddingLevel,
            childFriendly = this.childFriendly,
            weight = this.weight
        )
    }
}