package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.componentes.AvisoCartoon
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.BotaoIconeCartoon
import com.grupo.quemsoueu.ui.componentes.CampoCartoon
import com.grupo.quemsoueu.ui.componentes.CarimboResposta
import com.grupo.quemsoueu.ui.componentes.CartaCartoon
import com.grupo.quemsoueu.ui.componentes.EstiloBotao
import com.grupo.quemsoueu.ui.componentes.PillCartoon
import com.grupo.quemsoueu.ui.componentes.SeloCartoon
import com.grupo.quemsoueu.ui.componentes.TipoAviso
import com.grupo.quemsoueu.ui.componentes.cartoon
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.modelos.DadosExemplo
import com.grupo.quemsoueu.ui.modelos.EstadoJogoUi
import com.grupo.quemsoueu.ui.modelos.PerguntaIaUi
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Azul
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.CoresDicas
import com.grupo.quemsoueu.ui.theme.Lilas
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.theme.RosaCoracao
import com.grupo.quemsoueu.ui.theme.RoxoEscuro
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave

/**
 * Tela 2 · Jogo rodando.
 *
 * A tela só MOSTRA o [estado] e AVISA o que a pessoa fez. Quem decide se o palpite está
 * certo, quantas vidas sobram e quantos pontos valem é a lógica:
 *  - onVirarDica, onChutar e o [estado]: Matheus Rodrigues (RegrasJogo / GameViewModel)
 *  - onPerguntar e a parte de IA do [estado]: Gabriel
 */
@Composable
fun TelaJogo(
    estado: EstadoJogoUi,
    onVirarDica: () -> Unit,
    onPerguntar: (String) -> Unit,
    onChutar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var palpite by rememberSaveable { mutableStateOf("") }
    var erroPalpite by rememberSaveable { mutableStateOf<String?>(null) }

    fun enviarPalpite() {
        if (palpite.isBlank()) {
            erroPalpite = "Digite quem você acha que é"
        } else {
            onChutar(palpite.trim())
            palpite = ""
            erroPalpite = null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .fundoJogo()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PlacarDoJogo(estado)

        Row(verticalAlignment = Alignment.CenterVertically) {
            PillCartoon(texto = estado.categoria.nome, cor = estado.categoria.cor)
            Spacer(Modifier.weight(1f))
            Text(
                "Rodada ${estado.rodada} · ${estado.dificuldade.nome}",
                style = MaterialTheme.typography.labelLarge,
                color = Branco,
            )
        }

        if (estado.mostrarCronometro) {
            BarraDeTempo(progresso = estado.progressoTempo)
        }

        if (estado.aviso != null) {
            AvisoCartoon(texto = estado.aviso, tipo = TipoAviso.ERRO)
        }

        // Dicas já abertas
        estado.dicas.take(estado.dicasReveladas).forEachIndexed { indice, dica ->
            CartaDica(
                numero = indice + 1,
                texto = dica,
                cor = CoresDicas[indice % CoresDicas.size],
                nova = indice == estado.indiceDicaNova,
            )
        }

        // Dicas ainda viradas para baixo + botão de virar a próxima
        val restantes = estado.dicas.size - estado.dicasReveladas
        if (restantes > 0) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                for (numero in (estado.dicasReveladas + 1)..estado.dicas.size) {
                    CartaVirada(numero = numero, modifier = Modifier.weight(1f))
                }
            }
            BotaoCartoon(
                texto = "Virar dica (−${estado.custoDica} pts)",
                onClick = onVirarDica,
                icone = Icons.Rounded.Lightbulb,
                estilo = EstiloBotao.BRANCO,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (estado.iaLigada) {
            CaixaPergunteAIa(estado = estado, onPerguntar = onPerguntar)
        }

        // Palpite
        Text("Seu palpite", style = MaterialTheme.typography.labelLarge, color = Branco)
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CampoCartoon(
                valor = palpite,
                aoMudar = {
                    palpite = it
                    erroPalpite = null
                },
                dica = "Quem sou eu?",
                erro = erroPalpite,
                acaoTeclado = ImeAction.Done,
                aoConfirmar = { enviarPalpite() },
                modifier = Modifier.weight(1f),
            )
            BotaoCartoon(
                texto = "Chutar!",
                onClick = { enviarPalpite() },
                estilo = EstiloBotao.VERDE,
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}

/** Vidas, tempo e pontos no topo. */
@Composable
private fun PlacarDoJogo(estado: EstadoJogoUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = "Vidas: ${estado.vidas} de ${estado.vidasMaximas}"
            },
        ) {
            repeat(estado.vidasMaximas) { indice ->
                Coracao(cheio = indice < estado.vidas)
            }
        }
        Spacer(Modifier.weight(1f))
        if (estado.mostrarCronometro) {
            PillCartoon(texto = estado.tempoTexto, icone = Icons.Rounded.Timer)
        }
        PillCartoon(texto = "${estado.pontos} pts", icone = Icons.Rounded.Star, cor = Amarelo)
    }
}

