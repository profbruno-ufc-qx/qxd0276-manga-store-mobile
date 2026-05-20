package br.com.brunomateus.mangastore.data

import br.com.brunomateus.mangastore.network.CollectionDTO
import br.com.brunomateus.mangastore.network.Single
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MangaRepository(val api: HttpClient) {

    suspend fun get(): CollectionDTO<Manga> =
        api.get("/api/mangas") {
            parameter("populate", "cover")
        } .body()

    suspend fun get(id: Int) : Single<Manga> =
        api.get("/api/mangas/${id}") {
            parameter("populate", "cover")
        } .body()


}