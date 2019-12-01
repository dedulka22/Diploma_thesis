package com.teamtreehouse.berbyt.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamtreehouse.berbyt.R
import com.teamtreehouse.berbyt.data.model.UserDbModel


/*
* Author: Deana Mareková
* Description: CreateAccountActivity for create new account and verify email
* Licence: MIT
* */
class CreateAccountActivity : BaseActivity() {


    lateinit var et_first_name: EditText
    lateinit var et_last_name: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var btn_reg: Button


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCloud: FirebaseFirestore

    private val TAG = "CreateAccountActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_create_account)

        et_first_name = findViewById(R.id.et_first_name)
        et_last_name = findViewById(R.id.et_last_name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btn_reg = findViewById(R.id.btn_reg)
        mProgressBar = ProgressDialog(this)



        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()


        btn_reg.setOnClickListener {
            createNewAccount()
        }

    }


    private fun createNewAccount() {

        val first_name = et_first_name.text.toString()
        val last_name = et_last_name.text.toString()
        val email = email.text.toString()
        val passwd = password.text.toString()

        if (!TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(passwd)
        ) {
            mProgressBar.setMessage("Registrování uživatele...")
            mProgressBar.show()

            mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this) { task ->
                    mProgressBar.hide()
                    val userDb: UserDbModel = UserDbModel(first_name, last_name)
                    var userId = mAuth.currentUser?.uid

                    mCloud.collection("Users").document(userId.toString())
                        .set(userDb)
                        .addOnSuccessListener { document ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${userId.toString()}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(
                            baseContext, "Registrace úspěšná, prosím potvrďte ověřovací email.",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(
                            Intent(
                                this@CreateAccountActivity,
                                LoginActivity::class.java
                            )
                        )
                        verifyEmail()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        val errorMessage = task.exception?.message

                        Toast.makeText(
                            this@CreateAccountActivity,
                            "Registrácia zlyhala --> Error : $errorMessage",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return@addOnCompleteListener
                }
        } else {
            Toast.makeText(baseContext, "Prosím vyplňte všechna povinná pole", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@CreateAccountActivity,
                        "Ověření emailu byl poslán na " + mUser.email,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        this@CreateAccountActivity,
                        "Neúspěšně poslání ověření emailu.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

