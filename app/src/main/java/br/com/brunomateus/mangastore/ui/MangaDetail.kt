package br.com.brunomateus.mangastore.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.brunomateus.mangastore.R
import br.com.brunomateus.mangastore.network.BASE_URL
import br.com.brunomateus.mangastore.ui.symbols.arrow_back
import br.com.brunomateus.mangastore.ui.symbols.attach_money
import br.com.brunomateus.mangastore.ui.symbols.numbers
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    id: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MangaViewModel = viewModel(factory = MangaViewModel.Factory)
) {

    viewModel.loadMangas(id)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val betterContentScale = when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> ContentScale.FillWidth
        else -> ContentScale.FillHeight
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = arrow_back,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is MangaUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MangaUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as MangaUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadMangas(id) }) {
                            Text("Tentar novamente")
                        }
                    }
                }
                is MangaUiState.Success -> {
                    val manga = (uiState as? MangaUiState.Success)?.selected
                    if (manga != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val offset = Offset(5.0f, 5.0f)
                            Box(
                                modifier = Modifier.weight(1.0f)
                            ) {
                                AsyncImage(
                                    model = "${BASE_URL}${manga.cover.url}",
                                    contentScale = betterContentScale,
                                    contentDescription = manga.title,
                                    modifier = Modifier
                                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                                        .drawWithContent {
                                            drawContent()
                                            drawRect(
                                                brush = Brush.verticalGradient(
                                                    0.7f to Color.Red, 1.0f to Color.Transparent
                                                ), blendMode = BlendMode.DstIn
                                            )
                                        }
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = manga.title,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.headlineMedium + TextStyle(
                                        shadow = Shadow(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            offset = offset,
                                            blurRadius = 3f
                                        )
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                        .align(Alignment.BottomStart)
                                )
                            }


                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                            ) {


                                Row(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AssistChip(
                                        onClick = { },
                                        label = { Text("Volume ${manga.number}") },
                                        leadingIcon = { Icon(imageVector = numbers, null, Modifier.size(18.dp)) }
                                    )
                                    AssistChip(
                                        onClick = { },
                                        label = { Text("R$ ${manga.price}") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = attach_money,
                                                null,
                                                Modifier.size(18.dp)
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            labelColor = MaterialTheme.colorScheme.primary,
                                            leadingIconContentColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }

                                Text(
                                    text = stringResource(R.string.summary),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = manga.summary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
