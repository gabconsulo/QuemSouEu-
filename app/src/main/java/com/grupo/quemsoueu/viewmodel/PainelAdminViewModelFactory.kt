package com.grupo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grupo.quemsoueu.BuildConfig
import com.grupo.quemsoueu.repository.CartaRepository
import com.grupo.quemsoueu.repository.GeminiPerguntaRepository

class PainelAdminViewModelFactory(
    private val cartaRepository: CartaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PainelAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PainelAdminViewModel(
                cartaRepository,
                GeminiPerguntaRepository(BuildConfig.GEMINI_API_KEY),
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
