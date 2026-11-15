package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.componentes.AvisoCartoon
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.CartaCartoon
import com.grupo.quemsoueu.ui.componentes.SeletorDificuldade
import com.grupo.quemsoueu.ui.componentes.TipoAviso
import com.grupo.quemsoueu.ui.componentes.TopoTela
import com.grupo.quemsoueu.ui.componentes.cartoon
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.modelos.AjustesUi
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.DadosExemplo
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Lilas
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave
import com.grupo.quemsoueu.ui.theme.Verde

/**
 * Tela 3 · Ajustes da partida.
 *
 * Quem liga: Matheus Rodrigues (guarda o [AjustesUi] recebido em onSalvar e usa no sorteio).
 */
@Composable
fun TelaAjustes(
    ajustesIniciais: AjustesUi,
    onSalvar: (AjustesUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    var categorias by remember(ajustesIniciais) { mutableStateOf(ajustesIniciais.categorias) }
    var dificuldade by remember(ajustesIniciais) { mutableStateOf(ajustesIniciais.dificuldade) }
    var cronometro by remember(ajustesIniciais) { mutableStateOf(ajustesIniciais.cronometro) }
    var perguntasIa by remember(ajustesIniciais) { mutableStateOf(ajustesIniciais.perguntasIa) }
    var erro by remember { mutableStateOf<String?>(null) }
    var salvo by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .fundoJogo()
            .verticalScroll(rememberScrollState()),
    ) {
        TopoTela(titulo = "Ajustes da partida")
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("Categorias", style = MaterialTheme.typography.labelLarge, color = Branco)
            // Grade 2 x 2 feita com Row/Column (Aulas 5–7)
            Categoria.entries.chunked(2).forEach { linha ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    linha.forEach { categoria ->
                        BlocoCategoria(
                            categoria = categoria,
                            ligada = categoria in categorias,
                            aoMudar = { ligada ->
                                categorias = if (ligada) categorias + categoria else categorias - categoria
                                erro = null
                                salvo = false
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            Text("Dificuldade", style = MaterialTheme.typography.labelLarge, color = Branco)
            SeletorDificuldade(
                selecionada = dificuldade,
                aoSelecionar = {
                    dificuldade = it
                    salvo = false
                },
            )

            CartaCartoon(modifier = Modifier.fillMaxWidth(), espacamento = 14) {
                LinhaInterruptor(
                    titulo = "Cronômetro",
                    descricao = "Bônus para quem acerta rápido",
                    ligado = cronometro,
                    aoMudar = {
                        cronometro = it
                        salvo = false
                    },
                )
                LinhaInterruptor(
                    titulo = "Perguntas à IA",
                    descricao = "Até 10 perguntas de sim ou não por partida",
                    ligado = perguntasIa,
                    aoMudar = {
                        perguntasIa = it
                        salvo = false
                    },
                )
            }

            if (erro != null) {
                AvisoCartoon(texto = erro ?: "", tipo = TipoAviso.ERRO)
            }
            if (salvo) {
                AvisoCartoon(texto = "Ajustes salvos. Valem a partir da próxima partida.", tipo = TipoAviso.SUCESSO)
            }

            BotaoCartoon(
                texto = "Salvar ajustes",
                onClick = {
                    if (categorias.isEmpty()) {
                        erro = "Escolha pelo menos uma categoria"
                    } else {
                        onSalvar(AjustesUi(categorias, dificuldade, cronometro, perguntasIa))
                        salvo = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** Bloco colorido de categoria que liga e desliga (funciona como um Checkbox). */
@Composable
private fun BlocoCategoria(
    categoria: Categoria,
    ligada: Boolean,
    aoMudar: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 5.dp)
                .height(76.dp)
                .cartoon(cor = if (ligada) categoria.cor else Lilas)
                .toggleable(value = ligada, role = Role.Checkbox, onValueChange = aoMudar),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                categoria.nome,
                style = TextStyle(fontFamily = Lilita, fontSize = 21.sp),
                color = Tinta,
            )
            if (!ligada) {
                Text("Fora da partida", style = MaterialTheme.typography.bodySmall, color = TextoSuave)
            }
        }
        if (ligada) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp)
                    .size(30.dp)
                    .cartoon(cor = Branco, forma = CircleShape, sombra = 0.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null, tint = Tinta, modifier = Modifier.size(18.dp))
            }
        }
    }
}

/** Linha inteira clicável com um Switch (fácil de acertar com o dedo). */
@Composable
private fun LinhaInterruptor(
    titulo: String,
    descricao: String,
    ligado: Boolean,
    aoMudar: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = ligado, role = Role.Switch, onValueChange = aoMudar),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.bodyLarge, color = Tinta)
            Text(descricao, style = MaterialTheme.typography.bodySmall, color = TextoSuave)
        }
        Switch(
            checked = ligado,
            onCheckedChange = null, // quem trata o toque é a linha inteira
            colors = SwitchDefaults.colors(
                checkedThumbColor = Branco,
                checkedTrackColor = Verde,
                checkedBorderColor = Tinta,
                uncheckedThumbColor = Branco,
                uncheckedTrackColor = Lilas,
                uncheckedBorderColor = Tinta,
            ),
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun TelaAjustesPreview() {
    QuemSouEuTema {
        TelaAjustes(ajustesIniciais = DadosExemplo.ajustes, onSalvar = {})
    }
}
