package com.seif.todoit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater.inflate(R.menu.todo_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
/**
    onSupportNavigateUp comes from AppCompatActivity. You should override
    the method in the same activity where you define your NavHostFragment
    (probably your MainActivity). You override it so that the NavigationUI
    can correctly support the up navigation or even the drawer layout menu.
    AppCompatActivity and NavigationUI are two independent components, so you
    override the method in order to connect the two
 **/
}



//you don't need to override the onSupportNavigationUp method as Navigation will automatically handle the click events.
//You also don't need to override it if you want to handle the toolbar yourself.
// code:
// val navController = findNavController(R.id.nav_host_fragment)
//  val appBarConfiguration = AppBarConfiguration(navController.graph)
//  binding.toolbar.setupWithNavController(navController, appBarConfiguration)