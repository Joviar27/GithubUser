package com.example.githubuser.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.di.Injection


class ViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(userRepository) as T
        }
        else if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var INSTANCE : ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context : Context, dataStore: DataStore<Preferences>) : ViewModelFactory{
            return INSTANCE ?: synchronized(ViewModelFactory::class.java){
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context, dataStore))
            }
        }
    }
}