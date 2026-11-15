package com.grupo.quemsoueu.ui.modelos

import androidx.compose.ui.graphics.Color
import com.grupo.quemsoueu.ui.theme.Amarelo
import com.grupo.quemsoueu.ui.theme.Azul
import com.grupo.quemsoueu.ui.theme.Laranja
import com.grupo.quemsoueu.ui.theme.Rosa
import com.grupo.quemsoueu.ui.theme.Verde

// Modelos usados SÓ pelas telas (camada de UI).
// As data class "oficiais" (Personagem e Partida) são do Gustavo. Quem liga a lógica
// converte as classes dele para estas antes de passar para as telas.

enum class Categoria(val nome: String, val cor: Color) {
    FAMOSOS("Famosos", Rosa),
    ANIMAIS("Animais", Verde),
    FILMES("Filmes", Azul),
    OBJETOS("Objetos", Laranja),
}

enum class Dificuldade(val nome: String, val estrelas: Int, val multiplicador: String) {
    FACIL("Fácil", 1, "×1"),
    MEDIO("Médio", 2, "×1,5"),
    DIFICIL("Difícil", 3, "×2"),
}

enum class RespostaIa(val texto: String, val cor: Color) {
    SIM("Sim", Verde),
    NAO("Não", Rosa),
    TALVEZ("Talvez", Amarelo),
}

enum class OrdemRanking(val rotulo: String) {
    PONTOS("Pontos"),
    TEMPO("Tempo"),
    RECENTES("Mais recentes"),
}

data class PerguntaIaUi(
    val pergunta: String,
    val resposta: RespostaIa,
)

/** Uma carta do jogo, como aparece no Painel Admin e no Cadastro. */
data class CartaUi(
    val id: String,
    val nome: String,
    val categoria: Categoria,
    val dificuldade: Dificuldade,
    val dicas: List<String>,
    val ativa: Boolean = true,
)

/** Uma linha do Ranking. */
data class PartidaUi(
    val id: Long,
    val nomeJogador: String,
    val personagem: String,
    val categoria: Categoria,
    val pontuacao: Int,
    val dicasUsadas: Int,
    val perguntasIa: Int,
    val tempoSegundos: Int,
)

/** Tudo o que a tela de jogo mostra. O GameViewModel monta este objeto na Fase 2. */
data class EstadoJogoUi(
    val categoria: Categoria,
    val dificuldade: Dificuldade,
    val rodada: Int,
    val vidas: Int,
    val vidasMaximas: Int = 3,
    val mostrarCronometro: Boolean = true,
    val tempoTexto: String = "00:00",
    /** De 0f (acabou o tempo) a 1f (tempo cheio). */
    val progressoTempo: Float = 1f,
    val pontos: Int,
    val dicas: List<String>,
    val dicasReveladas: Int,
    /** Posição (0 a 4) da dica que acabou de ser liberada, para ganhar o selo "Nova!". */
    val indiceDicaNova: Int? = null,
    val custoDica: Int = 15,
    /** Aviso do último palpite, por exemplo "“Elefante” não é. Você perdeu 1 vida…". */
    val aviso: String? = null,
    val iaLigada: Boolean = true,
    val perguntasIa: List<PerguntaIaUi> = emptyList(),
    val limitePerguntas: Int = 10,
    val custoPergunta: Int = 5,
    val iaPensando: Boolean = false,
    val erroIa: String? = null,
)

/** Escolhas da tela de Ajustes. */
data class AjustesUi(
    val categorias: Set<Categoria> = Categoria.entries.toSet(),
    val dificuldade: Dificuldade = Dificuldade.MEDIO,
    val cronometro: Boolean = true,
    val perguntasIa: Boolean = true,
)

/** Resultado mostrado na janela de fim de partida. */
data class ResultadoPartidaUi(
    val venceu: Boolean,
    val personagem: String,
    val pontos: Int,
)

/** "1:05" a partir de 65 segundos. */
fun formatarTempo(segundos: Int): String = "%d:%02d".format(segundos / 60, segundos % 60)
