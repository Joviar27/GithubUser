package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title=""
        setSupportActionBar(binding.toolbar)
    }
}