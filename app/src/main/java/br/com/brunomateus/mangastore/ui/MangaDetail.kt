package br.com.brunomateus.mangastore.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun MangaDetailScreen(id: Int, modifier: Modifier = Modifier, viewModel: MangaViewModel = viewModel(factory = MangaViewModel.Factory)) {

    viewModel.loadMangas(id)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Card(modifier = Modifier.padding(10.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val manga = uiState.selected
            if(manga != null ) {
                AsyncImage(
                    model = "http://10.0.2.2:1337${manga.cover.url}",
                    contentDescription = "${manga.title}"
                )
                Text(text = manga.title)
                Text(text = "${manga.price}")
                Text(text = "${manga.number}")
                Text(text = "id do manga selecionado $id")
            }
        }

    }
}