package com.grupo.quemsoueu.repository

import com.grupo.quemsoueu.data.local.PartidaDao
import com.grupo.quemsoueu.data.local.PartidaEntity
import com.grupo.quemsoueu.ui.modelos.PartidaUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface PartidaRepository {
    fun observarPartidas(): Flow<Result<List<PartidaUi>>>
    suspend fun inserir(partida: PartidaUi): Result<Unit>
    suspend fun atualizar(partida: PartidaUi): Result<Unit>
    suspend fun excluir(id: Long): Result<Unit>
}

class RoomPartidaRepository(
    private val dao: PartidaDao,
) : PartidaRepository {
    override fun observarPartidas(): Flow<Result<List<PartidaUi>>> =
        dao.observarTodas()
            .map { partidas -> Result.success(partidas.map(PartidaEntity::paraUi)) }
            .catch { erro ->
                if (erro is CancellationException) throw erro
                emit(Result.failure(erro))
            }

    override suspend fun inserir(partida: PartidaUi): Result<Unit> = executar {
        dao.inserir(PartidaEntity.deUi(partida.copy(id = 0L)))
        Unit
    }

    override suspend fun atualizar(partida: PartidaUi): Result<Unit> = executar {
        check(dao.atualizar(PartidaEntity.deUi(partida)) > 0) {
            "A partida não existe mais no ranking."
        }
    }

    override suspend fun excluir(id: Long): Result<Unit> = executar {
        check(dao.excluir(id) > 0) { "A partida não existe mais no ranking." }
    }

    private suspend fun executar(operacao: suspend () -> Unit): Result<Unit> =
        try {
            operacao()
            Result.success(Unit)
        } catch (erro: CancellationException) {
            throw erro
        } catch (erro: Exception) {
            Result.failure(erro)
        }
}
