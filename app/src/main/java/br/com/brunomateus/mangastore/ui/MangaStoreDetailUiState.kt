package br.com.brunomateus.mangastore.ui

import br.com.brunomateus.mangastore.data.Manga

sealed interface MangaDetailUiState {
    data object Loading : MangaDetailUiState
    data class Success(
        val selected: Manga? = null
    ) : MangaDetailUiState
    data class Error(val message: String) : MangaDetailUiState
}
