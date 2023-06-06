package com.example.androidinternshipproject


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidinternshipproject.api.ImageApi
import com.example.androidinternshipproject.repository.ImageRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    lateinit var viewModel: ImageViewModel
    private lateinit var factory: ImageViewModelFactory
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun initViewModel() {
        val repository = ImageRepository()
        factory = ImageViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[ImageViewModel::class.java]
    }
}