package com.grupo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.quemsoueu.admin.AdminValidacao
import com.grupo.quemsoueu.admin.FormularioCarta
import com.grupo.quemsoueu.admin.PainelAdminLogica
import com.grupo.quemsoueu.admin.ResultadoFormulario
import com.grupo.quemsoueu.data.CartasIniciais
import com.grupo.quemsoueu.model.Carta
import com.grupo.quemsoueu.model.Categoria as CategoriaDados
import com.grupo.quemsoueu.model.Dificuldade as DificuldadeDados
import com.grupo.quemsoueu.repository.CartaRepository
import com.grupo.quemsoueu.repository.GeminiPerguntaRepository
import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.Dificuldade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PainelAdminViewModel(
    private val repository: CartaRepository,
    private val gemini: GeminiPerguntaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PainelAdminUiState())
    val uiState: StateFlow<PainelAdminUiState> = _uiState.asStateFlow()

    init {
        carregarCartas()
    }

    fun carregarCartas() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            repository.listarCartas()
                .onSuccess { cartas ->
                    _uiState.update {
                        it.copy(cartas = cartas.map { carta -> carta.paraUi() }, carregando = false)
                    }
                }
                .onFailure { erro -> publicarErro(erro) }
        }
    }

    fun definirBusca(busca: String) {
        _uiState.update { it.copy(busca = busca) }
    }

    fun definirFiltro(filtro: Categoria?) {
        _uiState.update { it.copy(filtro = filtro) }
    }

    fun removerCarta(carta: CartaUi) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            repository.removerCarta(carta.id)
                .onSuccess {
                    _uiState.update {
                        it.copy(cartas = it.cartas.filterNot { atual -> atual.id == carta.id }, carregando = false)
                    }
                }
                .onFailure { erro -> publicarErro(erro) }
        }
    }

    fun salvarCarta(carta: CartaUi, aoConcluir: () -> Unit) {
        val estado = _uiState.value
        val formulario = FormularioCarta(
            personagem = carta.nome,
            dicasTexto = carta.dicas.joinToString(";"),
            categoria = carta.categoria.paraDados(),
            dificuldade = carta.dificuldade.paraDados(),
        )
        val resultado = AdminValidacao.montarCarta(
            form = formulario,
            cartasExistentes = estado.cartas.map { it.paraDados(it.id) },
            idEditando = carta.id.takeIf { it.isNotBlank() },
            ativa = carta.ativa,
        )
        val cartaDados = when (resultado) {
            is ResultadoFormulario.Erro -> {
                _uiState.update { it.copy(erro = resultado.mensagens.joinToString("\n")) }
                return
            }
            is ResultadoFormulario.Sucesso -> resultado.carta
        }

        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true, erro = null) }
            repository.salvarCarta(cartaDados)
                .onSuccess {
                    _uiState.update { atual ->
                        atual.copy(
                            cartas = PainelAdminLogica
                                .adicionarOuAtualizar(
                                    atual.cartas.map { it.paraDados(it.id) },
                                    cartaDados,
                                )
                                .map { it.paraUi() },
                            salvando = false,
                            dicasGeradas = null,
                        )
                    }
                    aoConcluir()
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(salvando = false) }
                    publicarErro(erro)
                }
        }
    }

    fun gerarDicas(nome: String, categoria: Categoria, dificuldade: Dificuldade) {
        viewModelScope.launch {
            _uiState.update { it.copy(gerandoDicas = true, erro = null, dicasGeradas = null) }
            gemini.gerarDicas(nome, categoria.nome, dificuldade.nome)
                .onSuccess { dicas ->
                    _uiState.update { it.copy(gerandoDicas = false, dicasGeradas = dicas) }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(gerandoDicas = false) }
                    publicarErro(erro)
                }
        }
    }

    fun limparDicas() {
        _uiState.update { it.copy(dicasGeradas = null, erro = null) }
    }

    fun importarCartasIniciais() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            for (carta in CartasIniciais.todas) {
                val falha = repository.salvarCarta(carta).exceptionOrNull()
                if (falha != null) {
                    publicarErro(falha)
                    return@launch
                }
            }
            carregarCartas()
        }
    }

    private fun publicarErro(erro: Throwable) {
        _uiState.update {
            it.copy(
                carregando = false,
                erro = erro.localizedMessage ?: "Não foi possível concluir a operação.",
            )
        }
    }

    private fun Carta.paraUi(): CartaUi = CartaUi(
        id = id,
        nome = personagem,
        categoria = when (categoria) {
            CategoriaDados.PESSOA_REAL -> Categoria.FAMOSOS
            CategoriaDados.PERSONAGEM_FICTICIO -> Categoria.FILMES
            CategoriaDados.ANIMAL -> Categoria.ANIMAIS
            CategoriaDados.OBJETO, CategoriaDados.LUGAR -> Categoria.OBJETOS
        },
        dificuldade = when (dificuldade) {
            DificuldadeDados.FACIL -> Dificuldade.FACIL
            DificuldadeDados.MEDIA -> Dificuldade.MEDIO
            DificuldadeDados.DIFICIL -> Dificuldade.DIFICIL
        },
        dicas = dicas,
        ativa = ativa,
    )

    private fun CartaUi.paraDados(id: String): Carta = Carta(
        id = id,
        personagem = nome.trim(),
        dicas = dicas.map(String::trim),
        categoria = when (categoria) {
            Categoria.FAMOSOS -> CategoriaDados.PESSOA_REAL
            Categoria.FILMES -> CategoriaDados.PERSONAGEM_FICTICIO
            Categoria.ANIMAIS -> CategoriaDados.ANIMAL
            Categoria.OBJETOS -> CategoriaDados.OBJETO
        },
        dificuldade = when (dificuldade) {
            Dificuldade.FACIL -> DificuldadeDados.FACIL
            Dificuldade.MEDIO -> DificuldadeDados.MEDIA
            Dificuldade.DIFICIL -> DificuldadeDados.DIFICIL
        },
        ativa = ativa,
    )

    private fun Categoria.paraDados(): CategoriaDados =
        when (this) {
            Categoria.FAMOSOS -> CategoriaDados.PESSOA_REAL
            Categoria.FILMES -> CategoriaDados.PERSONAGEM_FICTICIO
            Categoria.ANIMAIS -> CategoriaDados.ANIMAL
            Categoria.OBJETOS -> CategoriaDados.OBJETO
        }

    private fun Dificuldade.paraDados(): DificuldadeDados =
        when (this) {
            Dificuldade.FACIL -> DificuldadeDados.FACIL
            Dificuldade.MEDIO -> DificuldadeDados.MEDIA
            Dificuldade.DIFICIL -> DificuldadeDados.DIFICIL
        }
}
