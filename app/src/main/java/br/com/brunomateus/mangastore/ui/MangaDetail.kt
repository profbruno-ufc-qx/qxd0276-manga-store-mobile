package br.com.brunomateus.mangastore.ui


import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.test.attach_money
import com.example.test.numbers

@Composable
fun MangaDetailScreen(id: Int, modifier: Modifier = Modifier, viewModel: MangaViewModel = viewModel(factory = MangaViewModel.Factory)) {

    viewModel.loadMangas(id)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val manga = uiState.selected
    val configuration = LocalConfiguration.current
    val betterContentScale = when(configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> ContentScale.FillWidth
        else -> ContentScale.FillHeight
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(manga != null ) {
            AsyncImage(
                model = "http://10.0.2.2:1337${manga.cover.url}",
                contentScale = betterContentScale,
                contentDescription = "${manga.title}",
                modifier = Modifier
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent {
                        drawContent()
                        drawRect(brush = Brush.verticalGradient(
                            0f to Color.Transparent, 0.2f to Color.Red,
                            0.7f to Color.Red, 1.0f to Color.Transparent
                        ), blendMode = BlendMode.DstIn)
                    }.weight(1.0f)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1.0f).fillMaxWidth().padding(horizontal = 10.dp)
            ) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row() {

                    Icon(imageVector = numbers,
                        contentDescription = "Icon to indicate a number",
                        tint = Color.Gray
                    )
                    Text(
                        text = "${manga.number}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray

                    )
                }
                Row() {

                    Icon(imageVector = attach_money,
                        contentDescription = "Icon to indicate a price",
                        tint = Color.Gray
                    )
                    Text(
                        text = "${manga.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray

                    )
                }
                Text(
                    text = "Sumário",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = manga.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )


            }
        }
    }


}