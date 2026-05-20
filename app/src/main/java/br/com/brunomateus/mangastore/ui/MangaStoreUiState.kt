package br.com.brunomateus.mangastore.ui

import br.com.brunomateus.mangastore.data.Manga

data class MangaUiState(
    val mangas: List<Manga> = emptyList(),
    val selected: Manga? = null,
    val status: Boolean = false
)
