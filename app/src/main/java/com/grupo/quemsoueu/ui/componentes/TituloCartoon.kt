package com.grupo.quemsoueu.ui.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.Tinta

/**
 * Título branco com contorno escuro, estilo letreiro de jogo.
 * Funciona desenhando o mesmo texto duas vezes: primeiro só o contorno (Stroke), depois o preenchimento.
 */
@Composable
fun TituloCartoon(
    texto: String,
    modifier: Modifier = Modifier,
    tamanho: TextUnit = 32.sp,
    cor: Color = Branco,
    alinhamento: TextAlign = TextAlign.Start,
) {
    val larguraContorno = with(LocalDensity.current) { tamanho.toPx() * 0.18f }
    val estiloContorno = TextStyle(
        fontFamily = Lilita,
        fontSize = tamanho,
        lineHeight = tamanho * 1.1f,
        letterSpacing = 0.5.sp,
        textAlign = alinhamento,
        color = Tinta,
        drawStyle = Stroke(width = larguraContorno, join = StrokeJoin.Round),
    )
    val estiloPreenchimento = TextStyle(
        fontFamily = Lilita,
        fontSize = tamanho,
        lineHeight = tamanho * 1.1f,
        letterSpacing = 0.5.sp,
        textAlign = alinhamento,
        color = cor,
    )
    Box(modifier) {
        // Só desenho: o leitor de tela lê o texto de cima, uma vez só.
        Text(text = texto, style = estiloContorno, modifier = Modifier.clearAndSetSemantics { })
        Text(text = texto, style = estiloPreenchimento, modifier = Modifier.semantics { heading() })
    }
}
