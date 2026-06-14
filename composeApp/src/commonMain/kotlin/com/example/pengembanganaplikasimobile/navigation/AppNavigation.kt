package navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.pengembanganaplikasimobile.data.NoteRepository
import screens.*
import com.example.pengembanganaplikasimobile.screens.*

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(id: Long) = "note_detail/$id" // Ubah ID ke Long menyesuaikan SQLDelight
    }
}

@Composable
fun AppNavigation(repository: NoteRepository, dataStore: DataStore<Preferences>) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1A1A2E)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val menuItems = listOf(
                    Triple(Screen.Notes, "Notes", "📝"),
                    Triple(Screen.Favorites, "Favs", "⭐"),
                    Triple(Screen.Profile, "User", "👤")
                )

                menuItems.forEach { (screen, label, icon) ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(label, color = Color.White) },
                        icon = { Text(icon) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFF3D2C5E))
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Notes.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Notes.route) {
                NoteListScreen(
                    repository = repository,
                    dataStore = dataStore,
                    onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) }
                )
            }
            composable(Screen.Favorites.route) { FavoritesScreen() }
            composable(Screen.Profile.route) { ProfileScreen(dataStore = dataStore) }
            composable(
                route = Screen.NoteDetail.route,
                arguments = listOf(navArgument("noteId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("noteId") ?: 0L
                // TAMBAHKAN repository = repository di bawah ini 👇
                NoteDetailScreen(noteId = id, repository = repository, onBack = { navController.popBackStack() })
            }
        }
    }
}