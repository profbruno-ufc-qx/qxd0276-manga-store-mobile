package br.com.brunomateus.mangastore.ui

import br.com.brunomateus.mangastore.data.Manga

sealed interface MangaListUiState {
    data object Loading : MangaListUiState
    data class Success(
        val mangas: List<Manga> = emptyList()
    ) : MangaListUiState
    data class Error(val message: String) : MangaListUiState
}
