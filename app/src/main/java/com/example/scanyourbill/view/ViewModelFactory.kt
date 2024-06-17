package com.example.scanyourbill.view


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scanyourbill.data.repository.UserRepository
import com.example.scanyourbill.data.repository.WalletRepository

import com.example.scanyourbill.di.Injection
import com.example.scanyourbill.view.login.LoginViewModel
import com.example.scanyourbill.view.wallet.WalletViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(WalletViewModel::class.java) -> {
                WalletViewModel(walletRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: buildFactory(context).also { INSTANCE = it }
            }
        }

        private fun buildFactory(context: Context): ViewModelFactory {
            val userRepository = Injection.provideUserRepository(context)
            val walletRepository = Injection.provideWalletRepository(context)

            return ViewModelFactory(userRepository, walletRepository)
        }
    }
}
