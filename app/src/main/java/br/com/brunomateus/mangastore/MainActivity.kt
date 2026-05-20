package br.com.brunomateus.mangastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import br.com.brunomateus.mangastore.ui.MangaDetailScreen
import br.com.brunomateus.mangastore.ui.MangaList
import br.com.brunomateus.mangastore.ui.theme.MangaStoreTheme
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
                Navigation()
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
                MangaDetailScreen(
                    id = it.id,
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
                )
            }
        },
        modifier = modifier.fillMaxSize()
    )
}



@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    MangaStoreTheme {
        Navigation()
    }
}