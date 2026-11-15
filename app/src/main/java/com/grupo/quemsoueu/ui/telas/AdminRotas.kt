package com.grupo.quemsoueu.ui.telas

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.quemsoueu.repository.FirestoreCartaRepository
import com.grupo.quemsoueu.ui.componentes.EstadoCarregando
import com.grupo.quemsoueu.viewmodel.PainelAdminViewModel
import com.grupo.quemsoueu.viewmodel.PainelAdminViewModelFactory

@Composable
fun PainelAdminRota(
    onNovaCarta: () -> Unit,
    onEditarCarta: (String) -> Unit,
    onSair: () -> Unit,
    viewModel: PainelAdminViewModel = viewModel(
        factory = PainelAdminViewModelFactory(FirestoreCartaRepository()),
    ),
) {
    val estado = viewModel.uiState.collectAsStateWithLifecycle().value
    TelaPainelAdmin(
        cartas = estado.cartasExibidas,
        totalCartas = estado.cartas.size,
        busca = estado.busca,
        onBuscaChange = viewModel::definirBusca,
        filtro = estado.filtro,
        onFiltroChange = viewModel::definirFiltro,
        onSincronizar = viewModel::carregarCartas,
        onNovaCarta = onNovaCarta,
        onEditar = { carta -> onEditarCarta(carta.id) },
        onExcluir = viewModel::removerCarta,
        onSair = onSair,
        sincronizando = estado.carregando,
        carregando = estado.carregando,
        erro = estado.erro,
        onTentarDeNovo = viewModel::carregarCartas,
    )
}

@Composable
fun CadastroCartaRota(
    cartaId: String?,
    onVoltar: () -> Unit,
    viewModel: PainelAdminViewModel = viewModel(
        factory = PainelAdminViewModelFactory(FirestoreCartaRepository()),
    ),
) {
    val estado = viewModel.uiState.collectAsStateWithLifecycle().value
    val carta = estado.cartas.firstOrNull { it.id == cartaId }

    if (cartaId != null && estado.carregando && carta == null) {
        EstadoCarregando(texto = "Carregando carta…")
        return
    }

    TelaCadastroCarta(
        cartaInicial = carta,
        onVoltar = {
            viewModel.limparDicas()
            onVoltar()
        },
        onSalvar = { cartaAtualizada ->
            viewModel.salvarCarta(cartaAtualizada) {
                viewModel.limparDicas()
                onVoltar()
            }
        },
        onGerarDicas = viewModel::gerarDicas,
        gerandoDicas = estado.gerandoDicas,
        dicasGeradas = estado.dicasGeradas,
        erro = estado.erro,
        salvando = estado.salvando,
    )
}
