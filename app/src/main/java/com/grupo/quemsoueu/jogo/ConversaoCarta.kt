package com.grupo.quemsoueu.jogo

import com.grupo.quemsoueu.model.Carta
import com.grupo.quemsoueu.model.Categoria as CategoriaDados
import com.grupo.quemsoueu.model.Dificuldade as DificuldadeDados
import com.grupo.quemsoueu.ui.modelos.CartaUi
import com.grupo.quemsoueu.ui.modelos.Categoria
import com.grupo.quemsoueu.ui.modelos.Dificuldade

fun converterCategoria(categoria: CategoriaDados): Categoria =
    when (categoria) {
        CategoriaDados.PESSOA_REAL -> Categoria.FAMOSOS
        CategoriaDados.PERSONAGEM_FICTICIO -> Categoria.FILMES
        CategoriaDados.ANIMAL -> Categoria.ANIMAIS
        CategoriaDados.OBJETO -> Categoria.OBJETOS
        CategoriaDados.LUGAR -> Categoria.OBJETOS
    }

fun converterDificuldade(dificuldade: DificuldadeDados): Dificuldade =
    when (dificuldade) {
        DificuldadeDados.FACIL -> Dificuldade.FACIL
        DificuldadeDados.MEDIA -> Dificuldade.MEDIO
        DificuldadeDados.DIFICIL -> Dificuldade.DIFICIL
    }

fun converterCarta(carta: Carta): CartaUi =
    CartaUi(
        id = carta.id,
        nome = carta.personagem,
        categoria = converterCategoria(carta.categoria),
        dificuldade = converterDificuldade(carta.dificuldade),
        dicas = carta.dicas,
        ativa = carta.ativa
    )
