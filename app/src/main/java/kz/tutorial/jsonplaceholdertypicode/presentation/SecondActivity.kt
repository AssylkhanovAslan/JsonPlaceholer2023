package kz.tutorial.jsonplaceholdertypicode.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kz.tutorial.jsonplaceholdertypicode.R

class SecondActivity : AppCompatActivity() {

    private lateinit var bnvMain: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        assignViews()
        initBottomNav()
    }

    private fun assignViews() {
        bnvMain = findViewById(R.id.bnv_main)
    }

    private fun initBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bnvMain.setupWithNavController(navController)
        //Forgot how to connect nav with bottomNav
    }
}