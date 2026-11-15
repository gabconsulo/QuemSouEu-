package com.grupo.quemsoueu.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.ErroTexto
import com.grupo.quemsoueu.ui.theme.Lilas
import com.grupo.quemsoueu.ui.theme.Tinta
import com.grupo.quemsoueu.ui.theme.TextoSuave

/**
 * Campo de texto do jogo: o OutlinedTextField das Aulas 5–6 com as cores do tema.
 * O rótulo fica em cima do campo e a mensagem de erro logo embaixo.
 *
 * @param rotuloNoRoxo use true quando o campo está direto no fundo roxo (rótulo branco).
 */
@Composable
fun CampoCartoon(
    valor: String,
    aoMudar: (String) -> Unit,
    modifier: Modifier = Modifier,
    rotulo: String? = null,
    rotuloNoRoxo: Boolean = false,
    dica: String? = null,
    erro: String? = null,
    senha: Boolean = false,
    iconeInicio: ImageVector? = null,
    habilitado: Boolean = true,
    acaoTeclado: ImeAction = ImeAction.Default,
    aoConfirmar: (() -> Unit)? = null,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (rotulo != null) {
            Text(
                text = rotulo,
                style = MaterialTheme.typography.labelLarge,
                color = if (rotuloNoRoxo) Branco else Tinta,
            )
        }
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { if (erro != null) error(erro) },
            enabled = habilitado,
            placeholder = if (dica != null) {
                { Text(dica) }
            } else {
                null
            },
            leadingIcon = if (iconeInicio != null) {
                { Icon(iconeInicio, contentDescription = null) }
            } else {
                null
            },
            isError = erro != null,
            singleLine = true,
            visualTransformation = if (senha) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (senha) KeyboardType.Password else KeyboardType.Text,
                imeAction = acaoTeclado,
            ),
            keyboardActions = KeyboardActions(onAny = { aoConfirmar?.invoke() }),
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Tinta,
                unfocusedTextColor = Tinta,
                disabledTextColor = TextoSuave,
                errorTextColor = Tinta,
                focusedContainerColor = Branco,
                unfocusedContainerColor = Branco,
                disabledContainerColor = Lilas,
                errorContainerColor = Branco,
                cursorColor = Tinta,
                errorCursorColor = ErroTexto,
                focusedBorderColor = Tinta,
                unfocusedBorderColor = Tinta,
                disabledBorderColor = Tinta,
                errorBorderColor = ErroTexto,
                focusedLeadingIconColor = Tinta,
                unfocusedLeadingIconColor = TextoSuave,
                focusedPlaceholderColor = TextoSuave,
                unfocusedPlaceholderColor = TextoSuave,
                disabledPlaceholderColor = TextoSuave,
                errorPlaceholderColor = TextoSuave,
            ),
        )
        if (erro != null) {
            MensagemErroCampo(erro)
        }
    }
}
