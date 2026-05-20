package br.com.brunomateus.mangastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import br.com.brunomateus.mangastore.data.Manga
import br.com.brunomateus.mangastore.ui.MangaViewModel
import br.com.brunomateus.mangastore.ui.theme.MangaStoreTheme
import coil3.compose.AsyncImage
import kotlinx.serialization.Serializable

@Serializable
data object MangaListKey : NavKey

@Serializable
data class MangaDetail(
    val id: Int
) : NavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangaStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier = Modifier) {

    val backStack = rememberNavBackStack(MangaListKey)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<MangaListKey> {
                MangaList(navigateTo = {
                    backStack.add(MangaDetail(it))
                })
            }
            entry<MangaDetail> {

                MangaDetailScreen(it.id)
            }
        },
        modifier = modifier
    )
}

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


@Composable
fun MangaDetailScreen(id: Int, modifier: Modifier = Modifier) {
    Card(modifier = Modifier.padding(10.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            AsyncImage(
//                model = "http://10.0.2.2:1337${manga.cover.url}",
//                contentDescription = "${manga.title}"
//            )
//            Text(text = manga.title)
//            Text(text = "${manga.price}")
//            Text(text = "${manga.number}")
            Text(text = "id do manga selecionado $id")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    MangaStoreTheme {
        Navigation()
    }
}