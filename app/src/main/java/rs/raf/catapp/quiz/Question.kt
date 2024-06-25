package rs.raf.catapp.quiz

import rs.raf.catapp.breeds.list.model.BreedUiModel


data class Question (
    val firstBreed: BreedUiModel,
    val secondBreed: BreedUiModel,
    val correctAnswer: Int,
    val text: String
)