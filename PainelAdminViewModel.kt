package com.exemplo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplo.quemsoueu.admin.AdminValidacao
import com.exemplo.quemsoueu.admin.FormularioCarta
import com.exemplo.quemsoueu.admin.PainelAdminLogica
import com.exemplo.quemsoueu.admin.ResultadoFormulario
import com.exemplo.quemsoueu.data.CartasIniciais
import com.exemplo.quemsoueu.model.Carta
import com.exemplo.quemsoueu.model.Categoria
import com.exemplo.quemsoueu.repository.CartaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado único que a tela do Painel Admin observa. */
data class PainelAdminUiState(
    val cartas: List<Carta> = emptyList(),
    val carregando: Boolean = false,
    val busca: String = "",
    val categoriaFiltro: Categoria? = null,
    val erroGeral: String? = null,
    val errosFormulario: List<String> = emptyList(),
    val mensagemSucesso: String? = null
) {
    /** Lista que a tela deve mostrar (filtrada e ordenada). */
    val cartasExibidas: List<Carta>
        get() = PainelAdminLogica.filtrar(cartas, busca, categoriaFiltro)
}

class PainelAdminViewModel(
    private val repository: CartaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PainelAdminUiState())
    val uiState: StateFlow<PainelAdminUiState> = _uiState.asStateFlow()

    init {
        carregarCartas()
    }

    fun carregarCartas() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erroGeral = null) }
            repository.listarCartas()
                .onSuccess { cartas ->
                    _uiState.update { it.copy(cartas = cartas, carregando = false) }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false, erroGeral = mensagemDeErro(erro)) }
                }
        }
    }

    /** Valida o formulário e, se estiver ok, salva no Firestore. */
    fun salvarCarta(form: FormularioCarta, idEditando: String? = null) {
        val cartasAtuais = _uiState.value.cartas
        val ativaAtual = cartasAtuais.firstOrNull { it.id == idEditando }?.ativa ?: true

        when (val resultado = AdminValidacao.montarCarta(form, cartasAtuais, idEditando, ativaAtual)) {
            is ResultadoFormulario.Erro -> {
                _uiState.update {
                    it.copy(errosFormulario = resultado.mensagens, mensagemSucesso = null)
                }
            }
            is ResultadoFormulario.Sucesso -> {
                val carta = resultado.carta
                viewModelScope.launch {
                    _uiState.update { it.copy(carregando = true, errosFormulario = emptyList()) }
                    repository.salvarCarta(carta)
                        .onSuccess {
                            _uiState.update { estado ->
                                estado.copy(
                                    cartas = PainelAdminLogica.adicionarOuAtualizar(estado.cartas, carta),
                                    carregando = false,
                                    mensagemSucesso = "Carta \"${carta.personagem}\" salva com sucesso!"
                                )
                            }
                        }
                        .onFailure { erro ->
                            _uiState.update { it.copy(carregando = false, erroGeral = mensagemDeErro(erro)) }
                        }
                }
            }
        }
    }

    fun removerCarta(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erroGeral = null) }
            repository.removerCarta(id)
                .onSuccess {
                    _uiState.update { estado ->
                        estado.copy(
                            cartas = PainelAdminLogica.remover(estado.cartas, id),
                            carregando = false
                        )
                    }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false, erroGeral = mensagemDeErro(erro)) }
                }
        }
    }

    fun alternarAtiva(id: String) {
        val carta = _uiState.value.cartas.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            repository.atualizarAtiva(id, !carta.ativa)
                .onSuccess {
                    _uiState.update { estado ->
                        estado.copy(cartas = PainelAdminLogica.alternarAtiva(estado.cartas, id))
                    }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(erroGeral = mensagemDeErro(erro)) }
                }
        }
    }

    /** Envia a lista inicial de cartas para o Firestore (uso único, na primeira vez). */
    fun importarCartasIniciais() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erroGeral = null) }
            for (carta in CartasIniciais.todas) {
                val falha = repository.salvarCarta(carta).exceptionOrNull()
                if (falha != null) {
                    _uiState.update { it.copy(carregando = false, erroGeral = mensagemDeErro(falha)) }
                    return@launch
                }
            }
            _uiState.update { it.copy(mensagemSucesso = "Cartas iniciais importadas!") }
            carregarCartas()
        }
    }

    fun definirBusca(texto: String) {
        _uiState.update { it.copy(busca = texto) }
    }

    fun definirFiltroCategoria(categoria: Categoria?) {
        _uiState.update { it.copy(categoriaFiltro = categoria) }
    }

    fun limparMensagens() {
        _uiState.update {
            it.copy(erroGeral = null, mensagemSucesso = null, errosFormulario = emptyList())
        }
    }

    private fun mensagemDeErro(erro: Throwable): String =
        "Não foi possível concluir a operação. Verifique sua conexão e tente novamente. " +
            "(${erro.localizedMessage ?: "erro desconhecido"})"
}
