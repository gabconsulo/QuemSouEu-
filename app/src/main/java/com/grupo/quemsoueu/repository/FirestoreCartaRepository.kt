package com.grupo.quemsoueu.repository

import com.grupo.quemsoueu.model.Carta
import com.grupo.quemsoueu.model.Categoria
import com.grupo.quemsoueu.model.Dificuldade
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class FirestoreCartaRepository(
    private val obterDb: () -> FirebaseFirestore = { FirebaseFirestore.getInstance() },
) : CartaRepository {
    private val colecao get() = obterDb().collection(COLECAO_CARTAS)

    override suspend fun listarCartas(): Result<List<Carta>> =
        try {
            val cartas = colecao.get().await().documents.mapNotNull { it.paraCarta() }
            Result.success(cartas)
        } catch (erro: CancellationException) {
            throw erro
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    override suspend fun salvarCarta(carta: Carta): Result<Unit> =
        executar { colecao.document(carta.id).set(carta.paraMapa()).await() }

    override suspend fun removerCarta(id: String): Result<Unit> =
        executar { colecao.document(id).delete().await() }

    override suspend fun atualizarAtiva(id: String, ativa: Boolean): Result<Unit> =
        executar { colecao.document(id).update("ativa", ativa).await() }

    private suspend fun executar(operacao: suspend () -> Unit): Result<Unit> =
        try {
            withTimeoutOrNull(TEMPO_LIMITE_GRAVACAO_MS) { operacao() }
            Result.success(Unit)
        } catch (erro: CancellationException) {
            throw erro
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    private fun DocumentSnapshot.paraCarta(): Carta? {
        val personagem = getString("personagem") ?: return null
        val categoria = Categoria.deTexto(getString("categoria")) ?: return null
        val dicas = (get("dicas") as? List<*>)?.filterIsInstance<String>().orEmpty()
        val dificuldade = Dificuldade.entries.firstOrNull {
            it.name == getString("dificuldade")
        } ?: Dificuldade.MEDIA
        val ativa = getBoolean("ativa") ?: true

        return runCatching {
            Carta(id, personagem, dicas, categoria, dificuldade, ativa)
        }.getOrNull()
    }

    private fun Carta.paraMapa(): Map<String, Any> = mapOf(
        "personagem" to personagem,
        "dicas" to dicas,
        "categoria" to categoria.name,
        "dificuldade" to dificuldade.name,
        "ativa" to ativa,
    )

    companion object {
        const val COLECAO_CARTAS = "cartas"
        private const val TEMPO_LIMITE_GRAVACAO_MS = 5_000L
    }
}
