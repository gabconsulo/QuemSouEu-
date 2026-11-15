package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.CampoCartoon
import com.grupo.quemsoueu.ui.componentes.CartaCartoon
import com.grupo.quemsoueu.ui.componentes.EstadoCarregando
import com.grupo.quemsoueu.ui.componentes.EstadoErro
import com.grupo.quemsoueu.ui.componentes.EstadoVazio
import com.grupo.quemsoueu.ui.componentes.EstiloBotao
import com.grupo.quemsoueu.ui.componentes.PillCartoon
import com.grupo.quemsoueu.ui.componentes.TituloCartoon
import com.grupo.quemsoueu.ui.componentes.TopoTela
import com.grupo.quemsoueu.ui.componentes.cartoon
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.DadosExemplo
import com.grupo.quemsoueu.ui.modelos.OrdemRanking
import com.grupo.quemsoueu.ui.modelos.PartidaUi
import com.grupo.quemsoueu.ui.modelos.formatarTempo
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Laranja
import com.grupo.quemsoueu.ui.theme.Lilas
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.Pele
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave

/**
 * Tela 4 · Ranking.
 *
 * A tela mostra [partidas] na ordem em que chegam. Filtrar e ordenar é lógica do Gabriel:
 * ele recebe o filtro/ordem escolhidos em onFiltroChange/onOrdemChange e devolve a lista pronta.
 */
@Composable
fun TelaRanking(
    partidas: List<PartidaUi>,
    filtro: Categoria?,
    onFiltroChange: (Categoria?) -> Unit,
    ordem: OrdemRanking,
    onOrdemChange: (OrdemRanking) -> Unit,
    onEditarNome: (partida: PartidaUi, novoNome: String) -> Unit,
    onExcluir: (PartidaUi) -> Unit,
    onJogar: () -> Unit,
    modifier: Modifier = Modifier,
    carregando: Boolean = false,
    erro: String? = null,
    onTentarDeNovo: () -> Unit = {},
) {
    var partidaEmEdicao by remember { mutableStateOf<PartidaUi?>(null) }
    val mostrarPodio = ordem == OrdemRanking.PONTOS && partidas.size >= 3

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .fundoJogo(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            TopoTela(
                titulo = "Ranking",
                acao = { MenuOrdem(ordem = ordem, onOrdemChange = onOrdemChange) },
            )
        }
        item {
            FiltrosCategoria(
                filtro = filtro,
                onFiltroChange = onFiltroChange,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
        }

        when {
            carregando -> item { EstadoCarregando(texto = "Carregando o ranking…") }

            erro != null -> item {
                EstadoErro(
                    mensagem = erro,
                    onTentarDeNovo = onTentarDeNovo,
                    modifier = Modifier.padding(horizontal = 18.dp),
                )
            }

            partidas.isEmpty() -> item {
                if (filtro == null) {
                    EstadoVazio(
                        titulo = "Nenhuma partida ainda",
                        mensagem = "Jogue uma partida e salve seu nome para aparecer aqui.",
                        icone = Icons.Rounded.SportsEsports,
                        textoBotao = "Jogar agora",
                        onBotao = onJogar,
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                } else {
                    EstadoVazio(
                        titulo = "Nenhuma partida em ${filtro.nome}",
                        mensagem = "Ninguém salvou uma partida desta categoria ainda.",
                        textoBotao = "Ver todas",
                        onBotao = { onFiltroChange(null) },
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }
            }

            else -> {
                if (mostrarPodio) {
                    item {
                        Podio(
                            primeiro = partidas[0],
                            segundo = partidas[1],
                            terceiro = partidas[2],
                            onEscolher = { partidaEmEdicao = it },
                            modifier = Modifier.padding(horizontal = 18.dp),
                        )
                    }
                }
                val inicio = if (mostrarPodio) 3 else 0
                val resto = partidas.drop(inicio)
                itemsIndexed(resto, key = { _, partida -> partida.id }) { indice, partida ->
                    ItemRanking(
                        posicao = inicio + indice + 1,
                        partida = partida,
                        onEditar = { partidaEmEdicao = partida },
                        onExcluir = { onExcluir(partida) },
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }
            }
        }
        item { Box(Modifier.height(16.dp)) }
    }

    partidaEmEdicao?.let { partida ->
        DialogoEditarPartida(
            partida = partida,
            onSalvar = { novoNome ->
                onEditarNome(partida, novoNome)
                partidaEmEdicao = null
            },
            onExcluir = {
                onExcluir(partida)
                partidaEmEdicao = null
            },
            onFechar = { partidaEmEdicao = null },
        )
    }
}

@Composable
private fun MenuOrdem(ordem: OrdemRanking, onOrdemChange: (OrdemRanking) -> Unit) {
    var aberto by remember { mutableStateOf(false) }
    Box {
        PillCartoon(
            texto = ordem.rotulo,
            icone = Icons.Rounded.KeyboardArrowDown,
            onClick = { aberto = true },
            modifier = Modifier.semantics { contentDescription = "Ordenar por ${ordem.rotulo}. Toque para mudar." },
        )
        DropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }) {
            OrdemRanking.entries.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(opcao.rotulo, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onOrdemChange(opcao)
                        aberto = false
                    },
                )
            }
        }
    }
}

/** Filtros "Todas, Famosos, Animais…". Rola para o lado em telas estreitas. */
@Composable
fun FiltrosCategoria(
    filtro: Categoria?,
    onFiltroChange: (Categoria?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PillCartoon(texto = "Todas", selecionada = filtro == null, onClick = { onFiltroChange(null) })
        Categoria.entries.forEach { categoria ->
            PillCartoon(
                texto = categoria.nome,
                cor = categoria.cor,
                selecionada = filtro == categoria,
                onClick = { onFiltroChange(categoria) },
            )
        }
    }
}

