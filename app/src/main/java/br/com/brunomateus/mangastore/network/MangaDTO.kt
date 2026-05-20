package br.com.brunomateus.mangastore.network

import kotlinx.serialization.Serializable

@Serializable
data class CollectionDTO<T>(
    val data: List<T>
)

@Serializable
data class Single<T>(
    val data: T
)