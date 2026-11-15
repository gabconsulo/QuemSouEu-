package com.grupo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.quemsoueu.repository.PartidaRepository
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.OrdemRanking
import com.grupo.quemsoueu.ui.modelos.PartidaUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankingViewModel(
    private val repository: PartidaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()
    private var observacao: Job? = null

    init {
        carregar()
    }

    fun carregar() {
        observacao?.cancel()
        observacao = viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            repository.observarPartidas().collect { resultado ->
                resultado
                    .onSuccess { partidas ->
                        _uiState.update {
                            it.copy(partidas = partidas, carregando = false, erro = null)
                        }
                    }
                    .onFailure { erro -> publicarErro(erro) }
            }
        }
    }

    fun definirFiltro(filtro: Categoria?) {
        _uiState.update { it.copy(filtro = filtro) }
    }

    fun definirOrdem(ordem: OrdemRanking) {
        _uiState.update { it.copy(ordem = ordem) }
    }

    fun salvar(partida: PartidaUi) {
        executar { repository.inserir(partida) }
    }

    fun editarNome(partida: PartidaUi, novoNome: String) {
        if (novoNome.isBlank()) {
            _uiState.update { it.copy(erro = "O nome não pode ficar vazio.") }
            return
        }
        executar { repository.atualizar(partida.copy(nomeJogador = novoNome.trim())) }
    }

    fun excluir(partida: PartidaUi) {
        executar { repository.excluir(partida.id) }
    }

    private fun executar(operacao: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _uiState.update { it.copy(erro = null) }
            operacao().onFailure { erro -> publicarErro(erro) }
        }
    }

    private fun publicarErro(erro: Throwable) {
        _uiState.update {
            it.copy(
                carregando = false,
                erro = "Não foi possível atualizar o ranking. (${erro.localizedMessage ?: "erro desconhecido"})",
            )
        }
    }
}
