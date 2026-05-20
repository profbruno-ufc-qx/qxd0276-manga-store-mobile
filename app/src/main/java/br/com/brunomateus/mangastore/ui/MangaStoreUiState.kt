package br.com.brunomateus.mangastore.ui

import br.com.brunomateus.mangastore.data.Manga

sealed interface MangaUiState {
    data object Loading : MangaUiState
    data class Success(
        val mangas: List<Manga> = emptyList(),
        val selected: Manga? = null
    ) : MangaUiState
    data class Error(val message: String) : MangaUiState
}
