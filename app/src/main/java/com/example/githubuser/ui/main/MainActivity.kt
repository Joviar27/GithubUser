package com.example.githubuser.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbar?.title=""
        binding?.toolbar?.popupTheme
        setSupportActionBar(binding?.toolbar)

        viewModel = obtainViewModel()

        viewModel.darkMode.observe(this){
            if(it)AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun obtainViewModel() : MainViewModel {
        val factory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel : MainViewModel by viewModels {
            factory
        }
        return viewModel
    }
}