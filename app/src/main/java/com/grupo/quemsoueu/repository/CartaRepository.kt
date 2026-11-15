package com.grupo.quemsoueu.repository

import com.grupo.quemsoueu.model.Carta

interface CartaRepository {
    suspend fun listarCartas(): Result<List<Carta>>
    suspend fun salvarCarta(carta: Carta): Result<Unit>
    suspend fun removerCarta(id: String): Result<Unit>
    suspend fun atualizarAtiva(id: String, ativa: Boolean): Result<Unit>
}
