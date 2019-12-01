package com.teamtreehouse.berbyt.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.teamtreehouse.berbyt.R

/*
* Author: Deana Mareková
* Description: ForgotPasswordActivity for forgot passwd
* Licence: MIT
* */
class ForgotPasswordActivity : BaseActivity() {

    lateinit var btn_forgot: Button
    lateinit var email: EditText
    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_forgot_password)

        email = findViewById(R.id.email_forg)
        btn_forgot = findViewById(R.id.btn_forgot)
        mAuth = FirebaseAuth.getInstance()

        btn_forgot.setOnClickListener{
            sendChangePassword()
        }

    }

    fun sendChangePassword() {
        val email = email.text.toString()

        mAuth!!.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(
                        Intent(
                            this@ForgotPasswordActivity,
                            MainActivity::class.java
                        )
                    )
                    Toast.makeText(this@ForgotPasswordActivity, "Přáve Vám byl odoslán obnovovací email pro heslo.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                }
            }
    }

}