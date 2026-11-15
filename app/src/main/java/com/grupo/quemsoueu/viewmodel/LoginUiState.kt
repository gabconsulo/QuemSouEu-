package com.grupo.quemsoueu.viewmodel

import com.grupo.quemsoueu.login.Perfil

data class LoginUiState(
    val perfil: Perfil = Perfil.JOGADOR,
    val erro: String? = null
) {
    val ehAdmin: Boolean
        get() = perfil == Perfil.ADMIN
}
