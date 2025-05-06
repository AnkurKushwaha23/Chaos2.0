package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.font.FontWeight
import com.ankurkushwaha.chaos20.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaosTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "Chaos",
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
//    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
            )
        },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu_24),
                    contentDescription = "More",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = { onSearchClick() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        },
//        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}





//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
//        ),

//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//        )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaosMiddleTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String = "Chaos",
    onSearchClick: () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
//            IconButton(onClick = { onSearchClick() }) {
//                Icon(
//                    imageVector = Icons.Filled.Search,
//                    contentDescription = "Search"
//                )
//            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = navigationIcon
    )
}

//            DropdownMenu(
//                expanded = showMenu,
//                onDismissRequest = { showMenu = false }
//            ) {
//                menuItems.forEach { item ->
//                    DropdownMenuItem(
//                        onClick = {
//                            showMenu = false
//                            onMenuItemClick(item)
//                        },
//                        text = { Text(item) }
//                    )
//                }
//            }