@Composable
private fun Coracao(cheio: Boolean) {
    Box(contentAlignment = Alignment.Center) {
        Icon(
            Icons.Rounded.Favorite,
            contentDescription = null,
            tint = if (cheio) RosaCoracao else Branco,
            modifier = Modifier.size(30.dp),
        )
        Icon(
            Icons.Rounded.FavoriteBorder,
            contentDescription = null,
            tint = Tinta,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
private fun BarraDeTempo(progresso: Float) {
    val valor = progresso.coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .cartoon(cor = Branco, forma = RoundedCornerShape(8.dp), sombra = 0.dp)
            .semantics {
                contentDescription = "Tempo restante"
                progressBarRangeInfo = ProgressBarRangeInfo(valor, 0f..1f)
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(valor)
                .fillMaxHeight()
                .background(Amarelo),
        )
    }
}

/** Dica aberta: círculo colorido com o número e o texto. */
@Composable
private fun CartaDica(numero: Int, texto: String, cor: Color, nova: Boolean) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
                .cartoon()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .cartoon(cor = cor, forma = CircleShape, sombra = 0.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("$numero", style = TextStyle(fontFamily = Lilita, fontSize = 20.sp), color = Tinta)
            }
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge,
                color = Tinta,
                modifier = Modifier.semantics { contentDescription = "Dica $numero: $texto" },
            )
        }
        if (nova) {
            SeloCartoon(
                texto = "Nova!",
                cor = Amarelo,
                tamanho = 13,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp)
                    .offset(y = (-12).dp), // o selo "sai" da borda da carta
            )
        }
    }
}

/** Verso de uma dica bloqueada: listras e cadeado. */
@Composable
private fun CartaVirada(numero: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
            .height(68.dp)
            .cartoon(cor = RoxoEscuro, forma = RoundedCornerShape(16.dp), sombra = 4.dp)
            .drawBehind {
                // listras diagonais
                val passo = 12.dp.toPx()
                val cor = Color.White.copy(alpha = 0.14f)
                var x = -size.height
                while (x < size.width) {
                    drawLine(
                        color = cor,
                        start = Offset(x, size.height),
                        end = Offset(x + size.height, 0f),
                        strokeWidth = 6.dp.toPx(),
                    )
                    x += passo
                }
            }
            .semantics(mergeDescendants = true) { contentDescription = "Dica $numero bloqueada" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Rounded.Lock, contentDescription = null, tint = Branco, modifier = Modifier.size(20.dp))
        Text("Dica $numero", style = MaterialTheme.typography.labelMedium, color = Branco)
    }
}

/** Caixa "Pergunte à IA": histórico de perguntas e campo para perguntar. */
@Composable
private fun CaixaPergunteAIa(estado: EstadoJogoUi, onPerguntar: (String) -> Unit) {
    var pergunta by rememberSaveable { mutableStateOf("") }
    var erroPergunta by rememberSaveable { mutableStateOf<String?>(null) }
    val acabou = estado.perguntasIa.size >= estado.limitePerguntas

    fun enviar() {
        when {
            acabou -> Unit
            pergunta.isBlank() -> erroPergunta = "Escreva uma pergunta de sim ou não"
            else -> {
                onPerguntar(pergunta.trim())
                pergunta = ""
                erroPergunta = null
            }
        }
    }

    CartaCartoon(modifier = Modifier.fillMaxWidth(), espacamento = 6) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = Azul, modifier = Modifier.size(24.dp))
            Spacer(Modifier.size(6.dp))
            Text("Pergunte à IA", style = MaterialTheme.typography.titleLarge, color = Tinta)
            Spacer(Modifier.weight(1f))
            PillCartoon(texto = "${estado.perguntasIa.size} de ${estado.limitePerguntas}")
        }

        if (estado.perguntasIa.isEmpty()) {
            Text(
                "Faça perguntas de sim ou não para descobrir quem é. Ex.: “É uma pessoa real?”",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSuave,
            )
        }
        estado.perguntasIa.forEach { item -> LinhaPergunta(item) }

        if (estado.iaPensando) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Azul, strokeWidth = 3.dp)
                Text("A IA está pensando…", style = MaterialTheme.typography.bodyMedium, color = TextoSuave)
            }
        }
        if (estado.erroIa != null) {
            AvisoCartoon(texto = estado.erroIa, tipo = TipoAviso.ERRO)
        }

        if (acabou) {
            Text(
                "Você já usou as ${estado.limitePerguntas} perguntas desta partida.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSuave,
            )
        } else {
            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CampoCartoon(
                    valor = pergunta,
                    aoMudar = {
                        pergunta = it
                        erroPergunta = null
                    },
                    dica = "Pergunta de sim ou não",
                    erro = erroPergunta,
                    habilitado = !estado.iaPensando,
                    acaoTeclado = ImeAction.Send,
                    aoConfirmar = { enviar() },
                    modifier = Modifier.weight(1f),
                )
                BotaoIconeCartoon(
                    icone = Icons.AutoMirrored.Rounded.Send,
                    descricao = "Enviar pergunta",
                    onClick = { enviar() },
                    cor = Azul,
                    habilitado = !estado.iaPensando,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Text(
                "Cada pergunta custa ${estado.custoPergunta} pontos.",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSuave,
            )
        }
    }
}

@Composable
private fun LinhaPergunta(item: PerguntaIaUi) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .semantics(mergeDescendants = true) { },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                item.pergunta,
                style = MaterialTheme.typography.bodyMedium,
                color = Tinta,
                modifier = Modifier.weight(1f),
            )
            CarimboResposta(item.resposta)
        }
        HorizontalDivider(thickness = 2.dp, color = Lilas)
    }
}

@Preview(widthDp = 390, heightDp = 1200)
@Composable
private fun TelaJogoPreview() {
    QuemSouEuTema {
        TelaJogo(
            estado = DadosExemplo.estadoJogo,
            onVirarDica = {},
            onPerguntar = {},
            onChutar = {},
        )
    }
}
