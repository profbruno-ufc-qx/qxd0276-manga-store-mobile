package br.com.brunomateus.mangastore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunomateus.mangastore.data.MangaRepository
import br.com.brunomateus.mangastore.network.api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MangaViewModel(private val repository: MangaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MangaUiState>(MangaUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMangas()
    }

    fun loadMangas() {
        viewModelScope.launch {
            _uiState.value = MangaUiState.Loading
            _uiState.update {
                try {
                    val collection = repository.get()
                    MangaUiState.Success(mangas = collection.data)
                } catch (e: Exception) {
                    MangaUiState.Error("Erro ao carregar mangas: ${e.message}")
                }
            }

        }
    }

    fun loadMangas(id: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val currentMangas = when(currentState) {
                    is MangaUiState.Success -> currentState.mangas
                    else -> emptyList()
                }

                try {
                    val manga = repository.get(id)
                    MangaUiState.Success(
                        mangas = currentMangas,
                        selected = manga.data
                    )
                } catch (e: Exception) {
                    MangaUiState.Error("Erro ao carregar detalhes: ${e.message}")
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = MangaRepository(api)
                MangaViewModel(repository = repository)
            }
        }
    }
}
