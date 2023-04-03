package com.example.githubuser.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.githubuser.databinding.ActivityMainBinding

private  val Context.dataStore :
        DataStore<Preferences> by preferencesDataStore(name = "Setting" )

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbar?.title=""
        setSupportActionBar(binding?.toolbar)

        val viewModel = obtainViewModel()

        viewModel.getThemeSetting().observe(this){
            if(it){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun obtainViewModel() : UserViewModel{
        val factory : ViewModelFactory = ViewModelFactory.getInstance(this, dataStore)
        val viewModel : UserViewModel  by viewModels {
            factory
        }
        return viewModel
    }
}