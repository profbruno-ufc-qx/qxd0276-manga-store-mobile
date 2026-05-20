package br.com.brunomateus.mangastore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunomateus.mangastore.data.Manga
import br.com.brunomateus.mangastore.data.MangaRepository
import br.com.brunomateus.mangastore.network.api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class MangaViewModel(private val repository: MangaRepository): ViewModel(){

    private val _uiState = MutableStateFlow(MangaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMangas()
    }

    fun loadMangas() {
        viewModelScope.launch {
            val collection = repository.get()
            _uiState.update { current ->
                current.copy(
                    mangas = collection.data,
                    status = true
                )
            }
        }
    }

    fun loadMangas(id: Int) {
        viewModelScope.launch {
            val manga = repository.get(id)
            _uiState.update { current ->
                current.copy(
                    selected = manga.data,
                    status = true
                )
            }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = MangaRepository(api)
                MangaViewModel(
                    repository = repository,
                )
            }
        }
    }
}