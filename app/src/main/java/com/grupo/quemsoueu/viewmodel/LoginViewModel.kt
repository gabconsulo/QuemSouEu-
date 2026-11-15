package com.grupo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import com.grupo.quemsoueu.login.Perfil
import com.grupo.quemsoueu.login.validarLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEntrar(usuario: String, senha: String) {
        val erro = validarLogin(usuario, senha)

        if (erro != null) {
            _uiState.update { it.copy(erro = erro) }
            return
        }

        _uiState.update { it.copy(perfil = Perfil.ADMIN, erro = null) }
    }

    fun onLimparErro() {
        _uiState.update { it.copy(erro = null) }
    }

    fun onSair() {
        _uiState.value = LoginUiState()
    }
}
