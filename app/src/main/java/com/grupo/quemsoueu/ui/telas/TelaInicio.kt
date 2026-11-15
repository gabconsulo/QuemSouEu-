package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.EstiloBotao
import com.grupo.quemsoueu.ui.componentes.Mascote
import com.grupo.quemsoueu.ui.componentes.TituloCartoon
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema

/**
 * Tela 1 · Início. Só dois caminhos: jogar ou entrar na área do Admin.
 *
 * Quem liga: Matheus Rodrigues (onJogar → rota "jogo", onEntrarAdmin → rota "login").
 */
@Composable
fun TelaInicio(
    onJogar: () -> Unit,
    onEntrarAdmin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .fundoJogo()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Mascote(Modifier.fillMaxWidth(0.6f))
            Spacer(Modifier.height(12.dp))
            TituloCartoon(
                texto = "Quem Sou Eu?",
                tamanho = 46.sp,
                alinhamento = TextAlign.Center,
                modifier = Modifier.rotate(-3f),
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Descubra quem está na carta antes que as dicas acabem!",
                style = MaterialTheme.typography.bodyLarge,
                color = Branco,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 300.dp),
            )
        }

        BotaoCartoon(
            texto = "Jogar agora",
            onClick = onJogar,
            icone = Icons.Rounded.SportsEsports,
            grande = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        BotaoCartoon(
            texto = "Entrar como Admin",
            onClick = onEntrarAdmin,
            icone = Icons.Rounded.Lock,
            estilo = EstiloBotao.BRANCO,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun TelaInicioPreview() {
    QuemSouEuTema {
        TelaInicio(onJogar = {}, onEntrarAdmin = {})
    }
}
