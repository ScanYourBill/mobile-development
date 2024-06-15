package com.example.scanyourbill.di

import android.content.Context
import com.example.scanyourbill.data.ApiConfig
import com.example.scanyourbill.data.TokenProvider
import com.example.scanyourbill.data.UserPreference
import com.example.scanyourbill.data.dataStore
import com.example.scanyourbill.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(context, TokenProvider(pref))
        return UserRepository.getInstance(pref, apiService)
    }
}