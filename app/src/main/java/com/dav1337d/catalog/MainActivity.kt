package com.dav1337d.catalog

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.dav1337d.catalog.extensions.setupWithNavController


class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private val bottomNavBarStateList = arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked)
    )
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
//        bottomNavigationView.itemIconTintList = null
//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            Log.i("hallo", "hallo 0")
//            when (item.itemId) {
//                R.id.tv_home -> {
//                    Log.i("hallo", "hallo 1")
//                    updateBottomNavBarColor(resources.getColor(R.color.dark_red))
//                }
//                R.id.books_home -> {
//                    Log.i("hallo", "hallo 2")
//                    updateBottomNavBarColor(resources.getColor(R.color.green_munsell))
//                }
//                R.id.games_home -> {
//                    Log.i("hallo", "hallo 3")
//                    updateBottomNavBarColor(resources.getColor(R.color.carrot))
//                }
//            }
//            true
//        }

        val navGraphIds = listOf(
            R.navigation.games_home,
            R.navigation.tv_home,
            R.navigation.books_home
        )

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })

        currentNavController = controller
    }

    private fun updateBottomNavBarColor(currentSelectedColor: Int) {
        val colorList = intArrayOf(
            ContextCompat.getColor(this, currentSelectedColor),
            ContextCompat.getColor(this, R.color.colorAccent)
        )
        val colorStateList = ColorStateList(bottomNavBarStateList, colorList)
        bottomNavigationView.itemIconTintList = colorStateList
        bottomNavigationView.itemTextColor = colorStateList
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
