package rs.raf.catapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rs.raf.catapp.breeds.details.breedDetails
import rs.raf.catapp.breeds.list.breedsListScreen
import androidx.lifecycle.SavedStateHandle
import com.raf.catalist.users.details.userEdit
import rs.raf.catapp.breeds.gallery.photoGallery
import rs.raf.catapp.breeds.grid.breedImagesGrid
import rs.raf.catapp.leaderboard.leaderboardScreen
import rs.raf.catapp.quiz.quizHome
import rs.raf.catapp.quiz.quizQuestion
import rs.raf.catapp.users.details.userDetails
import rs.raf.catapp.users.login.userLogin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login",
    ){
        userLogin(
            route = "login",
            navController = navController,
        )

        userDetails(
            route = "userDetails",
            navController = navController,
        )

        userEdit(
            route = "userEdit",
            navController = navController,
        )

        quizHome(
            route = "quizHome",
            navController = navController,
        )

        quizQuestion(
            route = "quizQuestion",
            navController = navController,
        )

        leaderboardScreen(
            route = "leaderboardScreen",
            navController = navController,
        )

        breedsListScreen(
            route = "breedsListScreen",
            navController = navController,
        )

        breedDetails(
            route = "breeds/{dataId}",
            navController = navController,
        )

        breedImagesGrid(
            route = "grid/{dataId}",
            onAlbumClick = {
                navController.navigate(route = "gallery/${it}")
            },
            arguments = listOf(
                navArgument(name = "dataId") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = {
                navController.navigateUp()
            }
        )

        photoGallery(
            route = "gallery/{dataId}",
            navController = navController,
            arguments = listOf(
                navArgument(name = "dataId") {
                    nullable = false
                    type = NavType.StringType
                },
            ),
            onClose = {
                navController.navigateUp()
            },

        )
    }
}

inline val SavedStateHandle.dataId: String
    get() = checkNotNull(get("dataId")) { "breedId is mandatory" }