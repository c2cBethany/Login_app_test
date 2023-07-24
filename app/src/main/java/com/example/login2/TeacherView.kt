package com.example.login2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.login2.databinding.TeacherViewBinding
import com.example.login2.datasource.User
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TeacherView : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: TeacherViewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TeacherViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val navView: NavigationView = binding.navView
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val headerView: View = navView.getHeaderView(0)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val userId = firebaseAuth.currentUser?.uid

        val userEmail: TextView = headerView.findViewById(R.id.emailHeader)
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser?.let {
                userEmail.text = it.email
            }
        }

        val headerFullName: TextView = headerView.findViewById(R.id.headerFullName)
        val headerStatus: TextView = headerView.findViewById(R.id.headerStatus)
        if (userId.toString().isNotEmpty()) {
            databaseReference.child(userId.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)!!
                    headerFullName.text = user.firstName + " " + user.lastName
                    headerStatus.text = user.status
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@TeacherView,
                        "Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile, R.id.nav_post, R.id.nav_forum
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity3, menu)
        return true
    }

    //opens nav drawer
    override fun onSupportNavigateUp(): Boolean {
        23
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun logOut(item: MenuItem) {
        firebaseAuth.signOut()
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
        finish()
    }

    fun startHome(item: MenuItem) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}