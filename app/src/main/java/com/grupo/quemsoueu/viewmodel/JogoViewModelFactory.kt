package com.grupo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grupo.quemsoueu.BuildConfig
import com.grupo.quemsoueu.repository.CartaRepository
import com.grupo.quemsoueu.repository.GeminiPerguntaRepository

class JogoViewModelFactory(
    private val repository: CartaRepository,
    private val geminiRepository: GeminiPerguntaRepository =
        GeminiPerguntaRepository(BuildConfig.GEMINI_API_KEY),
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JogoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JogoViewModel(repository, geminiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
