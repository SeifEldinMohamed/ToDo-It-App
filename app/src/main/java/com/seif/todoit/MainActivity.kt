package com.seif.todoit

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.seif.todoit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
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
            R.id.menu_share -> shareApp()
            R.id.menu_rate -> rateApp()
        }
        binding.drawerLayout.close()
        return true
    }

    private fun rateApp() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "market://details?id=com.seif.todoit"
                )
            )
        )
    }

    private fun shareApp() {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Download Todo It app from here:\n" +
                    "https://play.google.com/store/apps/details?id=com.seif.todoit"
        )
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, "Choose the app you want to share with:"))
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

