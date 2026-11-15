package com.grupo.quemsoueu.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.modelos.Dificuldade
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Laranja
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave

/** Topo das telas: botão de voltar (opcional), título com contorno e uma ação à direita (opcional). */
@Composable
fun TopoTela(
    titulo: String,
    modifier: Modifier = Modifier,
    onVoltar: (() -> Unit)? = null,
    acao: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (onVoltar != null) {
            BotaoIconeCartoon(Icons.AutoMirrored.Rounded.ArrowBack, descricao = "Voltar", onClick = onVoltar)
        }
        TituloCartoon(texto = titulo, modifier = Modifier.weight(1f), tamanho = 30.sp)
        if (acao != null) acao()
    }
}

enum class AbaJogador(val rotulo: String, val icone: ImageVector) {
    JOGAR("Jogar", Icons.Rounded.SportsEsports),
    RANKING("Ranking", Icons.Rounded.EmojiEvents),
    AJUSTES("Ajustes", Icons.Rounded.Settings),
}

/**
 * Visual da Bottom Navigation Bar do Modo Jogador.
 * Matheus Rodrigues coloca este componente no bottomBar do Scaffold e liga as rotas em onSelecionar.
 */
@Composable
fun BarraJogador(
    selecionada: AbaJogador,
    onSelecionar: (AbaJogador) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Branco)
            .drawBehind { drawRect(color = Tinta, size = Size(size.width, 4.dp.toPx())) }
            .navigationBarsPadding()
            .height(84.dp)
            .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AbaJogador.entries.forEach { aba ->
            val ativa = aba == selecionada
            Column(
                modifier = Modifier
                    .weight(1f)
                    .selectable(selected = ativa, role = Role.Tab, onClick = { onSelecionar(aba) })
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .size(width = 56.dp, height = 38.dp)
                        .then(
                            if (ativa) {
                                Modifier.cartoon(cor = Amarelo, forma = RoundedCornerShape(14.dp), sombra = 3.dp)
                            } else {
                                Modifier
                            },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(aba.icone, contentDescription = null, tint = if (ativa) Tinta else TextoSuave)
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = aba.rotulo,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (ativa) Tinta else TextoSuave,
                )
            }
        }
    }
}

/** Três botões de dificuldade com estrelas. Usado em Ajustes e no Cadastro de carta. */
@Composable
fun SeletorDificuldade(
    selecionada: Dificuldade,
    aoSelecionar: (Dificuldade) -> Unit,
    modifier: Modifier = Modifier,
    mostrarMultiplicador: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Dificuldade.entries.forEach { dificuldade ->
            val ativa = dificuldade == selecionada
            Column(
                modifier = Modifier
                    .weight(1f)
                    // A opção escolhida fica "afundada": desce e perde a sombra.
                    .padding(top = if (ativa) 4.dp else 0.dp, bottom = if (ativa) 0.dp else 4.dp)
                    .cartoon(
                        cor = if (ativa) Amarelo else Branco,
                        forma = RoundedCornerShape(16.dp),
                        sombra = if (ativa) 0.dp else 4.dp,
                    )
                    .selectable(selected = ativa, role = Role.RadioButton, onClick = { aoSelecionar(dificuldade) })
                    .padding(vertical = 10.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    repeat(dificuldade.estrelas) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            tint = if (ativa) Tinta else Laranja,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
                Text(dificuldade.nome, style = MaterialTheme.typography.titleMedium, color = Tinta)
                if (mostrarMultiplicador) {
                    Text("pontos ${dificuldade.multiplicador}", style = MaterialTheme.typography.bodySmall, color = Tinta)
                }
            }
        }
    }
}
