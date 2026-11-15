package com.exemplo.quemsoueu.repository

import com.exemplo.quemsoueu.model.Carta
import com.exemplo.quemsoueu.model.Categoria
import com.exemplo.quemsoueu.model.Dificuldade
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Implementação do CartaRepository usando o Cloud Firestore.
 * Todas as operações têm try-catch e devolvem Result: o app nunca cai por falha de rede.
 */
class FirestoreCartaRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : CartaRepository {

    private val colecao get() = db.collection(COLECAO_CARTAS)

    override suspend fun listarCartas(): Result<List<Carta>> = try {
        val snapshot = colecao.get().await()
        // mapNotNull descarta documentos malformados sem derrubar a lista inteira
        val cartas = snapshot.documents.mapNotNull { it.paraCarta() }
        Result.success(cartas)
    } catch (e: CancellationException) {
        throw e // não engolir cancelamento de coroutine
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun salvarCarta(carta: Carta): Result<Unit> =
        executar {
            colecao.document(carta.id).set(carta.paraMapa()).await()
        }

    override suspend fun removerCarta(id: String): Result<Unit> =
        executar {
            colecao.document(id).delete().await()
        }

    override suspend fun atualizarAtiva(id: String, ativa: Boolean): Result<Unit> =
        executar {
            colecao.document(id).update("ativa", ativa).await()
        }

    /** Centraliza o try-catch das operações de escrita. */
    private suspend fun executar(bloco: suspend () -> Unit): Result<Unit> =
        try {
            bloco()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    /** Documento do Firestore -> Carta. Retorna null se faltar campo obrigatório. */
    private fun DocumentSnapshot.paraCarta(): Carta? {
        val personagem = getString("personagem") ?: return null
        val categoria = Categoria.deTexto(getString("categoria")) ?: return null
        val dicas = (get("dicas") as? List<*>)?.filterIsInstance<String>().orEmpty()
        val dificuldade = Dificuldade.entries
            .firstOrNull { it.name == getString("dificuldade") }
            ?: Dificuldade.MEDIA
        val ativa = getBoolean("ativa") ?: true

        // runCatching: se o init da Carta reclamar (dados inválidos), ignoramos o documento
        return runCatching {
            Carta(
                id = id,
                personagem = personagem,
                dicas = dicas,
                categoria = categoria,
                dificuldade = dificuldade,
                ativa = ativa
            )
        }.getOrNull()
    }

    /** Carta -> Map que o Firestore consegue salvar. O id vira o nome do documento. */
    private fun Carta.paraMapa(): Map<String, Any> = mapOf(
        "personagem" to personagem,
        "dicas" to dicas,
        "categoria" to categoria.name,
        "dificuldade" to dificuldade.name,
        "ativa" to ativa
    )

    companion object {
        const val COLECAO_CARTAS = "cartas"
    }
}
