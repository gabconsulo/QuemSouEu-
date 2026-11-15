package com.grupo.quemsoueu.admin

import com.grupo.quemsoueu.model.Carta
import com.grupo.quemsoueu.model.Categoria
import com.grupo.quemsoueu.model.Dificuldade

/** Dados digitados no formulário do Painel Admin (ainda não validados). */
data class FormularioCarta(
    val personagem: String = "",
    val dicasTexto: String = "",          // dicas separadas por ";"
    val categoria: Categoria? = null,
    val dificuldade: Dificuldade = Dificuldade.MEDIA
)

/** Resultado da validação: ou uma carta pronta, ou a lista de erros para mostrar na tela. */
sealed interface ResultadoFormulario {
    data class Sucesso(val carta: Carta) : ResultadoFormulario
    data class Erro(val mensagens: List<String>) : ResultadoFormulario
}

object AdminValidacao {

    const val MIN_DICAS = 2
    const val MAX_DICAS = 5
    const val MIN_CARACTERES_DICA = 3
    const val MAX_CARACTERES_NOME = 40

    /**
     * Valida o formulário e monta a Carta.
     * @param idEditando id da carta que está sendo editada (null = carta nova)
     * @param ativa mantém o status da carta ao editar
     */
    fun montarCarta(
        form: FormularioCarta,
        cartasExistentes: List<Carta>,
        idEditando: String? = null,
        ativa: Boolean = true
    ): ResultadoFormulario {
        val erros = mutableListOf<String>()

        // Strings e null safety (Aula 2.1): trim, isEmpty, length
        val nome = form.personagem.trim()
        val dicas = form.dicasTexto
            .split(";")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        val categoria = form.categoria

        when {
            nome.isEmpty() -> erros.add("Informe o nome do personagem.")
            nome.length > MAX_CARACTERES_NOME ->
                erros.add("O nome pode ter no máximo $MAX_CARACTERES_NOME caracteres.")
        }

        // Ignora a própria carta quando estiver editando
        val nomeDuplicado = cartasExistentes
            .filter { it.id != idEditando }
            .any { it.personagem.equals(nome, ignoreCase = true) }
        if (nome.isNotEmpty() && nomeDuplicado) {
            erros.add("Já existe uma carta com o nome \"$nome\".")
        }

        when {
            dicas.size < MIN_DICAS ->
                erros.add("Informe pelo menos $MIN_DICAS dicas, separadas por ponto e vírgula (;).")
            dicas.size > MAX_DICAS ->
                erros.add("Use no máximo $MAX_DICAS dicas.")
        }

        if (dicas.any { it.length < MIN_CARACTERES_DICA }) {
            erros.add("Cada dica precisa ter pelo menos $MIN_CARACTERES_DICA caracteres.")
        }

        if (categoria == null) {
            erros.add("Escolha uma categoria.")
        }

        // Smart cast: depois deste return, "categoria" já é não nula
        if (categoria == null || erros.isNotEmpty()) {
            return ResultadoFormulario.Erro(erros)
        }

        val id = idEditando ?: PainelAdminLogica.gerarId(cartasExistentes)

        // O init da classe Carta também valida; capturamos caso algo escape
        return try {
            ResultadoFormulario.Sucesso(
                Carta(
                    id = id,
                    personagem = nome,
                    dicas = dicas,
                    categoria = categoria,
                    dificuldade = form.dificuldade,
                    ativa = ativa
                )
            )
        } catch (e: IllegalArgumentException) {
            ResultadoFormulario.Erro(listOf(e.message ?: "Dados inválidos."))
        }
    }
}
