package com.grupo.quemsoueu.viewmodel

import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.Categoria

data class PainelAdminUiState(
    val cartas: List<CartaUi> = emptyList(),
    val busca: String = "",
    val filtro: Categoria? = null,
    val carregando: Boolean = true,
    val salvando: Boolean = false,
    val gerandoDicas: Boolean = false,
    val dicasGeradas: List<String>? = null,
    val erro: String? = null,
) {
    val cartasExibidas: List<CartaUi>
        get() = cartas
            .filter { filtro == null || it.categoria == filtro }
            .filter { busca.isBlank() || it.nome.contains(busca.trim(), ignoreCase = true) }
            .sortedBy { it.nome }
}
