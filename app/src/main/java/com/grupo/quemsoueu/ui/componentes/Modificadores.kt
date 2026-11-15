package com.grupo.quemsoueu.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.RoxoFundo
import com.grupo.quemsoueu.ui.theme.Tinta

/** Cantos arredondados padrão das peças do jogo. */
val FormaCartoon = RoundedCornerShape(20.dp)

/**
 * O "visual de peça de papelão" do app: contorno escuro grosso e uma sombra dura
 * deslocada para baixo. Use em qualquer caixa:  Modifier.cartoon(cor = Amarelo)
 *
 * A sombra é desenhada para fora da caixa; deixe um respiro embaixo (padding ou espaçamento).
 */
fun Modifier.cartoon(
    cor: Color = Branco,
    forma: Shape = FormaCartoon,
    sombra: Dp = 5.dp,
    contorno: Dp = 3.dp,
): Modifier = this
    .drawBehind {
        if (sombra > 0.dp) {
            val contornoDaForma = forma.createOutline(size, layoutDirection, this)
            translate(top = sombra.toPx()) {
                drawOutline(contornoDaForma, color = Tinta)
            }
        }
    }
    .clip(forma)
    .background(cor)
    .border(contorno, Tinta, forma)

/** Fundo roxo com bolinhas, usado em todas as telas. Coloque antes de verticalScroll. */
fun Modifier.fundoJogo(): Modifier = this
    .background(RoxoFundo)
    .drawBehind {
        val passo = 24.dp.toPx()
        val raio = 2.dp.toPx()
        val corBolinha = Color.White.copy(alpha = 0.13f)
        var y = passo / 2
        while (y < size.height) {
            var x = passo / 2
            while (x < size.width) {
                drawCircle(color = corBolinha, radius = raio, center = Offset(x, y))
                x += passo
            }
            y += passo
        }
    }
