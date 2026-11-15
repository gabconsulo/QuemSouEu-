package com.grupo.quemsoueu.navegacao

import com.grupo.quemsoueu.ui.componentes.AbaJogador

const val ROTA_INICIO = "inicio"
const val ROTA_JOGO = "jogo"
const val ROTA_AJUSTES = "ajustes"
const val ROTA_RANKING = "ranking"
const val ROTA_LOGIN = "login"
const val ROTA_ADMIN = "admin"
const val ROTA_CARTA = "carta/{id}"
const val ID_CARTA_NOVA = "nova"

fun rotaCarta(id: String): String = "carta/$id"

fun rotaDaAba(aba: AbaJogador): String =
    when (aba) {
        AbaJogador.JOGAR -> ROTA_JOGO
        AbaJogador.RANKING -> ROTA_RANKING
        AbaJogador.AJUSTES -> ROTA_AJUSTES
    }

fun abaDaRota(rota: String?): AbaJogador? =
    when (rota) {
        ROTA_JOGO -> AbaJogador.JOGAR
        ROTA_RANKING -> AbaJogador.RANKING
        ROTA_AJUSTES -> AbaJogador.AJUSTES
        else -> null
    }
