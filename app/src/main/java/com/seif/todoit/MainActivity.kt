package com.seif.todoit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.seif.todoit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        binding.navDraw.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        // set up navigation drawer


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_share -> Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        }
        return true
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