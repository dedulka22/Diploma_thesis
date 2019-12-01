package com.teamtreehouse.berbyt.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.teamtreehouse.berbyt.R
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.facebook.AccessToken
import com.facebook.login.LoginBehavior

/*
* Author: Deana Mareková
* Description: MainActivity for homepage and SignIn
* Licence: MIT
* */
class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var text_forgot: TextView

    lateinit var btn_register: Button
    lateinit var btn_signIn: Button
    lateinit var facebookSignInButton: LoginButton
    private lateinit var auth: FirebaseAuth


    var callbackManager: CallbackManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_main)

        btn_register = findViewById(R.id.button_register)
        btn_signIn = findViewById(R.id.button_signIn)
        facebookSignInButton = findViewById<View>(R.id.facebook_sign_in_button) as LoginButton
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        mProgressBar = ProgressDialog(this)
        text_forgot = findViewById(R.id.text_forgot)

        auth = FirebaseAuth.getInstance()

        text_forgot.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    ForgotPasswordActivity::class.java
                )
            )
        }


        btn_signIn.setOnClickListener {
            if (isOnline(this) == true) {
                loginUser()
            } else {
                Toast.makeText(
                    this
                    , "Nejste připojeni k internetu.", Toast.LENGTH_LONG
                ).show()
            }
        }

        btn_register.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    CreateAccountActivity::class.java
                )
            )
        }

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        facebookSignInButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY)
        facebookSignInButton.setReadPermissions("email", "public_profile")

        facebookSignInButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)
                }
            })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        var currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    var user = auth.currentUser
                    if (user!!.email == null) {
                        var userEmail: String = ""
                        user!!.providerData.forEach { userInfo ->
                            userEmail = userInfo.email.toString()
                        }
                        Toast.makeText(
                            this@MainActivity, "Autentikace byla úspěšná." + userEmail,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (user != null) {
                        updateUI(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Autentikace byla neúspěšná!",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun loginUser() {
        val email = email.text.toString()
        val password = password.text.toString()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgressBar.setMessage("Přihlašuji...")
            mProgressBar.show()
            Log.d(TAG, "Logging in user.")
            auth = FirebaseAuth.getInstance()


            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        var user = auth.currentUser

                        mProgressBar.dismiss()

                        if (user != null && user.isEmailVerified) {

                            // Sign in success, update UI with signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")

                            Toast.makeText(
                                this@MainActivity, "Autentikace byla úspěšná." + user!!.email,
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(user)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Musíte mít ověřený email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        val errorMessage = task.getException()!!.message

                        Toast.makeText(
                            this@MainActivity,
                            "Autentikáce byla neúspěšná --> Error : $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

        } else {
            Toast.makeText(this, "Prosím vyplňte všechna povinná pole", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this@MainActivity, SearchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}



