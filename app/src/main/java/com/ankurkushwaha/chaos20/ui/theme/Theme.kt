package com.ankurkushwaha.chaos20.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Modern Dark Theme - Deep blues with vibrant accent
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3A86FF),       // Bright blue
    secondary = Color(0xFF8338EC),     // Purple
    tertiary = Color(0xFFFF006E),      // Vibrant pink
    background = Color(0xFF0A1929),    // Deep navy blue
    surface = Color(0xFF132F4C),       // Slightly lighter navy
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFE0E0E0),  // Light gray for text
    onSurface = Color(0xFFE0E0E0)      // Light gray for text
)

// Light Theme - Soft blues with complementary accents
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3A86FF),       // Bright blue
    secondary = Color(0xFF6B5ECD),     // Softer purple
    tertiary = Color(0xFFFF6B6B),      // Coral pink
    background = Color(0xFFF8F9FA),    // Off-white
    surface = Color(0xFFFFFFFF),       // Pure white
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),  // Dark gray for text
    onSurface = Color(0xFF1C1B1F)      // Dark gray for text
)

// Alternative "Synthwave" Theme - Retro-futuristic vibe
private val SynthwaveColorScheme = darkColorScheme(
    primary = Color(0xFFFF00FF),       // Magenta
    secondary = Color(0xFF00FFFF),     // Cyan
    tertiary = Color(0xFFFFCC00),      // Golden yellow
    background = Color(0xFF1A0933),    // Deep purple-blue
    surface = Color(0xFF2D1B54),       // Medium purple
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFFE0E0E0),  // Light gray for text
    onSurface = Color(0xFFE0E0E0)      // Light gray for text
)

// Minimalist "Spotify-inspired" Theme - Dark with green accent
private val SpotifyInspiredScheme = darkColorScheme(
    primary = Color(0xFF1DB954),       // Spotify green
    secondary = Color(0xFF1ED760),     // Lighter green
    tertiary = Color(0xFFFFFFFF),      // White accent
    background = Color(0xFF121212),    // Very dark gray
    surface = Color(0xFF181818),       // Dark gray
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFFE0E0E0),  // Light gray for text
    onSurface = Color(0xFFE0E0E0)      // Light gray for text
)

// Warm "Vinyl" Theme - Warm colors reminiscent of vinyl records and retro equipment
private val VinylColorScheme = darkColorScheme(
    primary = Color(0xFFF2A65A),       // Amber/orange
    secondary = Color(0xFFEF8354),     // Burnt orange
    tertiary = Color(0xFF5C4742),      // Brown
    background = Color(0xFF252422),    // Very dark brown
    surface = Color(0xFF403D39),       // Medium brown
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFEBECF0),  // Off-white for text
    onSurface = Color(0xFFEBECF0)      // Off-white for text
)

@Composable
fun Chaos20Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}