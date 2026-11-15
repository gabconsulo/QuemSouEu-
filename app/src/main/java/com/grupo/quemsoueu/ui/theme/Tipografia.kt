package com.grupo.quemsoueu.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.R

/** Fonte arredondada e grossa dos títulos e botões (pasta res/font). */
val Lilita = FontFamily(Font(R.font.lilita_one))

/** Fonte do texto corrido. */
val Nunito = FontFamily(
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
    Font(R.font.nunito_black, FontWeight.Black),
)

val TipografiaJogo = Typography(
    displayLarge = TextStyle(fontFamily = Lilita, fontSize = 46.sp, lineHeight = 50.sp),
    headlineMedium = TextStyle(fontFamily = Lilita, fontSize = 30.sp, lineHeight = 34.sp),
    titleLarge = TextStyle(fontFamily = Lilita, fontSize = 22.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = Lilita, fontSize = 18.sp, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 14.sp, lineHeight = 18.sp),
    labelMedium = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Black, fontSize = 13.sp, lineHeight = 16.sp),
)
