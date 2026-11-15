package com.grupo.quemsoueu.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grupo.quemsoueu.ui.componentes.AvisoCartoon
import com.grupo.quemsoueu.ui.componentes.BotaoCartoon
import com.grupo.quemsoueu.ui.componentes.CampoCartoon
import com.grupo.quemsoueu.ui.componentes.CartaCartoon
import com.grupo.quemsoueu.ui.componentes.TipoAviso
import com.grupo.quemsoueu.ui.componentes.TopoTela
import com.grupo.quemsoueu.ui.componentes.cartoon
import com.grupo.quemsoueu.ui.componentes.fundoJogo
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Branco
import com.grupo.quemsoueu.ui.theme.QuemSouEuTema
import com.grupo.quemsoueu.ui.theme.Tinta

/**
 * Tela 5 · Login do Admin.
 *
 * A tela só confere se os campos estão preenchidos. Se usuário e senha estão CORRETOS
 * quem decide é o Matheus Rodrigues (LoginViewModel): ele recebe os dois em onEntrar
 * e, se estiverem errados, devolve a mensagem em [erro].
 */
@Composable
fun TelaLoginAdmin(
    onVoltar: () -> Unit,
    onEntrar: (usuario: String, senha: String) -> Unit,
    modifier: Modifier = Modifier,
    erro: String? = null,
    carregando: Boolean = false,
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var erroUsuario by rememberSaveable { mutableStateOf<String?>(null) }
    var erroSenha by rememberSaveable { mutableStateOf<String?>(null) }

    // O botão nunca fica desabilitado: ao tocar, a tela explica o que falta (regra de UX).
    fun tentarEntrar() {
        erroUsuario = if (usuario.isBlank()) "Digite o usuário" else null
        erroSenha = if (senha.isBlank()) "Digite a senha" else null
        if (erroUsuario == null && erroSenha == null) {
            onEntrar(usuario.trim(), senha)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .fundoJogo()
            .verticalScroll(rememberScrollState())
            .imePadding(),
    ) {
        TopoTela(titulo = "Área do Admin", onVoltar = onVoltar)
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .rotate(-5f)
                    .padding(bottom = 6.dp)
                    .size(96.dp)
                    .cartoon(cor = Amarelo, forma = RoundedCornerShape(28.dp), sombra = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Lock, contentDescription = null, tint = Tinta, modifier = Modifier.size(48.dp))
            }
            Text(
                "Só quem cuida das cartas entra aqui.",
                style = MaterialTheme.typography.bodyLarge,
                color = Branco,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 300.dp),
            )

            CartaCartoon(modifier = Modifier.fillMaxWidth(), espacamento = 14) {
                CampoCartoon(
                    valor = usuario,
                    aoMudar = {
                        usuario = it
                        erroUsuario = null
                    },
                    rotulo = "Usuário",
                    erro = erroUsuario,
                    acaoTeclado = ImeAction.Next,
                )
                CampoCartoon(
                    valor = senha,
                    aoMudar = {
                        senha = it
                        erroSenha = null
                    },
                    rotulo = "Senha",
                    senha = true,
                    erro = erroSenha,
                    acaoTeclado = ImeAction.Done,
                    aoConfirmar = { tentarEntrar() },
                )
                if (erro != null) {
                    AvisoCartoon(texto = erro, tipo = TipoAviso.ERRO)
                }
                BotaoCartoon(
                    texto = "Entrar",
                    onClick = { tentarEntrar() },
                    carregando = carregando,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                )
            }
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun TelaLoginAdminPreview() {
    QuemSouEuTema {
        TelaLoginAdmin(onVoltar = {}, onEntrar = { _, _ -> }, erro = "Usuário ou senha incorretos. Tente de novo.")
    }
}
