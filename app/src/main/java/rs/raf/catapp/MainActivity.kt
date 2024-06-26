package rs.raf.catapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import rs.raf.catapp.core.theme.CatappTheme
import rs.raf.catapp.navigation.AppNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = (applicationContext as CatApp)
        setContent {
                CatappTheme {
                    AppNavigation()
                }
        }
    }
}