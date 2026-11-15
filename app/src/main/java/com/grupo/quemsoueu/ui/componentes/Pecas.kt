package com.grupo.quemsoueu.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.modelos.RespostaIa
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Azul
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.ErroTexto
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.Rosa
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.Verde

/** Caixa branca com contorno e sombra: a base de dicas, listas e formulários. */
@Composable
fun CartaCartoon(
    modifier: Modifier = Modifier,
    cor: Color = Branco,
    preenchimento: PaddingValues = PaddingValues(14.dp),
    espacamento: Int = 8,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .padding(bottom = 5.dp)
            .cartoon(cor = cor)
            .padding(preenchimento),
        verticalArrangement = Arrangement.spacedBy(espacamento.dp),
        content = conteudo,
    )
}

/**
 * Etiqueta arredondada. Com onClick vira um filtro que pode ser escolhido
 * (fica amarela quando selecionada).
 */
@Composable
fun PillCartoon(
    texto: String,
    modifier: Modifier = Modifier,
    cor: Color = Branco,
    icone: ImageVector? = null,
    selecionada: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val fundo = if (selecionada) Amarelo else cor
    val clique = if (onClick != null) {
        Modifier
            .minimumInteractiveComponentSize()
            .selectable(selected = selecionada, role = Role.RadioButton, onClick = onClick)
    } else {
        Modifier
    }
    Row(
        modifier = modifier
            .then(clique)
            .clip(CircleShape)
            .background(fundo)
            .border(3.dp, Tinta, CircleShape)
            .heightIn(min = 34.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (icone != null) {
            Icon(icone, contentDescription = null, tint = Tinta, modifier = Modifier.size(18.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelLarge, color = Tinta, maxLines = 1)
    }
}

/** Selo inclinado, como um carimbo de papel. */
@Composable
fun SeloCartoon(texto: String, cor: Color, modifier: Modifier = Modifier, tamanho: Int = 15) {
    val forma = RoundedCornerShape(12.dp)
    Text(
        text = texto,
        color = Tinta,
        style = TextStyle(fontFamily = Lilita, fontSize = tamanho.sp, letterSpacing = 0.5.sp),
        modifier = modifier
            .rotate(-4f)
            .clip(forma)
            .background(cor)
            .border(3.dp, Tinta, forma)
            .padding(horizontal = 10.dp, vertical = 2.dp),
    )
}

/** Resposta da IA (Sim, Não ou Talvez). A palavra aparece sempre escrita, não só a cor. */
@Composable
fun CarimboResposta(resposta: RespostaIa, modifier: Modifier = Modifier) {
    SeloCartoon(texto = resposta.texto, cor = resposta.cor, modifier = modifier)
}

enum class TipoAviso(val fundo: Color, val icone: ImageVector) {
    ERRO(Rosa, Icons.Rounded.ErrorOutline),
    SUCESSO(Verde, Icons.Rounded.CheckCircle),
    INFO(Amarelo, Icons.Rounded.Info),
    IA(Azul, Icons.Rounded.AutoAwesome),
}

/**
 * Faixa de aviso (erro, sucesso, informação). O leitor de tela anuncia o texto
 * assim que ele aparece (liveRegion).
 */
@Composable
fun AvisoCartoon(texto: String, tipo: TipoAviso, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .cartoon(cor = tipo.fundo, forma = RoundedCornerShape(16.dp), sombra = 4.dp)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .semantics {
                liveRegion = if (tipo == TipoAviso.ERRO) LiveRegionMode.Assertive else LiveRegionMode.Polite
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(tipo.icone, contentDescription = null, tint = Tinta)
        Text(texto, style = MaterialTheme.typography.labelLarge, color = Tinta)
    }
}

/** Mensagem de erro embaixo de um campo. Fundo branco para ter contraste até sobre o roxo. */
@Composable
fun MensagemErroCampo(texto: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Branco)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(Icons.Rounded.ErrorOutline, contentDescription = null, tint = ErroTexto, modifier = Modifier.size(18.dp))
        Text(texto, style = MaterialTheme.typography.labelMedium, color = ErroTexto)
    }
}
