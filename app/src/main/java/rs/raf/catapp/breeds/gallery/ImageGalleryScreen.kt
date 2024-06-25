package rs.raf.catapp.breeds.gallery

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage

fun NavGraphBuilder.photoGallery(
    route: String,
    onClose: () -> Unit,
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(
    route = route,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
    arguments = arguments,
) { navBackStackEntry ->

    val photoGalleryViewModel = hiltViewModel<PhotoGalleryViewModel>(navBackStackEntry)

    val state = photoGalleryViewModel.state.collectAsState()

    PhotoGalleryScreen(
        state = state.value,
        onClose = onClose,
    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(
    state: BreedGalleryState,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            state.images.size
        }
    )

    var currentTitle by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentTitle,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            if (state.images.isNotEmpty()) {
                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect {
                        currentTitle = "Gallery"
                    }
                }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = { state.images[it].id },
                ) { pageIndex ->
                    val photo = state.images[pageIndex]
                    PhotoPreview(
                        modifier = Modifier,
                        url = photo.url,
                    )
                }

            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No albums.",
                )
            }
        },
    )
}

@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Composable
fun PhotoPreview(
    modifier: Modifier,
    url: String?,
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = url,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                    )
                }

            },
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
    }
}