package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankurkushwaha.chaos20.BuildConfig
import com.ankurkushwaha.chaos20.R

/**
 * @author Ankur Kushwaha;
 * created at 06 May 2025 10:54
 */

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    onSleepTimerClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onSuggestionsClick: () -> Unit,
    onShareClick: () -> Unit,
    onAboutDeveloperClick: () -> Unit,
) {

    Spacer(modifier = Modifier.height(20.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.chaos_bg),
            contentDescription = "Chaos",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Chaos",
        fontSize = 28.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(3.dp))

    val versionName = BuildConfig.VERSION_NAME

    Text(
        text = versionName.ifEmpty { "1.0" },
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    HorizontalDivider()
    Spacer(modifier = Modifier.height(4.dp))

    NavigationDrawerItem(
        label = {
            Text(
                text = "Sleep Timer",
                fontSize = 17.sp,
            )
        },
        selected = false,
        onClick = { onSleepTimerClick() },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_timer),
                contentDescription = "Sleep Timer",
                modifier = Modifier
                    .size(27.dp)
            )
        }
    )

    Spacer(modifier = Modifier.height(4.dp))

    NavigationDrawerItem(
        label = {
            Text(
                text = "Report Bug",
                fontSize = 17.sp,

                )
        },
        selected = false,
        onClick = { onReportBugClick() },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_bug),
                contentDescription = "Report Bug",
                modifier = Modifier
                    .size(27.dp)
            )
        }
    )

    Spacer(modifier = Modifier.height(4.dp))

    NavigationDrawerItem(
        label = {
            Text(
                text = "Suggestions",
                fontSize = 17.sp,
            )
        },
        selected = false,
        onClick = { onSuggestionsClick() },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_suggestion),
                contentDescription = "Suggestions",
                modifier = Modifier
                    .size(27.dp)
            )
        }
    )

    Spacer(modifier = Modifier.height(4.dp))

    NavigationDrawerItem(
        label = {
            Text(
                text = "Share",
                fontSize = 17.sp,
            )
        },
        selected = false,
        onClick = { onShareClick() },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = "Share",
                modifier = Modifier
                    .size(27.dp)
            )
        }
    )

    Spacer(modifier = Modifier.height(4.dp))

    NavigationDrawerItem(
        label = {
            Text(
                text = "About Developer",
                fontSize = 17.sp,
            )
        },
        selected = false,
        onClick = { onAboutDeveloperClick() },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = "About Developer",
                modifier = Modifier
                    .size(27.dp)
            )
        }
    )

    Spacer(modifier = Modifier.height(4.dp))
}

//        badge = TODO(),
//        shape = TODO(),
//        colors = TODO(),
//        interactionSource = TODO()