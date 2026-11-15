package com.exemplo.quemsoueu.model

/** Categorias usadas para organizar as cartas no Painel Admin e no jogo. */
enum class Categoria(val rotulo: String) {
    PESSOA_REAL("Pessoa real"),
    PERSONAGEM_FICTICIO("Personagem fictício"),
    ANIMAL("Animal"),
    OBJETO("Objeto"),
    LUGAR("Lugar");

    companion object {
        /** Converte o nome salvo no banco (ex.: "ANIMAL") na categoria. Retorna null se não existir. */
        fun deTexto(texto: String?): Categoria? = entries.firstOrNull { it.name == texto }
    }
}

enum class Dificuldade { FACIL, MEDIA, DIFICIL }

/**
 * Uma carta do jogo "Quem sou eu?".
 * O jogador vê as dicas uma por vez e tenta adivinhar o personagem.
 */
data class Carta(
    val id: String,
    val personagem: String,
    val dicas: List<String>,
    val categoria: Categoria,
    val dificuldade: Dificuldade = Dificuldade.MEDIA,
    val ativa: Boolean = true
) {
    // Validação no init, no mesmo estilo da classe Contato (Aula 4)
    init {
        require(id.isNotBlank()) { "O id da carta não pode ser vazio." }
        require(personagem.isNotBlank()) { "O nome do personagem não pode ser vazio." }
        require(dicas.isNotEmpty()) { "A carta precisa ter pelo menos uma dica." }
    }

    val totalDicas: Int
        get() = dicas.size
}
