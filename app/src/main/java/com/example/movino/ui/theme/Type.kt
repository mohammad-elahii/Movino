package com.example.movino.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.movino.R


val Typography: Typography
    get() = Typography(
        displayLarge = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = (0).sp
        ),
        displaySmall = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = (0).sp
        ),


        headlineLarge = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = (0).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = (0).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = (0).sp
        ),


        titleLarge = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = (0).sp
        ),
        titleMedium = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = (0.15).sp
        ),
        titleSmall = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = (0.1).sp
        ),

        bodyLarge = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W300,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = robotoFamily,
            fontWeight = FontWeight.W700,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )

    )


val robotoFamily = FontFamily(
    fonts = listOf(
        Font(R.font.roboto_bold, FontWeight.W700),
        Font(R.font.roboto_regular, FontWeight.W400),
    )

)