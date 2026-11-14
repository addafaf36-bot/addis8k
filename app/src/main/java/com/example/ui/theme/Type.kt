package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val NotoSansEthiopicFamily =
  FontFamily(
    Font(R.font.noto_sans_ethiopic, FontWeight.Normal),
    Font(R.font.noto_sans_ethiopic, FontWeight.Medium),
    Font(R.font.noto_sans_ethiopic, FontWeight.SemiBold),
    Font(R.font.noto_sans_ethiopic, FontWeight.Bold),
  )

val PlusJakartaSansFamily =
  FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold),
  )

val JetBrainsMonoFamily =
  FontFamily(
    Font(R.font.jetbrains_mono, FontWeight.Medium),
    Font(R.font.jetbrains_mono, FontWeight.Bold),
  )

val MonoAccountTextStyle =
  TextStyle(
    fontFamily = JetBrainsMonoFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.6.sp,
  )

val AppTypography =
  Typography(
    headlineMedium =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
      ),
    titleLarge =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
      ),
    titleMedium =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
      ),
    titleSmall =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
      ),
    bodyLarge =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
      ),
    bodyMedium =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
      ),
    bodySmall =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
      ),
    labelLarge =
      TextStyle(
        fontFamily = NotoSansEthiopicFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
      ),
    labelMedium =
      TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
      ),
  )
