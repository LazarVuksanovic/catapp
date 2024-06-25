package rs.raf.catapp.breeds.db

import androidx.room.Embedded
import androidx.room.Relation
import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.images.db.Image

data class BreedWithImage(
    @Embedded val breed: Breed,

    @Relation(
        parentColumn = "image",
        entityColumn = "id",
    )
    val image: Image?
){
    fun asBreedUiModel(): BreedUiModel {
        return BreedUiModel(
            id = this.breed.id,
            name = this.breed.name,
            altNames = this.breed.altNames,
            origin = this.breed.origin,
            wikipediaUrl = this.breed.wikipediaUrl,
            description = this.breed.description,
            temperament = this.breed.temperament,
            image = this.image,
            lifeSpan = this.breed.lifeSpan,
            rare = this.breed.rare,
            affectionLevel = this.breed.affectionLevel,
            dogFriendly = this.breed.dogFriendly,
            energyLevel = this.breed.energyLevel,
            sheddingLevel = this.breed.sheddingLevel,
            childFriendly = this.breed.childFriendly,
            weight = this.breed.weight
        )
    }
}

