package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.componentes.AvisoCartoon
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.CampoCartoon
import com.grupo.quemsoueu.ui.componentes.CartaCartoon
import com.grupo.quemsoueu.ui.componentes.EstiloBotao
import com.grupo.quemsoueu.ui.componentes.PillCartoon
import com.grupo.quemsoueu.ui.componentes.SeletorDificuldade
import com.grupo.quemsoueu.ui.componentes.TipoAviso
import com.grupo.quemsoueu.ui.componentes.TopoTela
import com.grupo.quemsoueu.ui.componentes.cartoon
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.DadosExemplo
import com.grupo.quemsoueu.ui.modelos.Dificuldade
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.CoresDicas
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave
import com.grupo.quemsoueu.ui.theme.Verde

private const val QUANTIDADE_DICAS = 5

/**
 * Tela 7 · Cadastro / edição de carta.
 *
 * - [cartaInicial] nula = carta nova.
 * - Salvar: Gustavo recebe a carta pronta em onSalvar.
 * - Gerar dicas com IA: Gabriel recebe nome/categoria/nível em onGerarDicas e devolve
 *   as dicas em [dicasGeradas] (a tela preenche os 5 campos para o Admin revisar).
 */
@Composable
fun TelaCadastroCarta(
    cartaInicial: CartaUi?,
    onVoltar: () -> Unit,
    onSalvar: (CartaUi) -> Unit,
    onGerarDicas: (nome: String, categoria: Categoria, dificuldade: Dificuldade) -> Unit,
    modifier: Modifier = Modifier,
    gerandoDicas: Boolean = false,
    dicasGeradas: List<String>? = null,
    erroIa: String? = null,
    salvando: Boolean = false,
) {
    var nome by remember(cartaInicial) { mutableStateOf(cartaInicial?.nome ?: "") }
    var categoria by remember(cartaInicial) { mutableStateOf(cartaInicial?.categoria ?: Categoria.ANIMAIS) }
    var dificuldade by remember(cartaInicial) { mutableStateOf(cartaInicial?.dificuldade ?: Dificuldade.FACIL) }
    var ativa by remember(cartaInicial) { mutableStateOf(cartaInicial?.ativa ?: true) }
    val dicas = remember(cartaInicial) {
        mutableStateListOf(*Array(QUANTIDADE_DICAS) { indice -> cartaInicial?.dicas?.getOrNull(indice) ?: "" })
    }
    var erroNome by remember { mutableStateOf<String?>(null) }
    var mostrarErrosDicas by remember { mutableStateOf(false) }
    var dicasDaIa by remember { mutableStateOf(false) }

    // Quando a IA devolve as dicas, elas preenchem os campos (o Admin ainda pode editar).
    LaunchedEffect(dicasGeradas) {
        if (dicasGeradas != null) {
            for (indice in 0 until QUANTIDADE_DICAS) {
                dicas[indice] = dicasGeradas.getOrNull(indice) ?: ""
            }
            dicasDaIa = true
            mostrarErrosDicas = false
        }
    }

    fun salvar() {
        erroNome = if (nome.isBlank()) "Escreva quem é a carta (a resposta do jogo)" else null
        mostrarErrosDicas = dicas.any { it.isBlank() }
        if (erroNome == null && !mostrarErrosDicas) {
            onSalvar(
                CartaUi(
                    id = cartaInicial?.id ?: "",
                    nome = nome.trim(),
                    categoria = categoria,
                    dificuldade = dificuldade,
                    dicas = dicas.map { it.trim() },
                    ativa = ativa,
                ),
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .fundoJogo()
            .verticalScroll(rememberScrollState())
            .imePadding(),
    ) {
        TopoTela(titulo = if (cartaInicial == null) "Nova carta" else "Editar carta", onVoltar = onVoltar)

        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            CartaCartoon(modifier = Modifier.fillMaxWidth(), espacamento = 12) {
                CampoCartoon(
                    valor = nome,
                    aoMudar = {
                        nome = it
                        erroNome = null
                    },
                    rotulo = "Quem é? (a resposta)",
                    dica = "Ex.: Pinguim",
                    erro = erroNome,
                    acaoTeclado = ImeAction.Next,
                )
                Text("Categoria", style = MaterialTheme.typography.labelLarge, color = Tinta)
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Categoria.entries.forEach { opcao ->
                        PillCartoon(
                            texto = opcao.nome,
                            cor = opcao.cor,
                            selecionada = opcao == categoria,
                            icone = if (opcao == categoria) Icons.Rounded.Check else null,
                            onClick = { categoria = opcao },
                        )
                    }
                }
                Text("Nível", style = MaterialTheme.typography.labelLarge, color = Tinta)
                SeletorDificuldade(
                    selecionada = dificuldade,
                    aoSelecionar = { dificuldade = it },
                    mostrarMultiplicador = false,
                )
            }

            BotaoCartoon(
                texto = if (gerandoDicas) "Gerando dicas…" else "Gerar dicas com IA",
                onClick = {
                    if (nome.isBlank()) {
                        erroNome = "Escreva a resposta antes de pedir dicas à IA"
                    } else {
                        onGerarDicas(nome.trim(), categoria, dificuldade)
                    }
                },
                icone = Icons.Rounded.AutoAwesome,
                estilo = EstiloBotao.AZUL,
                carregando = gerandoDicas,
                modifier = Modifier.fillMaxWidth(),
            )
            if (erroIa != null) {
                AvisoCartoon(texto = erroIa, tipo = TipoAviso.ERRO)
            }

            CartaCartoon(modifier = Modifier.fillMaxWidth(), espacamento = 10) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Dicas", style = MaterialTheme.typography.titleLarge, color = Tinta, modifier = Modifier.weight(1f))
                    if (dicasDaIa) {
                        PillCartoon(texto = "Sugeridas pela IA", icone = Icons.Rounded.AutoAwesome)
                    }
                }
                Text(
                    "Da mais difícil para a mais fácil. Revise antes de salvar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSuave,
                )
                for (indice in 0 until QUANTIDADE_DICAS) {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .size(34.dp)
                                .cartoon(cor = CoresDicas[indice], forma = CircleShape, sombra = 0.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("${indice + 1}", style = TextStyle(fontFamily = Lilita, fontSize = 17.sp), color = Tinta)
                        }
                        CampoCartoon(
                            valor = dicas[indice],
                            aoMudar = { dicas[indice] = it },
                            dica = "Dica ${indice + 1}",
                            erro = if (mostrarErrosDicas && dicas[indice].isBlank()) "Preencha a dica ${indice + 1}" else null,
                            acaoTeclado = if (indice < QUANTIDADE_DICAS - 1) ImeAction.Next else ImeAction.Done,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                LinhaCartaAtiva(ativa = ativa, aoMudar = { ativa = it })
            }

            if (mostrarErrosDicas) {
                AvisoCartoon(texto = "Toda carta precisa das 5 dicas para entrar no jogo.", tipo = TipoAviso.ERRO)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BotaoCartoon(texto = "Cancelar", onClick = onVoltar, estilo = EstiloBotao.BRANCO, modifier = Modifier.weight(1f))
                BotaoCartoon(
                    texto = "Salvar carta",
                    onClick = { salvar() },
                    estilo = EstiloBotao.VERDE,
                    carregando = salvando,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LinhaCartaAtiva(ativa: Boolean, aoMudar: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .toggleable(value = ativa, role = Role.Checkbox, onValueChange = aoMudar)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .cartoon(cor = if (ativa) Verde else Branco, forma = RoundedCornerShape(8.dp), sombra = 0.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (ativa) Icon(Icons.Rounded.Check, contentDescription = null, tint = Tinta, modifier = Modifier.size(18.dp))
        }
        Column {
            Text("Carta ativa no jogo", style = MaterialTheme.typography.bodyLarge, color = Tinta)
            Text("Desligue para pausar a carta sem apagar", style = MaterialTheme.typography.bodySmall, color = TextoSuave)
        }
    }
}

@Preview(widthDp = 390, heightDp = 1100)
@Composable
private fun TelaCadastroCartaPreview() {
    QuemSouEuTema {
        TelaCadastroCarta(
            cartaInicial = DadosExemplo.cartas[4],
            onVoltar = {},
            onSalvar = {},
            onGerarDicas = { _, _, _ -> },
        )
    }
}
