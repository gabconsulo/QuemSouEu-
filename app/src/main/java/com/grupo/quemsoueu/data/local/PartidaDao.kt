package com.grupo.quemsoueu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Query("SELECT * FROM partidas ORDER BY criadaEm DESC")
    fun observarTodas(): Flow<List<PartidaEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(partida: PartidaEntity): Long

    @Update
    suspend fun atualizar(partida: PartidaEntity): Int

    @Query("DELETE FROM partidas WHERE id = :id")
    suspend fun excluir(id: Long): Int
}
