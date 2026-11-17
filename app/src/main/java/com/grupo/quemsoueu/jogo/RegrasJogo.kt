package com.grupo.quemsoueu.jogo

import com.grupo.quemsoueu.ui.modelos.AjustesUi
import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.Dificuldade

const val VIDAS_MAXIMAS = 3
const val PONTOS_INICIAIS = 100
const val CUSTO_DICA = 15
const val CUSTO_PERGUNTA = 5
const val LIMITE_PERGUNTAS_IA = 10
const val TEMPO_LIMITE_SEGUNDOS = 120
const val SEGUNDOS_POR_PONTO_BONUS = 3

fun multiplicador(dificuldade: Dificuldade): Double =
    when (dificuldade) {
        Dificuldade.FACIL -> 1.0
        Dificuldade.MEDIO -> 1.5
        Dificuldade.DIFICIL -> 2.0
    }

fun palpiteCerto(palpite: String, carta: CartaUi): Boolean =
    palpite.trim().lowercase() == carta.nome.trim().lowercase()

fun pontosAposDica(pontos: Int): Int =
    if (pontos - CUSTO_DICA < 0) 0 else pontos - CUSTO_DICA

fun bonusDeTempo(segundosRestantes: Int, cronometro: Boolean): Int =
    if (cronometro) segundosRestantes / SEGUNDOS_POR_PONTO_BONUS else 0

fun pontosFinais(
    pontos: Int,
    dificuldade: Dificuldade,
    segundosRestantes: Int,
    cronometro: Boolean
): Int {
    val total = pontos + bonusDeTempo(segundosRestantes, cronometro)
    return (total * multiplicador(dificuldade)).toInt()
}

fun filtrarCartas(cartas: List<CartaUi>, ajustes: AjustesUi): List<CartaUi> =
    cartas
        .filter { it.ativa }
        .filter { it.categoria in ajustes.categorias }
        .filter { it.dificuldade == ajustes.dificuldade }

fun sortearCarta(cartas: List<CartaUi>, ajustes: AjustesUi, ultimaCartaId: String? = null): CartaUi? {
    val candidatas = filtrarCartas(cartas, ajustes)
        .distinctBy { it.nome.trim().lowercase() }
    val semRepetir = candidatas.filter { it.id != ultimaCartaId }
    return semRepetir.ifEmpty { candidatas }.randomOrNull()
}
