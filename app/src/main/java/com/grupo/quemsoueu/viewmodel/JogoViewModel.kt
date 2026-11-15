package com.grupo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.quemsoueu.jogo.PONTOS_INICIAIS
import com.grupo.quemsoueu.jogo.TEMPO_LIMITE_SEGUNDOS
import com.grupo.quemsoueu.jogo.VIDAS_MAXIMAS
import com.grupo.quemsoueu.jogo.converterCarta
import com.grupo.quemsoueu.jogo.palpiteCerto
import com.grupo.quemsoueu.jogo.pontosAposDica
import com.grupo.quemsoueu.jogo.pontosFinais
import com.grupo.quemsoueu.jogo.sortearCarta
import com.grupo.quemsoueu.repository.CartaRepository
import com.grupo.quemsoueu.ui.modelos.AjustesUi
import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.PartidaUi
import com.grupo.quemsoueu.ui.modelos.ResultadoPartidaUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JogoViewModel(
    private val repository: CartaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JogoUiState())
    val uiState: StateFlow<JogoUiState> = _uiState.asStateFlow()

    private var cronometro: Job? = null

    init {
        onCarregarCartas()
    }

    fun onCarregarCartas() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            repository.listarCartas()
                .onSuccess { cartas ->
                    val cartasDoJogo = cartas
                        .filter { carta -> carta.ativa }
                        .map { carta -> converterCarta(carta) }
                    _uiState.update { it.copy(cartasDisponiveis = cartasDoJogo, carregando = false) }
                }
                .onFailure { falha ->
                    _uiState.update {
                        it.copy(
                            carregando = false,
                            erro = "Não foi possível carregar as cartas. (${falha.localizedMessage ?: "erro desconhecido"})"
                        )
                    }
                }
        }
    }

    fun onAjustesChange(novosAjustes: AjustesUi) {
        _uiState.update { it.copy(ajustes = novosAjustes, erro = null) }
    }

    fun onIniciarPartida() {
        val state = _uiState.value
        val carta = sortearCarta(state.cartasDisponiveis, state.ajustes)

        if (carta == null) {
            _uiState.update {
                it.copy(erro = "Nenhuma carta ativa com esses ajustes. Mude os ajustes ou peça ao Admin para cadastrar cartas.")
            }
            return
        }

        _uiState.update {
            it.copy(
                erro = null,
                ajustesDaPartida = it.ajustes,
                carta = carta,
                partidaNumero = it.partidaNumero + 1,
                vidas = VIDAS_MAXIMAS,
                dicasReveladas = 1,
                indiceDicaNova = null,
                pontos = PONTOS_INICIAIS,
                aviso = null,
                segundosRestantes = TEMPO_LIMITE_SEGUNDOS,
                resultado = null
            )
        }

        cronometro?.cancel()
        if (state.ajustes.cronometro) {
            iniciarCronometro()
        }
    }

    fun onVirarDica() {
        val state = _uiState.value
        val carta = state.carta ?: return
        if (!state.emAndamento) return
        if (state.dicasReveladas >= carta.dicas.size) return

        _uiState.update {
            it.copy(
                dicasReveladas = it.dicasReveladas + 1,
                indiceDicaNova = it.dicasReveladas,
                pontos = pontosAposDica(it.pontos),
                aviso = null
            )
        }
    }

    fun onChutar(palpite: String) {
        val state = _uiState.value
        val carta = state.carta ?: return
        if (!state.emAndamento) return

        if (palpiteCerto(palpite, carta)) {
            val total = pontosFinais(
                state.pontos,
                carta.dificuldade,
                state.segundosRestantes,
                state.ajustesDaPartida.cronometro
            )
            encerrarPartida(true, carta, total)
            return
        }

        val vidasRestantes = state.vidas - 1
        if (vidasRestantes <= 0) {
            _uiState.update { it.copy(vidas = 0) }
            encerrarPartida(false, carta, 0)
            return
        }

        val temDicaNova = state.dicasReveladas < carta.dicas.size
        val aviso = if (temDicaNova) {
            "“$palpite” não é. Você perdeu 1 vida e ganhou uma dica!"
        } else {
            "“$palpite” não é. Você perdeu 1 vida."
        }

        _uiState.update {
            it.copy(
                vidas = vidasRestantes,
                aviso = aviso,
                dicasReveladas = if (temDicaNova) it.dicasReveladas + 1 else it.dicasReveladas,
                indiceDicaNova = if (temDicaNova) it.dicasReveladas else null
            )
        }
    }

    fun onLimparPartida() {
        cronometro?.cancel()
        _uiState.update { it.copy(carta = null, resultado = null, aviso = null) }
    }

    fun montarPartida(nomeJogador: String): PartidaUi? {
        val state = _uiState.value
        val carta = state.carta ?: return null
        val resultado = state.resultado ?: return null

        return PartidaUi(
            id = 0L,
            nomeJogador = nomeJogador,
            personagem = carta.nome,
            categoria = carta.categoria,
            pontuacao = resultado.pontos,
            dicasUsadas = state.dicasReveladas,
            perguntasIa = 0,
            tempoSegundos = state.tempoGasto
        )
    }

    private fun encerrarPartida(venceu: Boolean, carta: CartaUi, pontos: Int) {
        cronometro?.cancel()
        _uiState.update {
            it.copy(
                aviso = null,
                resultado = ResultadoPartidaUi(venceu, carta.nome, pontos)
            )
        }
    }

    private fun iniciarCronometro() {
        cronometro = viewModelScope.launch {
            while (_uiState.value.segundosRestantes > 0) {
                delay(1000)
                _uiState.update { it.copy(segundosRestantes = it.segundosRestantes - 1) }
            }
            val carta = _uiState.value.carta
            if (carta != null && _uiState.value.emAndamento) {
                encerrarPartida(false, carta, 0)
            }
        }
    }
}