/** Pódio dos 3 primeiros: 2º à esquerda, 1º no meio (mais alto) e 3º à direita. */
@Composable
private fun Podio(
    primeiro: PartidaUi,
    segundo: PartidaUi,
    terceiro: PartidaUi,
    onEscolher: (PartidaUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DegrauPodio(segundo, 2, 92, Lilas, onEscolher, Modifier.weight(1f))
            DegrauPodio(primeiro, 1, 124, Amarelo, onEscolher, Modifier.weight(1f))
            DegrauPodio(terceiro, 3, 72, Laranja, onEscolher, Modifier.weight(1f))
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Tinta),
        )
    }
}

@Composable
private fun DegrauPodio(
    partida: PartidaUi,
    posicao: Int,
    alturaDp: Int,
    cor: Color,
    onEscolher: (PartidaUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(role = Role.Button, onClickLabel = "Editar ou excluir") { onEscolher(partida) }
            .semantics(mergeDescendants = true) {
                contentDescription = "${posicao}º lugar: ${partida.nomeJogador}, ${partida.pontuacao} pontos"
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Avatar(partida.nomeJogador, tamanho = 56)
        Text(
            partida.nomeJogador,
            style = MaterialTheme.typography.labelLarge,
            color = Branco,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaDp.dp)
                .background(cor, RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .border(3.dp, Tinta, RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .padding(top = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TituloCartoon(texto = "${posicao}º", tamanho = 30.sp, alinhamento = TextAlign.Center)
            Text("${partida.pontuacao} pts", style = MaterialTheme.typography.labelLarge, color = Tinta)
        }
    }
}

@Composable
private fun Avatar(nome: String, tamanho: Int) {
    Box(
        modifier = Modifier
            .size(tamanho.dp)
            .cartoon(cor = Pele, forma = CircleShape, sombra = 0.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            nome.take(1).uppercase(),
            style = TextStyle(fontFamily = Lilita, fontSize = (tamanho * 0.42f).sp),
            color = Tinta,
        )
    }
}

@Composable
private fun ItemRanking(
    posicao: Int,
    partida: PartidaUi,
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .cartoon()
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            "${posicao}º",
            style = TextStyle(fontFamily = Lilita, fontSize = 22.sp),
            color = Tinta,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.Center,
        )
        Column(Modifier.weight(1f)) {
            Text(partida.nomeJogador, style = MaterialTheme.typography.bodyLarge, color = Tinta)
            Text(
                "${partida.personagem} (${partida.categoria.nome}) · ${partida.dicasUsadas} dicas · " +
                    "${partida.perguntasIa} perg. IA · ${formatarTempo(partida.tempoSegundos)}",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSuave,
            )
        }
        Text("${partida.pontuacao}", style = TextStyle(fontFamily = Lilita, fontSize = 18.sp), color = Tinta)
        IconButton(onClick = onEditar) {
            Icon(Icons.Rounded.Edit, contentDescription = "Editar nome de ${partida.nomeJogador}", tint = TextoSuave)
        }
        IconButton(onClick = onExcluir) {
            Icon(Icons.Rounded.Delete, contentDescription = "Excluir partida de ${partida.nomeJogador}", tint = TextoSuave)
        }
    }
}

/** Janela para corrigir o nome ou excluir uma partida. */
@Composable
private fun DialogoEditarPartida(
    partida: PartidaUi,
    onSalvar: (String) -> Unit,
    onExcluir: () -> Unit,
    onFechar: () -> Unit,
) {
    var nome by rememberSaveable(partida.id) { mutableStateOf(partida.nomeJogador) }
    var erroNome by rememberSaveable(partida.id) { mutableStateOf<String?>(null) }

    fun salvar() {
        if (nome.isBlank()) erroNome = "O nome não pode ficar vazio" else onSalvar(nome.trim())
    }

    Dialog(onDismissRequest = onFechar) {
        CartaCartoon(modifier = Modifier.fillMaxWidth(), espacamento = 12) {
            Text("Editar partida", style = MaterialTheme.typography.headlineMedium, color = Tinta)
            Text(
                "${partida.personagem} · ${partida.pontuacao} pontos",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSuave,
            )
            CampoCartoon(
                valor = nome,
                aoMudar = {
                    nome = it
                    erroNome = null
                },
                rotulo = "Nome no ranking",
                erro = erroNome,
                acaoTeclado = ImeAction.Done,
                aoConfirmar = { salvar() },
            )
            BotaoCartoon(texto = "Salvar nome", onClick = { salvar() }, estilo = EstiloBotao.VERDE, modifier = Modifier.fillMaxWidth())
            BotaoCartoon(texto = "Excluir partida", onClick = onExcluir, estilo = EstiloBotao.ROSA, icone = Icons.Rounded.Delete, modifier = Modifier.fillMaxWidth())
            BotaoCartoon(texto = "Cancelar", onClick = onFechar, estilo = EstiloBotao.BRANCO, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun TelaRankingPreview() {
    QuemSouEuTema {
        TelaRanking(
            partidas = DadosExemplo.partidas,
            filtro = null,
            onFiltroChange = {},
            ordem = OrdemRanking.PONTOS,
            onOrdemChange = {},
            onEditarNome = { _, _ -> },
            onExcluir = {},
            onJogar = {},
        )
    }
}

@Preview(widthDp = 390, heightDp = 600)
@Composable
private fun TelaRankingVaziaPreview() {
    QuemSouEuTema {
        TelaRanking(
            partidas = emptyList(),
            filtro = null,
            onFiltroChange = {},
            ordem = OrdemRanking.PONTOS,
            onOrdemChange = {},
            onEditarNome = { _, _ -> },
            onExcluir = {},
            onJogar = {},
        )
    }
}
