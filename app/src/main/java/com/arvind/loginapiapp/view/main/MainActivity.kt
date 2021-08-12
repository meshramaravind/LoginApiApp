package com.arvind.loginapiapp.view.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.arvind.loginapiapp.R
import com.arvind.loginapiapp.databinding.ActivityMainBinding
import com.arvind.loginapiapp.model.DataModelLoginBody
import com.arvind.loginapiapp.utils.Resource
import com.arvind.loginapiapp.utils.hideKeyboard
import com.arvind.loginapiapp.viewmodel.DashboardViewModel
import com.arvind.loginapiapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.regex.Pattern

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    @ExperimentalCoroutinesApi
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dashboardViewModel

        initViews(binding)
        observeNavElements(binding, navHostFragment.navController)

    }

    private fun initViews(binding: ActivityMainBinding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            ?: return

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }

    }

    private fun observeNavElements(binding: ActivityMainBinding, navController: NavController) {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.dashboardFragment -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                }
                R.id.loginFragment -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                }

                else -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(true)
                }
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        navHostFragment.navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}