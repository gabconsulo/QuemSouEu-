package com.grupo.quemsoueu.ui.componentes

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Azul
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.Lilas
import com.grupo.quemsoueu.ui.theme.Lilita
import com.grupo.quemsoueu.ui.theme.Rosa
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.Verde

enum class EstiloBotao(val fundo: Color) {
    AMARELO(Amarelo),
    BRANCO(Branco),
    VERDE(Verde),
    AZUL(Azul),
    ROSA(Rosa),
}

/**
 * Botão "de borracha": quando a pessoa aperta, ele desce e a sombra some.
 * O texto do botão sempre diz o que acontece ("Salvar carta", não "OK").
 */
@Composable
fun BotaoCartoon(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null,
    estilo: EstiloBotao = EstiloBotao.AMARELO,
    grande: Boolean = false,
    carregando: Boolean = false,
) {
    val interacao = remember { MutableInteractionSource() }
    val pressionado by interacao.collectIsPressedAsState()
    val descida by animateDpAsState(
        targetValue = if (pressionado) 5.dp else 0.dp,
        label = "descidaBotao",
    )
    val alturaMinima = if (grande) 64.dp else 52.dp
    val tamanhoTexto = if (grande) 26.sp else 20.sp

    Box(
        modifier = modifier
            .padding(bottom = 6.dp) // espaço para a sombra
            .offset(y = descida)
            .cartoon(cor = estilo.fundo, sombra = 6.dp - descida)
            .clickable(
                interactionSource = interacao,
                indication = null,
                enabled = !carregando,
                role = Role.Button,
                onClick = onClick,
            )
            .heightIn(min = alturaMinima)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (carregando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Tinta,
                    strokeWidth = 3.dp,
                )
            } else if (icone != null) {
                Icon(
                    imageVector = icone,
                    contentDescription = null,
                    tint = Tinta,
                    modifier = Modifier.size(if (grande) 30.dp else 24.dp),
                )
            }
            Text(
                text = texto,
                color = Tinta,
                textAlign = TextAlign.Center,
                style = TextStyle(fontFamily = Lilita, fontSize = tamanhoTexto, letterSpacing = 0.5.sp),
            )
        }
    }
}

/** Botão quadrado só com ícone (voltar, sair, enviar). A descrição é lida pelo leitor de tela. */
@Composable
fun BotaoIconeCartoon(
    icone: ImageVector,
    descricao: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cor: Color = Branco,
    habilitado: Boolean = true,
) {
    Box(
        modifier = modifier
            .padding(bottom = 4.dp)
            .size(48.dp)
            .cartoon(
                cor = if (habilitado) cor else Lilas,
                forma = RoundedCornerShape(16.dp),
                sombra = 4.dp,
            )
            .clickable(enabled = habilitado, role = Role.Button, onClick = onClick)
            .semantics { contentDescription = descricao },
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icone, contentDescription = null, tint = Tinta)
    }
}
