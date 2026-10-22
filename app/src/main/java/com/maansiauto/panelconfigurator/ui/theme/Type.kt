package com.maansiauto.panelconfigurator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Uses the platform default font family. To match the web app's
// Space Grotesk / Inter pairing, add those as custom fonts under
// res/font/ and reference them here via FontFamily(Font(...)).
val AppTypography = Typography(
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.5.sp, lineHeight = 20.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp, lineHeight = 14.sp)
)
