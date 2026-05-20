package br.com.brunomateus.mangastore.data

import kotlinx.serialization.Serializable

@Serializable
data class Manga(
    val id: Int,
    val number: Int,
    val title: String,
    val summary: String,
    val cover: Cover,
    val price: Double
)


@Serializable
data class Cover(
    val url: String
)