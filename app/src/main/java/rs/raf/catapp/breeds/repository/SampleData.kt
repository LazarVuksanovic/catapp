package rs.raf.catapp.breeds.repository

import rs.raf.catapp.breeds.api.model.BreedApiModel
import rs.raf.catapp.breeds.api.model.Image
import rs.raf.catapp.breeds.api.model.Weight
import rs.raf.catapp.breeds.domain.BreedData
import rs.raf.catapp.breeds.list.model.BreedUiModel

val weight = Weight("7  -  10", "3 - 5")

// Create an Image object
val image = Image("0XYvRd7oD", 1204, 1445, "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg")

//val SampleData = listOf(
//    BreedApiModel(
//        weight = weight,
//        id = "abys",
//        name = "Abyssinian",
//        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
//        origin = "Egypt",
//        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
//        lifeSpan = "14 - 15",
//        altNames = "",
//        affectionLevel = 5,
//        childFriendly = 3,
//        dogFriendly = 4,
//        energyLevel = 5,
//        sheddingLevel = 2,
//        rare = 0,
//        wikipediaUrl = "https://en.wikipedia.org/wiki/Abyssinian_(cat)",
//        image = image
//    ),
//)