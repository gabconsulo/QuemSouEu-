package com.exemplo.quemsoueu.data

import com.exemplo.quemsoueu.model.Carta
import com.exemplo.quemsoueu.model.Categoria
import com.exemplo.quemsoueu.model.Dificuldade

/**
 * Lista inicial de cartas. Usada para popular o Firestore pela primeira vez
 * (botão "Importar cartas iniciais" no Painel Admin).
 */
object CartasIniciais {

    val todas: List<Carta> = listOf(
        Carta(
            id = "carta_001",
            personagem = "Albert Einstein",
            dicas = listOf(
                "Nasci na Alemanha",
                "Criei a Teoria da Relatividade",
                "Meu cabelo era famoso por ser bagunçado"
            ),
            categoria = Categoria.PESSOA_REAL,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_002",
            personagem = "Pelé",
            dicas = listOf(
                "Ganhei três Copas do Mundo",
                "Joguei pelo Santos",
                "Fiquei conhecido como O Rei"
            ),
            categoria = Categoria.PESSOA_REAL,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_003",
            personagem = "Santos Dumont",
            dicas = listOf(
                "Nasci em Minas Gerais",
                "Fui pioneiro da aviação",
                "Criei o 14-Bis"
            ),
            categoria = Categoria.PESSOA_REAL,
            dificuldade = Dificuldade.MEDIA
        ),
        Carta(
            id = "carta_004",
            personagem = "Marie Curie",
            dicas = listOf(
                "Nasci na Polônia",
                "Recebi dois Prêmios Nobel",
                "Estudei a radioatividade"
            ),
            categoria = Categoria.PESSOA_REAL,
            dificuldade = Dificuldade.DIFICIL
        ),
        Carta(
            id = "carta_005",
            personagem = "Harry Potter",
            dicas = listOf(
                "Tenho uma cicatriz na testa",
                "Estudo em Hogwarts",
                "Sou amigo de Ron e Hermione"
            ),
            categoria = Categoria.PERSONAGEM_FICTICIO,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_006",
            personagem = "Sherlock Holmes",
            dicas = listOf(
                "Moro na Baker Street",
                "Tenho um parceiro chamado Watson",
                "Sou um detetive famoso"
            ),
            categoria = Categoria.PERSONAGEM_FICTICIO,
            dificuldade = Dificuldade.MEDIA
        ),
        Carta(
            id = "carta_007",
            personagem = "Mônica",
            dicas = listOf(
                "Tenho um coelhinho de pelúcia",
                "Sou dona da rua do Limoeiro",
                "Sou baixinha, mas bato forte"
            ),
            categoria = Categoria.PERSONAGEM_FICTICIO,
            dificuldade = Dificuldade.MEDIA
        ),
        Carta(
            id = "carta_008",
            personagem = "Girafa",
            dicas = listOf(
                "Tenho um pescoço bem longo",
                "Como folhas de árvores altas"
            ),
            categoria = Categoria.ANIMAL,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_009",
            personagem = "Pinguim",
            dicas = listOf(
                "Não consigo voar",
                "Vivo em regiões muito frias",
                "Nado muito bem"
            ),
            categoria = Categoria.ANIMAL,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_010",
            personagem = "Polvo",
            dicas = listOf(
                "Tenho três corações",
                "Consigo mudar de cor",
                "Sou um molusco sem concha"
            ),
            categoria = Categoria.ANIMAL,
            dificuldade = Dificuldade.DIFICIL
        ),
        Carta(
            id = "carta_011",
            personagem = "Violão",
            dicas = listOf(
                "Tenho seis cordas",
                "Sou muito usado em serestas"
            ),
            categoria = Categoria.OBJETO,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_012",
            personagem = "Bússola",
            dicas = listOf(
                "Tenho uma agulha magnetizada",
                "Aponto sempre para o norte",
                "Ajudo na navegação"
            ),
            categoria = Categoria.OBJETO,
            dificuldade = Dificuldade.MEDIA
        ),
        Carta(
            id = "carta_013",
            personagem = "Torre Eiffel",
            dicas = listOf(
                "Fico em Paris",
                "Fui construída para a Exposição Universal de 1889",
                "Sou feita de ferro"
            ),
            categoria = Categoria.LUGAR,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_014",
            personagem = "Cristo Redentor",
            dicas = listOf(
                "Fico no Rio de Janeiro",
                "Estou no alto do Corcovado",
                "Meus braços estão abertos"
            ),
            categoria = Categoria.LUGAR,
            dificuldade = Dificuldade.FACIL
        ),
        Carta(
            id = "carta_015",
            personagem = "Pirâmide de Quéops",
            dicas = listOf(
                "Fico no Egito",
                "Sou uma das Sete Maravilhas do Mundo Antigo",
                "Sou feita de blocos de pedra"
            ),
            categoria = Categoria.LUGAR,
            dificuldade = Dificuldade.MEDIA
        )
    )
}
