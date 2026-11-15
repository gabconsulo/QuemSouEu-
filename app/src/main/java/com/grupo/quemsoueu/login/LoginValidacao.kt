package com.grupo.quemsoueu.login

const val USUARIO_ADMIN = "admin"
const val SENHA_ADMIN = "admin123"

enum class Perfil { JOGADOR, ADMIN }

fun validarLogin(usuario: String, senha: String): String? {
    if (usuario.isBlank() || senha.isBlank()) {
        return "Preencha usuário e senha."
    }
    if (usuario.trim().lowercase() != USUARIO_ADMIN || senha != SENHA_ADMIN) {
        return "Usuário ou senha incorretos. Tente de novo."
    }
    return null
}
