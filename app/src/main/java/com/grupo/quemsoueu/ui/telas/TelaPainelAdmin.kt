package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.BotaoIconeCartoon
import com.grupo.quemsoueu.ui.componentes.CampoCartoon
import com.grupo.quemsoueu.ui.componentes.EstadoCarregando
import com.grupo.quemsoueu.ui.componentes.EstadoErro
import com.grupo.quemsoueu.ui.componentes.EstadoVazio
import com.grupo.quemsoueu.ui.componentes.PillCartoon
import com.grupo.quemsoueu.ui.componentes.SeloCartoon
import com.grupo.quemsoueu.ui.componentes.TopoTela
import com.grupo.quemsoueu.ui.componentes.cartoon
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.DadosExemplo
import com.grupo.quemsoueu.ui.theme.Azul
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Lilas
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave
import com.grupo.quemsoueu.ui.theme.Verde

/**
 * Tela 6 · Painel Admin ("Cartas do jogo").
 *
 * Quem liga: Gustavo. Busca, filtro e ordenação são lógica dele; a tela mostra [cartas]
 * já filtradas e avisa o que a pessoa digitou/escolheu.
 */
@Composable
fun TelaPainelAdmin(
    cartas: List<CartaUi>,
    totalCartas: Int,
    busca: String,
    onBuscaChange: (String) -> Unit,
    filtro: Categoria?,
    onFiltroChange: (Categoria?) -> Unit,
    onSincronizar: () -> Unit,
    onNovaCarta: () -> Unit,
    onEditar: (CartaUi) -> Unit,
    onExcluir: (CartaUi) -> Unit,
    onSair: () -> Unit,
    modifier: Modifier = Modifier,
    textoSincronizacao: String = "Atualizado agora",
    sincronizando: Boolean = false,
    carregando: Boolean = false,
    erro: String? = null,
    onTentarDeNovo: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .fundoJogo(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                TopoTela(
                    titulo = "Cartas do jogo",
                    acao = { BotaoIconeCartoon(Icons.AutoMirrored.Rounded.Logout, descricao = "Sair da área do Admin", onClick = onSair) },
                )
            }
            item {
                CaixaSincronizacao(
                    texto = textoSincronizacao,
                    sincronizando = sincronizando,
                    onSincronizar = onSincronizar,
                    modifier = Modifier.padding(horizontal = 18.dp),
                )
            }
            item {
                CampoCartoon(
                    valor = busca,
                    aoMudar = onBuscaChange,
                    dica = "Buscar carta",
                    iconeInicio = Icons.Rounded.Search,
                    acaoTeclado = ImeAction.Search,
                    modifier = Modifier.padding(horizontal = 18.dp),
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
                carregando -> item { EstadoCarregando(texto = "Carregando as cartas…") }

                erro != null -> item {
                    EstadoErro(
                        mensagem = erro,
                        onTentarDeNovo = onTentarDeNovo,
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }

                cartas.isEmpty() && (busca.isNotBlank() || filtro != null) -> item {
                    EstadoVazio(
                        titulo = "Nenhuma carta encontrada",
                        mensagem = "Tente outro nome ou mostre todas as categorias.",
                        icone = Icons.Rounded.SearchOff,
                        textoBotao = "Limpar busca",
                        onBotao = {
                            onBuscaChange("")
                            onFiltroChange(null)
                        },
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }

                cartas.isEmpty() -> item {
                    EstadoVazio(
                        titulo = "Nenhuma carta ainda",
                        mensagem = "Crie a primeira carta para o jogo ter o que sortear.",
                        textoBotao = "Nova carta",
                        onBotao = onNovaCarta,
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }

                else -> {
                    item {
                        Text(
                            "${cartas.size} de $totalCartas cartas, de A a Z",
                            style = MaterialTheme.typography.labelLarge,
                            color = Branco,
                            modifier = Modifier.padding(horizontal = 18.dp),
                        )
                    }
                    items(cartas, key = { it.id }) { carta ->
                        ItemCarta(
                            carta = carta,
                            onEditar = { onEditar(carta) },
                            onExcluir = { onExcluir(carta) },
                            modifier = Modifier.padding(horizontal = 18.dp),
                        )
                    }
                }
            }
            // Espaço para o botão "Nova carta" não cobrir a última carta
            item { Box(Modifier.height(96.dp)) }
        }

        BotaoCartoon(
            texto = "Nova carta",
            onClick = onNovaCarta,
            icone = Icons.Rounded.Add,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 16.dp),
        )
    }
}

@Composable
private fun CaixaSincronizacao(
    texto: String,
    sincronizando: Boolean,
    onSincronizar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .cartoon(cor = Azul)
            .padding(start = 14.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                if (sincronizando) "Sincronizando com a nuvem…" else "Sincronizado com a nuvem",
                style = MaterialTheme.typography.labelLarge,
                color = Tinta,
            )
            Text(texto, style = MaterialTheme.typography.bodySmall, color = Tinta)
        }
        PillCartoon(
            texto = "Sincronizar",
            icone = Icons.Rounded.Refresh,
            onClick = { if (!sincronizando) onSincronizar() },
        )
    }
}

@Composable
private fun ItemCarta(
    carta: CartaUi,
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .cartoon()
            .height(IntrinsicSize.Min) // a faixa colorida acompanha a altura do texto
            .heightIn(min = 68.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Faixa com a cor da categoria e a inicial da carta
        Box(
            modifier = Modifier
                .width(56.dp)
                .fillMaxHeight()
                .background(carta.categoria.cor)
                .drawBehind {
                    drawLine(
                        color = Tinta,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3.dp.toPx(),
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(carta.nome.take(1).uppercase(), style = TextStyle(fontFamily = Lilita, fontSize = 26.sp), color = Tinta)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(carta.nome, style = MaterialTheme.typography.bodyLarge, color = Tinta)
                SeloCartoon(
                    texto = if (carta.ativa) "Ativa" else "Pausada",
                    cor = if (carta.ativa) Verde else Lilas,
                    tamanho = 12,
                )
            }
            Text(
                "${carta.categoria.nome} · ${carta.dificuldade.nome} · ${carta.dicas.size} dicas",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSuave,
            )
        }
        IconButton(onClick = onEditar) {
            Icon(Icons.Rounded.Edit, contentDescription = "Editar ${carta.nome}", tint = TextoSuave)
        }
        IconButton(onClick = onExcluir) {
            Icon(Icons.Rounded.Delete, contentDescription = "Excluir ${carta.nome}", tint = TextoSuave)
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun TelaPainelAdminPreview() {
    QuemSouEuTema {
        TelaPainelAdmin(
            cartas = DadosExemplo.cartas,
            totalCartas = 18,
            busca = "",
            onBuscaChange = {},
            filtro = null,
            onFiltroChange = {},
            onSincronizar = {},
            onNovaCarta = {},
            onEditar = {},
            onExcluir = {},
            onSair = {},
        )
    }
}
