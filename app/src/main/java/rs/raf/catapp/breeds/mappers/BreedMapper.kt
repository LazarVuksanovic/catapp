package rs.raf.catapp.breeds.mappers

import rs.raf.catapp.breeds.api.model.BreedApiModel
import rs.raf.catapp.breeds.db.Breed
import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.images.db.Image
import rs.raf.catapp.images.repository.ImageRepository

fun BreedApiModel.asBreedDbModel(): Breed {
    return Breed(
        id = this.id,
        name = this.name,
        origin = this.origin,
        wikipediaUrl = this.id,
        description = this.description,
        affectionLevel = this.affectionLevel,
        sheddingLevel = this.sheddingLevel,
        childFriendly = this.childFriendly,
        lifeSpan = this.lifeSpan,
        dogFriendly = this.dogFriendly,
        energyLevel = this.energyLevel,
        altNames = this.altNames,
        image = this.image?.id,
        rare = this.rare,
        temperament = this.temperament,
        weight = this.weight.metric,
    )
}