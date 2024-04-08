package rs.raf.catapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rs.raf.catapp.breeds.domain.BreedData
import rs.raf.catapp.breeds.details.BreedsDetailsScreen
import rs.raf.catapp.breeds.details.breedDetails
import rs.raf.catapp.breeds.list.BreedsListScreen
import rs.raf.catapp.breeds.list.breedsListScreen
import rs.raf.catapp.breeds.repository.BreedRepository
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "breedsListScreen",
    ){
        breedsListScreen(
            route = "breedsListScreen",
            navController = navController,
        )

        breedDetails(
            route = "breeds/{dataId}",
            navController = navController,
        )
    }
}