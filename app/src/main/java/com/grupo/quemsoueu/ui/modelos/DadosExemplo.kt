package com.grupo.quemsoueu.ui.modelos

/**
 * Dados de exemplo usados nos @Preview das telas (para ver o visual no Android Studio).
 * Não são os dados do jogo: as cartas de verdade vêm do Gustavo e as partidas do Gabriel.
 */
object DadosExemplo {

    val cartas = listOf(
        CartaUi(
            id = "bicicleta", nome = "Bicicleta", categoria = Categoria.OBJETOS, dificuldade = Dificuldade.MEDIO,
            dicas = listOf(
                "Não uso combustível.",
                "Tenho uma corrente.",
                "Posso ter cestinha na frente.",
                "Tenho pedais.",
                "Ando sobre duas rodas.",
            ),
        ),
        CartaUi(
            id = "farol", nome = "Farol", categoria = Categoria.OBJETOS, dificuldade = Dificuldade.DIFICIL, ativa = false,
            dicas = listOf(
                "Fico parado o tempo todo.",
                "Trabalho mais à noite.",
                "Moro perto do mar.",
                "Sou alto e às vezes listrado.",
                "Mostro o caminho para os navios.",
            ),
        ),
        CartaUi(
            id = "girafa", nome = "Girafa", categoria = Categoria.ANIMAIS, dificuldade = Dificuldade.MEDIO,
            dicas = listOf(
                "Vivo na savana africana.",
                "Sou herbívoro e como folhas do alto das árvores.",
                "Tenho o pescoço mais longo de todos os animais.",
                "Minha pele tem manchas marrons.",
                "Sou o animal mais alto do mundo.",
            ),
        ),
        CartaUi(
            id = "guarda-chuva", nome = "Guarda-chuva", categoria = Categoria.OBJETOS, dificuldade = Dificuldade.FACIL,
            dicas = listOf(
                "Passo a maior parte do tempo fechado.",
                "Tenho um cabo.",
                "Às vezes viro do avesso com o vento.",
                "Abro quando o céu fica cinza.",
                "Protejo você da chuva.",
            ),
        ),
        CartaUi(
            id = "pinguim", nome = "Pinguim", categoria = Categoria.ANIMAIS, dificuldade = Dificuldade.FACIL,
            dicas = listOf(
                "Vivo em lugares muito frios.",
                "Sou uma ave, mas não voo.",
                "Nado muito bem.",
                "Tenho penas pretas e brancas.",
                "Ando desajeitado sobre o gelo.",
            ),
        ),
    )

    val partidas = listOf(
        PartidaUi(1, "Ana", "Girafa", Categoria.ANIMAIS, 162, dicasUsadas = 2, perguntasIa = 1, tempoSegundos = 48),
        PartidaUi(2, "Lucas", "Bicicleta", Categoria.OBJETOS, 140, dicasUsadas = 2, perguntasIa = 3, tempoSegundos = 65),
        PartidaUi(3, "Bia", "Pinguim", Categoria.ANIMAIS, 118, dicasUsadas = 3, perguntasIa = 2, tempoSegundos = 72),
        PartidaUi(4, "Rafa", "Guarda-chuva", Categoria.OBJETOS, 95, dicasUsadas = 3, perguntasIa = 4, tempoSegundos = 100),
        PartidaUi(5, "Jô", "Farol", Categoria.OBJETOS, 60, dicasUsadas = 4, perguntasIa = 6, tempoSegundos = 151),
        PartidaUi(6, "Caio", "Pinguim", Categoria.ANIMAIS, 52, dicasUsadas = 5, perguntasIa = 2, tempoSegundos = 170),
    )

    /** O mesmo momento de jogo dos mockups: 2 vidas, 3 dicas abertas, 3 perguntas feitas. */
    val estadoJogo = EstadoJogoUi(
        categoria = Categoria.ANIMAIS,
        dificuldade = Dificuldade.MEDIO,
        rodada = 1,
        vidas = 2,
        tempoTexto = "01:24",
        progressoTempo = 0.58f,
        pontos = 85,
        dicas = cartas[2].dicas,
        dicasReveladas = 3,
        indiceDicaNova = 2,
        aviso = "“Elefante” não é. Você perdeu 1 vida e ganhou uma dica!",
        perguntasIa = listOf(
            PerguntaIaUi("É um animal?", RespostaIa.SIM),
            PerguntaIaUi("Ele voa?", RespostaIa.NAO),
            PerguntaIaUi("É maior que um cavalo?", RespostaIa.SIM),
        ),
    )

    val ajustes = AjustesUi(
        categorias = setOf(Categoria.FAMOSOS, Categoria.ANIMAIS, Categoria.OBJETOS),
        dificuldade = Dificuldade.MEDIO,
    )
}
