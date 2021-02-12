package com.harish.todo_app_pits.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.harish.todo_app_pits.R
import com.harish.todo_app_pits.utils.UserUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkAndCreateUserId()
        navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.allTodosFragment,
                R.id.yourTodoFragment
            )
        )

        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
        //bottomNavigationView.selectedItemId = R.id.yourTodoFragment
    }

    private fun checkAndCreateUserId() {
        val userID = UserUtils.init(this).getLocalUserId()
        if(userID == -1 || userID==null)
        {
            UserUtils.init(this).setLocalUserId((500..999).random())
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}