package com.teamtreehouse.berbyt.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.teamtreehouse.berbyt.R

/*
* Author: Deana Mareková
* Description: LoginActivity for LogIn, forgot passwd
* Licence: MIT
* */
class LoginActivity : BaseActivity() {


    private val TAG = "LoginActivity"
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var btn_login: Button
    lateinit var text_forg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btn_login = findViewById(R.id.login)
        mProgressBar = ProgressDialog(this)
        text_forg = findViewById(R.id.text_forg)


        btn_login.setOnClickListener {
            loginUser()
        }

        text_forg.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    ForgotPasswordActivity::class.java
                )
            )
        }
    }


    private fun loginUser() {
        val email = email.text.toString()
        val password = password.text.toString()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            Log.d(TAG, "Logging in user.")
            val mAuth = FirebaseAuth.getInstance()

            mProgressBar.setMessage("Přihlašuji...")
            mProgressBar.show()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        var user = mAuth.currentUser
                        mProgressBar.dismiss()

                        if (user != null && user.isEmailVerified) {

                            // Sign in success, update UI with signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")

                            Toast.makeText(
                                this@LoginActivity, "Autentikáce byla úspěšná." + user!!.email,
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Musíte mít ověřený email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        val errorMessage = task.exception!!.message

                        Toast.makeText(
                            this@LoginActivity,
                            "Autentikáce byla neúspěšná --> Error : $errorMessage",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

        } else {
            Toast.makeText(this, "Prosím vyplňte všechna povinná pole", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val intent = Intent(this@LoginActivity, SearchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}
