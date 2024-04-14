package com.example.githubuser.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.di.Injection
import com.example.githubuser.ui.detail.DetailViewModel
import com.example.githubuser.ui.favourite.FavouriteViewModel
import com.example.githubuser.ui.follow.FollowViewModel
import com.example.githubuser.ui.home.HomeViewModel
import com.example.githubuser.ui.main.MainViewModel


class ViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->{
                return HomeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) ->{
                return MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->{
                return DetailViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(FavouriteViewModel::class.java) ->{
                return FavouriteViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(FollowViewModel::class.java) ->{
                return FollowViewModel(userRepository) as T
            }
        }
        throw IllegalArgumentException("Unknown viewmodel class ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var INSTANCE : ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context : Context) : ViewModelFactory{
            return INSTANCE ?: synchronized(ViewModelFactory::class.java){
                INSTANCE ?: ViewModelFactory(Injection.provideUserRepository(context))
            }
        }
    }
}