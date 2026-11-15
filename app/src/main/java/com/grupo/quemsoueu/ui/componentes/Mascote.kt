package com.grupo.quemsoueu.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.Pele
import com.grupo.quemsoueu.ui.theme.RosaCoracao
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.Verde

/**
 * Mascote do jogo, desenhado 100% em Kotlin com Canvas: uma carinha com uma tiara
 * e a carta secreta "?" presa na testa, como no Quem Sou Eu? de tabuleiro.
 *
 * O desenho foi feito numa "folha" de 220 x 210 pontos; a variável [s] converte
 * esses pontos para o tamanho real na tela.
 */
@Composable
fun Mascote(modifier: Modifier = Modifier) {
    val medidorDeTexto = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .aspectRatio(220f / 210f)
            .semantics { contentDescription = "Mascote do jogo com uma carta de interrogação na testa" },
    ) {
        val s = size.width / 220f
        fun p(x: Float, y: Float) = Offset(x * s, y * s)

        // Sombra no chão
        drawOval(color = Tinta.copy(alpha = 0.35f), topLeft = p(40f, 192f), size = Size(140f * s, 16f * s))

        // Cabeça
        drawCircle(color = Pele, radius = 62f * s, center = p(110f, 138f))
        drawCircle(color = Tinta, radius = 62f * s, center = p(110f, 138f), style = Stroke(width = 6f * s))

        // Bochechas
        val corBochecha = Color(0xFFFF8FA8).copy(alpha = 0.8f)
        drawCircle(color = corBochecha, radius = 7f * s, center = p(72f, 158f))
        drawCircle(color = corBochecha, radius = 7f * s, center = p(148f, 158f))

        // Olhos com brilho
        drawOval(color = Tinta, topLeft = p(80f, 129f), size = Size(16f * s, 22f * s))
        drawOval(color = Tinta, topLeft = p(124f, 129f), size = Size(16f * s, 22f * s))
        drawCircle(color = Branco, radius = 3f * s, center = p(91f, 136f))
        drawCircle(color = Branco, radius = 3f * s, center = p(135f, 136f))

        // Sorriso
        val sorriso = Path().apply {
            moveTo(92f * s, 166f * s)
            quadraticTo(110f * s, 182f * s, 128f * s, 166f * s)
        }
        drawPath(sorriso, color = Tinta, style = Stroke(width = 6f * s, cap = StrokeCap.Round))

        // Tiara (contorno escuro por baixo, rosa por cima)
        val tiara = Path().apply {
            moveTo(50f * s, 108f * s)
            quadraticTo(110f * s, 82f * s, 170f * s, 108f * s)
        }
        drawPath(tiara, color = Tinta, style = Stroke(width = 16f * s, cap = StrokeCap.Round))
        drawPath(tiara, color = RosaCoracao, style = Stroke(width = 8f * s, cap = StrokeCap.Round))

        // Carta secreta, levemente inclinada
        rotate(degrees = -6f, pivot = p(110f, 52f)) {
            drawRoundRect(color = Amarelo, topLeft = p(76f, 8f), size = Size(68f * s, 92f * s), cornerRadius = CornerRadius(12f * s))
            drawRoundRect(
                color = Tinta, topLeft = p(76f, 8f), size = Size(68f * s, 92f * s),
                cornerRadius = CornerRadius(12f * s), style = Stroke(width = 6f * s),
            )
            drawRoundRect(
                color = Tinta, topLeft = p(84f, 16f), size = Size(52f * s, 76f * s),
                cornerRadius = CornerRadius(7f * s),
                style = Stroke(width = 2.5f * s, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * s, 5f * s))),
            )

            val interrogacao = medidorDeTexto.measure(
                text = "?",
                style = TextStyle(fontFamily = Lilita, fontSize = (62f * s).toSp()),
            )
            val canto = p(110f, 56f) - Offset(interrogacao.size.width / 2f, interrogacao.size.height / 2f)
            drawText(
                textLayoutResult = interrogacao,
                color = Tinta,
                topLeft = canto,
                drawStyle = Stroke(width = 10f * s, join = StrokeJoin.Round),
            )
            drawText(textLayoutResult = interrogacao, color = Branco, topLeft = canto)
        }

        // Brilhos
        desenharBrilho(centro = p(36f, 40f), raio = 18f * s, cor = Branco, contorno = 3f * s)
        desenharBrilho(centro = p(188f, 74f), raio = 13f * s, cor = Verde, contorno = 3f * s)
    }
}

/** Estrelinha de quatro pontas. */
private fun DrawScope.desenharBrilho(centro: Offset, raio: Float, cor: Color, contorno: Float) {
    val miolo = raio * 0.3f
    val brilho = Path().apply {
        moveTo(centro.x, centro.y - raio)
        lineTo(centro.x + miolo, centro.y - miolo)
        lineTo(centro.x + raio, centro.y)
        lineTo(centro.x + miolo, centro.y + miolo)
        lineTo(centro.x, centro.y + raio)
        lineTo(centro.x - miolo, centro.y + miolo)
        lineTo(centro.x - raio, centro.y)
        lineTo(centro.x - miolo, centro.y - miolo)
        close()
    }
    drawPath(brilho, color = cor)
    drawPath(brilho, color = Tinta, style = Stroke(width = contorno, join = StrokeJoin.Round))
}

@Preview(showBackground = true, backgroundColor = 0xFF6C3CE1)
@Composable
private fun MascotePreview() {
    Mascote(Modifier.width(220.dp))
}
