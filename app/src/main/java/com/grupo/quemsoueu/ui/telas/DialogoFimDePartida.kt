package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.CampoCartoon
import com.grupo.quemsoueu.ui.componentes.CartaCartoon
import com.grupo.quemsoueu.ui.componentes.EstiloBotao
import com.grupo.quemsoueu.ui.componentes.TituloCartoon
import com.grupo.quemsoueu.ui.modelos.ResultadoPartidaUi
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.theme.Rosa
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave

/**
 * Janela de fim de partida: mostra quem era, os pontos e pede o nome para o Ranking.
 *
 * Quem liga: Matheus Rodrigues (mostra quando o jogo termina e salva a partida em onSalvar).
 */
@Composable
fun DialogoFimDePartida(
    resultado: ResultadoPartidaUi,
    onSalvar: (nome: String) -> Unit,
    onJogarDeNovo: () -> Unit,
    salvando: Boolean = false,
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var erroNome by rememberSaveable { mutableStateOf<String?>(null) }

    fun salvar() {
        if (nome.isBlank()) {
            erroNome = "Digite seu nome para entrar no ranking"
        } else {
            onSalvar(nome.trim())
        }
    }

    Dialog(
        onDismissRequest = onJogarDeNovo,
        properties = DialogProperties(dismissOnClickOutside = false),
    ) {
        CartaCartoon(modifier = Modifier.fillMaxWidth(), espacamento = 12) {
            TituloCartoon(
                texto = if (resultado.venceu) "Acertou!" else "Fim de jogo",
                tamanho = 40.sp,
                cor = if (resultado.venceu) Amarelo else Rosa,
                alinhamento = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text("A carta era", style = MaterialTheme.typography.bodyMedium, color = TextoSuave)
                Text(resultado.personagem, style = MaterialTheme.typography.headlineMedium, color = Tinta)
                Text("${resultado.pontos} pontos", style = MaterialTheme.typography.titleLarge, color = Tinta)
            }
            CampoCartoon(
                valor = nome,
                aoMudar = {
                    nome = it
                    erroNome = null
                },
                rotulo = "Seu nome no ranking",
                dica = "Ex.: Ana",
                erro = erroNome,
                acaoTeclado = ImeAction.Done,
                aoConfirmar = { salvar() },
            )
            BotaoCartoon(
                texto = "Salvar no ranking",
                onClick = { salvar() },
                icone = Icons.Rounded.EmojiEvents,
                estilo = EstiloBotao.VERDE,
                carregando = salvando,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            )
            BotaoCartoon(
                texto = "Jogar de novo sem salvar",
                onClick = onJogarDeNovo,
                estilo = EstiloBotao.BRANCO,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun DialogoFimDePartidaPreview() {
    QuemSouEuTema {
        DialogoFimDePartida(
            resultado = ResultadoPartidaUi(venceu = true, personagem = "Girafa", pontos = 162),
            onSalvar = {},
            onJogarDeNovo = {},
        )
    }
}
