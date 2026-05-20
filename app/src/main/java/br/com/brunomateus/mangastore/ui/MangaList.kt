package br.com.brunomateus.mangastore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.brunomateus.mangastore.data.Manga
import coil3.compose.AsyncImage


@Composable
fun MangaList(
    navigateTo: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MangaViewModel = viewModel(factory = MangaViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
    ) {
        items(uiState.mangas) { manga ->
            MangaItem(
                manga = manga,
                navigateTo = navigateTo
            )

        }
    }
}

@Composable
fun MangaItem(manga: Manga, navigateTo: (Int) -> Unit, modifier: Modifier = Modifier) {

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        onClick = {
            navigateTo(manga.id)
        },
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = "http://10.0.2.2:1337${manga.cover.url}",
                contentDescription = "${manga.title}",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            BadgedBox(
                badge = {
                    Badge {
                        Text(
                            text = manga.number.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )


                    }
                },
                modifier = Modifier.offset(20.dp, 10.dp).align(Alignment.TopStart)
            ) {

            }
        }
    }


}

