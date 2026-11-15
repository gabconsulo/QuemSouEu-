package com.grupo.quemsoueu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaDeCores = lightColorScheme(
    primary = RoxoFundo,
    onPrimary = Branco,
    secondary = Amarelo,
    onSecondary = Tinta,
    background = RoxoFundo,
    onBackground = Branco,
    surface = Branco,
    onSurface = Tinta,
    surfaceVariant = Lilas,
    onSurfaceVariant = TextoSuave,
    outline = Tinta,
    error = ErroTexto,
    onError = Branco,
)

/** Envolve o app inteiro (e cada @Preview) com as cores e fontes do jogo. */
@Composable
fun QuemSouEuTema(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaDeCores,
        typography = TipografiaJogo,
        content = content,
    )
}
