package com.gretea5.finder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gretea5.finder.R
import com.gretea5.finder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val appBarConfiguration = AppBarConfiguration(setOf(
        R.id.mapFragment,
        R.id.listFragment,
        R.id.questionnaireFragment
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initNavigation()

        findNavController(R.id.nav_host).addOnDestinationChangedListener{ _, destination, _ ->
            binding.navBar.isVisible = appBarConfiguration.topLevelDestinations.contains(destination.id)
        }
    }

    private fun initNavigation() {
        NavigationUI.setupWithNavController(binding.navBar, findNavController(R.id.nav_host))
    }
}