package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ankurkushwaha.chaos20.presentation.navigation.Screen

/**
 * @author Ankur Kushwaha
 * created at 06 May 2025 09:58
 */

@Composable
fun BottomNavigation(
    navController: NavHostController,
    currentScreen: Screen
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = currentScreen == Screen.Home,
            onClick = {
                navController.navigate(Screen.Home)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            label = { Text("Search") },
            selected = currentScreen == Screen.Search,
            onClick = {
                navController.navigate(Screen.Search)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.Favorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Fav"
                )
            },
            label = { Text("Favorite") },
            selected = currentScreen == Screen.Favorite,
            onClick = {
                navController.navigate(Screen.Favorite)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "PlayList"
                )
            },
            label = { Text("Playlist") },
            selected = currentScreen == Screen.Playlist,
            onClick = {
                navController.navigate(Screen.Playlist)
            }
        )
    }
}

