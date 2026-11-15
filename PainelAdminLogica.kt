package com.exemplo.quemsoueu.admin

import com.exemplo.quemsoueu.model.Carta
import com.exemplo.quemsoueu.model.Categoria

/**
 * Lógica pura do Painel Admin: não usa Android nem banco, só listas.
 * Cada função devolve uma NOVA lista (estilo imutável), usando map / filter (Aula 3).
 */
object PainelAdminLogica {

    /** Gera o próximo id no formato carta_016, carta_017... */
    fun gerarId(cartas: List<Carta>): String {
        val maiorNumero = cartas
            .mapNotNull { it.id.removePrefix("carta_").toIntOrNull() }
            .maxOrNull() ?: 0
        return "carta_%03d".format(maiorNumero + 1)
    }

    /** Insere a carta se o id não existir; senão substitui a carta existente. */
    fun adicionarOuAtualizar(cartas: List<Carta>, carta: Carta): List<Carta> =
        if (cartas.any { it.id == carta.id }) {
            cartas.map { if (it.id == carta.id) carta else it }
        } else {
            cartas + carta
        }

    fun remover(cartas: List<Carta>, id: String): List<Carta> =
        cartas.filterNot { it.id == id }

    /** Liga/desliga a carta (cartas desativadas não entram no jogo). */
    fun alternarAtiva(cartas: List<Carta>, id: String): List<Carta> =
        cartas.map { if (it.id == id) it.copy(ativa = !it.ativa) else it }

    /** Filtra por texto (ignora maiúsculas) e por categoria, e ordena pelo nome. */
    fun filtrar(
        cartas: List<Carta>,
        busca: String,
        categoria: Categoria?
    ): List<Carta> {
        val termo = busca.trim()
        return cartas
            .filter { categoria == null || it.categoria == categoria }
            .filter { termo.isEmpty() || it.personagem.contains(termo, ignoreCase = true) }
            .sortedBy { it.personagem }
    }

    /** Quantidade de cartas por categoria, para o resumo do painel. */
    fun resumoPorCategoria(cartas: List<Carta>): Map<Categoria, Int> =
        cartas.groupingBy { it.categoria }.eachCount()

    fun cartasAtivas(cartas: List<Carta>): List<Carta> =
        cartas.filter { it.ativa }
}
