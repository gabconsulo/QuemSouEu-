package com.grupo.quemsoueu.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.PartidaUi

@Entity(tableName = "partidas")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nomeJogador: String,
    val personagem: String,
    val categoria: String,
    val pontuacao: Int,
    val dicasUsadas: Int,
    val perguntasIa: Int,
    val tempoSegundos: Int,
    val criadaEm: Long = System.currentTimeMillis(),
) {
    fun paraUi(): PartidaUi = PartidaUi(
        id = id,
        nomeJogador = nomeJogador,
        personagem = personagem,
        categoria = Categoria.entries.firstOrNull { it.name == categoria }
            ?: error("Categoria inválida salva no ranking: $categoria"),
        pontuacao = pontuacao,
        dicasUsadas = dicasUsadas,
        perguntasIa = perguntasIa,
        tempoSegundos = tempoSegundos,
        criadaEm = criadaEm,
    )

    companion object {
        fun deUi(partida: PartidaUi): PartidaEntity = PartidaEntity(
            id = partida.id,
            nomeJogador = partida.nomeJogador,
            personagem = partida.personagem,
            categoria = partida.categoria.name,
            pontuacao = partida.pontuacao,
            dicasUsadas = partida.dicasUsadas,
            perguntasIa = partida.perguntasIa,
            tempoSegundos = partida.tempoSegundos,
            criadaEm = partida.criadaEm.takeIf { it > 0L } ?: System.currentTimeMillis(),
        )
    }
}
