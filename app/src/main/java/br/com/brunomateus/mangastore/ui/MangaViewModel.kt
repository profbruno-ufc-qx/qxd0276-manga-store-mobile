package br.com.brunomateus.mangastore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunomateus.mangastore.data.Manga
import br.com.brunomateus.mangastore.data.MangaRepository
import br.com.brunomateus.mangastore.network.api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MangaUiState(
    val mangas: List<Manga> = emptyList(),
    val status: Boolean = false
)


class MangaViewModel(): ViewModel(){

    private val _uiState = MutableStateFlow(MangaUiState())
    val uiState = _uiState.asStateFlow()

    val respository = MangaRepository(api)

    init {
        loadMangas()
    }

    fun loadMangas() {
        viewModelScope.launch {
            val collection = respository.get()
            _uiState.update { current ->
                current.copy(
                    mangas = collection.data,
                    status = true
                )
            }
        }
    }
}