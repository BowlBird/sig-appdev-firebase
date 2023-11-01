package com.github.bowlbird.sig_appdev_firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.security.auth.callback.Callback

data class FirebaseUiState(
    val messages: List<String> = listOf()
)

class FirebaseViewModel(
    private val repository: FirebaseRepository,
) : ViewModel() {
    private var _firebaseUiState: MutableStateFlow<FirebaseUiState> = MutableStateFlow(
        FirebaseUiState()
    )
    val firebaseUiState: StateFlow<FirebaseUiState> = _firebaseUiState

    init {
        repository.get { messages ->
            _firebaseUiState.update {
                it.copy(messages = messages ?: listOf<String>())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val firebaseRepository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FirebaseApplication).firebaseRepository
                FirebaseViewModel(
                    repository = firebaseRepository,
                )
            }
        }
    }

    fun pushMessage(message: String) {
        repository.push(message, firebaseUiState.value.messages.size)
    }
}