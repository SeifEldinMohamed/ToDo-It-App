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

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var binding: ActivityMainBinding
    private var appBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appBarConfiguration = AppBarConfiguration(
            findNavController(R.id.nav_host_fragment).graph,
            binding.drawerLayout,
        )
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment), appBarConfiguration!!) //the most important part
        binding.navDraw.setNavigationItemSelectedListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration!!) ||
                super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_share -> Toast.makeText(this, "share", Toast.LENGTH_SHORT).show()
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

