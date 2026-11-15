package com.exemplo.quemsoueu.repository

import com.exemplo.quemsoueu.model.Carta

/**
 * Contrato de acesso às cartas. O ViewModel depende só desta interface,
 * então dá para trocar o Firestore por outra fonte (ex.: dados fake em testes).
 */
interface CartaRepository {
    suspend fun listarCartas(): Result<List<Carta>>
    suspend fun salvarCarta(carta: Carta): Result<Unit>
    suspend fun removerCarta(id: String): Result<Unit>
    suspend fun atualizarAtiva(id: String, ativa: Boolean): Result<Unit>
}
