package com.example.engvocab.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    // Primary/Chủ đạo
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    inversePrimary = PrimaryLight, // Đảo ngược Primary

    // Secondary/Phụ
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,

    // Tertiary/Nhấn khác (Dùng màu Phụ thứ 3 - nếu có, nếu không có thì dùng Secondary)
    tertiary = PrimaryDark, // Có thể dùng lại primary nếu không có màu tertiary riêng
    onTertiary = OnPrimaryDark,
    tertiaryContainer = PrimaryContainerDark,
    onTertiaryContainer = OnPrimaryContainerDark,

    // Nền & Bề mặt (Đen, Xám)
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = OutlineVariantDark, // Dùng Xám đậm
    onSurfaceVariant = OnSurfaceDark,
    surfaceTint = PrimaryDark, // Màu primary khi được elevation
    inverseSurface = SurfaceLight, // Đảo ngược Surface (Trắng)
    inverseOnSurface = OnSurfaceLight, // Nội dung trên Inverse Surface (Đen)

    // Lỗi (Error)
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,

    // Viền (Outline)
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,

    // Khác
    scrim = Color(0x99000000), // Lớp mờ (mask)

    // MÀU SURFACE MỚI (Từ Material 3)
    surfaceDim = SurfaceDimDark,
    surfaceBright = SurfaceBrightDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark
)

private val LightColorScheme = lightColorScheme(
    // Primary/Chủ đạo
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    inversePrimary = PrimaryDark,

    // Secondary/Phụ
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,

    // Tertiary/Nhấn khác
    tertiary = PrimaryLight,
    onTertiary = OnPrimaryLight,
    tertiaryContainer = PrimaryContainerLight,
    onTertiaryContainer = OnPrimaryContainerLight,

    // Nền & Bề mặt (Trắng, Xám)
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = OutlineVariantLight, // Dùng Xám rất nhạt
    onSurfaceVariant = OnSurfaceLight,
    surfaceTint = PrimaryLight,
    inverseSurface = SurfaceDark,
    inverseOnSurface = OnSurfaceDark,

    // Lỗi (Error)
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,

    // Viền (Outline)
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,

    // Khác
    scrim = Color(0x99000000),

    // MÀU SURFACE MỚI (Từ Material 3)
    surfaceDim = SurfaceDimLight,
    surfaceBright = SurfaceBrightLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight
)

@Composable
fun EngVocabTheme(
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}