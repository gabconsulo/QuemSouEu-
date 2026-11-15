package com.grupo.quemsoueu.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Lilas
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave

// Os três estados que toda lista do app precisa ter além de "pronto":
// carregando, vazio e erro. (Fase 2: Ranking, Painel Admin e IA.)

/** Enquanto os dados chegam do Room ou do Firestore. */
@Composable
fun EstadoCarregando(modifier: Modifier = Modifier, texto: String = "Carregando…") {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = Amarelo,
            trackColor = Lilas,
            strokeWidth = 6.dp,
        )
        Text(texto, style = MaterialTheme.typography.bodyLarge, color = Branco)
    }
}

/**
 * Lista sem nada. Sempre diz o que fazer em seguida, com um botão quando houver uma ação.
 */
@Composable
fun EstadoVazio(
    titulo: String,
    mensagem: String,
    modifier: Modifier = Modifier,
    icone: ImageVector = Icons.Rounded.Inbox,
    textoBotao: String? = null,
    onBotao: (() -> Unit)? = null,
) {
    CartaCartoon(modifier = modifier.fillMaxWidth(), espacamento = 10) {
        Icon(icone, contentDescription = null, tint = TextoSuave, modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally))
        Text(
            titulo,
            style = MaterialTheme.typography.titleLarge,
            color = Tinta,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            mensagem,
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSuave,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        if (textoBotao != null && onBotao != null) {
            BotaoCartoon(texto = textoBotao, onClick = onBotao, modifier = Modifier.fillMaxWidth().padding(top = 6.dp))
        }
    }
}

/** Falha ao carregar. Diz o que aconteceu e oferece tentar de novo; os dados já mostrados não somem. */
@Composable
fun EstadoErro(
    mensagem: String,
    onTentarDeNovo: () -> Unit,
    modifier: Modifier = Modifier,
    titulo: String = "Não deu para carregar",
) {
    CartaCartoon(
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Assertive },
        espacamento = 10,
    ) {
        Icon(Icons.Rounded.CloudOff, contentDescription = null, tint = Tinta, modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally))
        Text(
            titulo,
            style = MaterialTheme.typography.titleLarge,
            color = Tinta,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            mensagem,
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSuave,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        BotaoCartoon(texto = "Tentar de novo", onClick = onTentarDeNovo, modifier = Modifier.fillMaxWidth().padding(top = 6.dp))
    }
}
