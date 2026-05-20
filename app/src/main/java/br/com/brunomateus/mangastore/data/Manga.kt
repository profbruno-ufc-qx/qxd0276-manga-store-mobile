package br.com.brunomateus.mangastore.data

import kotlinx.serialization.Serializable

@Serializable
data class Manga(
    val id: Int,
    val number: Int,
    val title: String,
    //val cover: String,
    val price: Double
)
