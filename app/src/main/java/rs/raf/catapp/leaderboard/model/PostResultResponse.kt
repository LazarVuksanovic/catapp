package rs.raf.catapp.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class PostResultResponse(
    val result: Result,
    val ranking: Int,
)

@Serializable
data class Result(
    val category: Int,
    val nickname: String,
    val result: Double,
    val createdAt: Long
)