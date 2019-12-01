package com.teamtreehouse.berbyt.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamtreehouse.berbyt.R

/*
* Author: Deana Mareková
* Description: BaseActivity for show Toolbar, Layouts, DrawerLayout, Bottom nav
* Licence: MIT
* */
abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    lateinit var tv_name: TextView
    lateinit var tv_email: TextView
    private lateinit var mCloud: FirebaseFirestore
    lateinit var navView: NavigationView
    lateinit var mProgressBar: ProgressDialog


    protected fun onCreate(savedInstanceState: Bundle, layoutId: Int) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        mProgressBar = ProgressDialog(this)

        navView = findViewById(R.id.nav_view)
        val navHeaderView: View = navView.getHeaderView(0)
        navView.setNavigationItemSelectedListener(this)

    }


    fun initToolbar(toolbarId: Int) {
        mAuth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)


        toolbar.setTitle(title)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val user = mAuth.currentUser

        mCloud = FirebaseFirestore.getInstance()

        tv_name = navView.getHeaderView(0).findViewById(R.id.tv_name)
        tv_email = navView.getHeaderView(0).findViewById(R.id.tv_email)


        if (user != null) {
            val currUser = mCloud.collection("Users").document(user!!.uid)
            val currUserFb = user.providerId

            currUser
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.data != null) {
                            if (user.isEmailVerified) {
                                tv_name.text =
                                    document["first"].toString() + " " + document["last"].toString()
                                tv_email.text = user.email
                            }
                        } else if (document.data == null) {
                            user.providerData.forEach { profile ->
                                tv_name.text = user.displayName
                                tv_email.text = profile.email

                            }
                        }


                        Log.d("", "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d("", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("", "get failed with ", exception)
                }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        if (user != null && user!!.isEmailVerified) { // for create user
            println("overeni: " + user!!.isEmailVerified)
            navView.menu.setGroupVisible(R.id.group_user, true)
            navView.menu.findItem(R.id.nav_search).setVisible(true)
            navView.menu.findItem(R.id.nav_log_out).setVisible(true)
        } else if (user != null) { // for FB
            println("overeni FB: " + user!!.isEmailVerified)
            navView.menu.setGroupVisible(R.id.group_user, true)
            navView.menu.findItem(R.id.nav_search).setVisible(true)
            navView.menu.findItem(R.id.nav_log_out).setVisible(true)
        } else {
            navView.menu.setGroupVisible(R.id.group_visitor, true)
            navView.menu.findItem(R.id.nav_search).setVisible(true)
        }

        navView.setNavigationItemSelectedListener(this)
    }

    fun initLayout(layoutId: Int) {
        // get a hold of the instance of your layout
        val dynamicContent: LinearLayout = findViewById(R.id.dynamic_content)

        // assuming your Wizard content is in content_wizard.xml
        val wizardView: View = layoutInflater
            .inflate(layoutId, dynamicContent, false)

        // add the inflated View to the layout
        dynamicContent.addView(wizardView)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun initBottomNav() {
        mProgressBar = ProgressDialog(this)
        val bottomNavigationView = findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.selectedItemId = R.id.action_feed
        bottomNavigationView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_AUTO
        bottomNavigationView.isItemHorizontalTranslationEnabled = true
        bottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.action_feed ->
                        startActivity(
                            Intent(
                                this@BaseActivity,
                                RentalActivity::class.java
                            )
                        )
                    R.id.action_map -> {
                        if (mAuth.currentUser != null) {
                            startActivity(
                                Intent(
                                    this@BaseActivity,
                                    MapActivity::class.java
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@BaseActivity,
                                "Nejprve se musíte přihlásit.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    R.id.action_favorite -> {
                        if (mAuth.currentUser != null) {
                            mProgressBar.setMessage("Načítaní...")
                            mProgressBar.show()
                            startActivity(
                                Intent(
                                    this@BaseActivity,
                                    FavouriteFeedsActivity::class.java
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@BaseActivity,
                                "Nejprve se musíte přihlásit.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
                return true
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.search -> {
                startActivity(
                    Intent(
                        this@BaseActivity,
                        SearchActivity::class.java
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        mAuth = FirebaseAuth.getInstance()
        mProgressBar = ProgressDialog(this)

        when (item.itemId) {
            R.id.nav_reg -> {
                startActivity(
                    Intent(
                        this@BaseActivity,
                        CreateAccountActivity::class.java
                    )
                )
            }
            R.id.nav_log_in -> {
                startActivity(
                    Intent(
                        this@BaseActivity,
                        MainActivity::class.java
                    )
                )
            }
            R.id.nav_home -> {
                startActivity(
                    Intent(
                        this@BaseActivity,
                        ProfileActivity::class.java
                    )
                )
            }
            R.id.nav_search -> {
                startActivity(
                    Intent(
                        this@BaseActivity,
                        SearchActivity::class.java
                    )
                )
            }
            R.id.nav_star -> {
                mProgressBar.setMessage("Načítaní...")
                mProgressBar.show()
                startActivity(
                    Intent(
                        this@BaseActivity,
                        FavouriteFeedsActivity::class.java
                    )
                )
            }
            R.id.nav_log_out -> {
                if (mAuth.currentUser != null) {
                    mAuth.signOut()
                    // for FB log out
                    LoginManager.getInstance().logOut()
                    Toast.makeText(
                        baseContext, "Nyní jste odhlášeni.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@BaseActivity,
                            MainActivity::class.java
                        )
                    )
                }

            }
            R.id.nav_contact ->
                startActivity(
                    Intent(
                        this@BaseActivity,
                        ContactActivity::class.java
                    )
                )

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateUI() {
        val refresh = Intent(this@BaseActivity, MainActivity::class.java)
        startActivity(refresh)
        finish()
    }


}

