package com.ankurkushwaha.chaos20.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankurkushwaha.chaos20.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaosBottomSheet(
    modifier: Modifier = Modifier,
    onSleepTimerClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onSuggestionsClick: () -> Unit,
    onShareClick: () -> Unit,
    onAboutDeveloperClick: () -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Effect to handle show/hide
    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    // Effect to notify parent when sheet is hidden via gesture
    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            onDismiss()
        }
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
            },
            sheetState = sheetState,
            dragHandle = null
//            containerColor = Color(0xFF2A2C35),
//            contentColor = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    text = "Chaos",
                    style = MaterialTheme.typography.headlineMedium,
//                    color = MaterialTheme.colorScheme.primary
                )

                // Divider line
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )

                // Sleep Timer
                MenuItem(
                    iconRes = R.drawable.ic_timer,
                    text = "Sleep Timer",
                    onClick = onSleepTimerClick
                )

                // Report Bug
                MenuItem(
                    iconRes = R.drawable.ic_bug,
                    text = "Report Bug",
                    onClick = onReportBugClick
                )

                // Suggestions
                MenuItem(
                    iconRes = R.drawable.ic_suggestion,
                    text = "Suggestions",
                    onClick = onSuggestionsClick
                )

                // Share
                MenuItem(
                    iconRes = R.drawable.ic_share,
                    text = "Share",
                    onClick = onShareClick
                )

                // About Developer
                MenuItem(
                    iconRes = R.drawable.ic_info,
                    text = "About Developer",
                    onClick = onAboutDeveloperClick
                )

                // Add some padding at the bottom
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MenuItem(
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    val iconColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.primary
        )
    }
}