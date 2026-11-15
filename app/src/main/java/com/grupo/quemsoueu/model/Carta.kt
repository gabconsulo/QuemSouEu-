package com.grupo.quemsoueu.model

enum class Categoria(val rotulo: String) {
    PESSOA_REAL("Pessoa real"),
    PERSONAGEM_FICTICIO("Personagem fictício"),
    ANIMAL("Animal"),
    OBJETO("Objeto"),
    LUGAR("Lugar");

    companion object {
        fun deTexto(texto: String?): Categoria? = entries.firstOrNull { it.name == texto }
    }
}

enum class Dificuldade {
    FACIL,
    MEDIA,
    DIFICIL,
}

data class Carta(
    val id: String,
    val personagem: String,
    val dicas: List<String>,
    val categoria: Categoria,
    val dificuldade: Dificuldade = Dificuldade.MEDIA,
    val ativa: Boolean = true,
) {
    init {
        require(id.isNotBlank()) { "O id da carta não pode ser vazio." }
        require(personagem.isNotBlank()) { "O nome do personagem não pode ser vazio." }
        require(dicas.isNotEmpty()) { "A carta precisa ter pelo menos uma dica." }
    }
}
