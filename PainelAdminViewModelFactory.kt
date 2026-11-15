package com.exemplo.quemsoueu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exemplo.quemsoueu.repository.CartaRepository
import com.exemplo.quemsoueu.repository.FirestoreCartaRepository

/**
 * Factory que ensina o ViewModelProvider a criar o PainelAdminViewModel,
 * que recebe o repositório pelo construtor (o ViewModel padrão não aceita parâmetros).
 */
class PainelAdminViewModelFactory(
    private val repository: CartaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PainelAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PainelAdminViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }

    companion object {
        /** Atalho para criar a Factory já com o repositório do Firestore. */
        fun comFirestore(): PainelAdminViewModelFactory =
            PainelAdminViewModelFactory(FirestoreCartaRepository())
    }
}
