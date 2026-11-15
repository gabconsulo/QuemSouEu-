package com.grupo.quemsoueu.viewmodel

import com.grupo.quemsoueu.jogo.CUSTO_DICA
import com.grupo.quemsoueu.jogo.CUSTO_PERGUNTA
import com.grupo.quemsoueu.jogo.LIMITE_PERGUNTAS_IA
import com.grupo.quemsoueu.jogo.PONTOS_INICIAIS
import com.grupo.quemsoueu.jogo.TEMPO_LIMITE_SEGUNDOS
import com.grupo.quemsoueu.jogo.VIDAS_MAXIMAS
import com.grupo.quemsoueu.ui.modelos.AjustesUi
import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.EstadoJogoUi
import com.grupo.quemsoueu.ui.modelos.PerguntaIaUi
import com.grupo.quemsoueu.ui.modelos.ResultadoPartidaUi
import com.grupo.quemsoueu.ui.modelos.formatarTempo

data class JogoUiState(
    val carregando: Boolean = false,
    val erro: String? = null,
    val cartasDisponiveis: List<CartaUi> = emptyList(),
    val ajustes: AjustesUi = AjustesUi(),
    val ajustesDaPartida: AjustesUi = AjustesUi(),
    val carta: CartaUi? = null,
    val partidaNumero: Int = 0,
    val vidas: Int = VIDAS_MAXIMAS,
    val dicasReveladas: Int = 1,
    val indiceDicaNova: Int? = null,
    val pontos: Int = PONTOS_INICIAIS,
    val aviso: String? = null,
    val segundosRestantes: Int = TEMPO_LIMITE_SEGUNDOS,
    val resultado: ResultadoPartidaUi? = null,
    val perguntasIa: List<PerguntaIaUi> = emptyList(),
    val iaPensando: Boolean = false,
    val erroIa: String? = null,
) {
    val emAndamento: Boolean
        get() = carta != null && resultado == null

    val precisaIniciar: Boolean
        get() = carta == null && !carregando && erro == null

    val tempoGasto: Int
        get() = if (ajustesDaPartida.cronometro) TEMPO_LIMITE_SEGUNDOS - segundosRestantes else 0

    val estadoTela: EstadoJogoUi?
        get() {
            val cartaAtual = carta ?: return null
            return EstadoJogoUi(
                categoria = cartaAtual.categoria,
                dificuldade = cartaAtual.dificuldade,
                rodada = partidaNumero,
                vidas = vidas,
                vidasMaximas = VIDAS_MAXIMAS,
                mostrarCronometro = ajustesDaPartida.cronometro,
                tempoTexto = formatarTempo(segundosRestantes),
                progressoTempo = segundosRestantes.toFloat() / TEMPO_LIMITE_SEGUNDOS,
                pontos = pontos,
                dicas = cartaAtual.dicas,
                dicasReveladas = dicasReveladas,
                indiceDicaNova = indiceDicaNova,
                custoDica = CUSTO_DICA,
                custoPergunta = CUSTO_PERGUNTA,
                limitePerguntas = LIMITE_PERGUNTAS_IA,
                aviso = aviso,
                iaLigada = ajustesDaPartida.perguntasIa,
                perguntasIa = perguntasIa,
                iaPensando = iaPensando,
                erroIa = erroIa,
            )
        }
}
