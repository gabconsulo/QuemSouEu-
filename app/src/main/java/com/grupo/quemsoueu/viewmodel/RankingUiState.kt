package com.grupo.quemsoueu.viewmodel

import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.OrdemRanking
import com.grupo.quemsoueu.ui.modelos.PartidaUi

data class RankingUiState(
    val partidas: List<PartidaUi> = emptyList(),
    val filtro: Categoria? = null,
    val ordem: OrdemRanking = OrdemRanking.PONTOS,
    val carregando: Boolean = true,
    val erro: String? = null,
) {
    val partidasExibidas: List<PartidaUi>
        get() = partidas
            .filter { filtro == null || it.categoria == filtro }
            .let { lista ->
                when (ordem) {
                    OrdemRanking.PONTOS -> lista.sortedByDescending { it.pontuacao }
                    OrdemRanking.TEMPO -> lista.sortedBy { it.tempoSegundos }
                    OrdemRanking.RECENTES -> lista.sortedByDescending { it.criadaEm }
                }
            }
}
