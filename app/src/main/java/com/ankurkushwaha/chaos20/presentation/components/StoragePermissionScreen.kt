package com.ankurkushwaha.chaos20.presentation.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankurkushwaha.chaos20.R

@Composable
fun StoragePermissionScreen(
    onPermissionClick: () -> Unit
) {
    val title = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        "Storage and Post Notification Permission are needed"
    } else {
        "Storage Permission are needed"
    }

    val desc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        "This app needs access to your storage and notification."
    } else {
        "This app needs access to your storage."
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                painter = painterResource(id = R.drawable.undraw_listening_),
                contentDescription = "music"
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                text = desc,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                onClick = { onPermissionClick() }
            ) {
                Text(text = "Allow Permission")
            }
        }
    }
}