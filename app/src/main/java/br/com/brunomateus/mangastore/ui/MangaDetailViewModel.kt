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


class MangaDetailViewModel(private val repository: MangaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MangaDetailUiState>(MangaDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()


    fun loadMangas(id: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                try {
                    val manga = repository.get(id)
                    MangaDetailUiState.Success(
                        selected = manga.data
                    )
                } catch (e: Exception) {
                    MangaDetailUiState.Error("Erro ao carregar detalhes: ${e.message}")
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = MangaRepository(api)
                MangaDetailViewModel(repository = repository)
            }
        }
    }
}
