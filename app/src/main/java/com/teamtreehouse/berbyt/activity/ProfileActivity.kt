package com.teamtreehouse.berbyt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.teamtreehouse.berbyt.R




/*
* Author: Deana Mareková
* Description: ProfileActivity for profile
* Licence: MIT
* */
class ProfileActivity : BaseActivity() {

    private val TAG = "ProfileActivity"
    private lateinit var name: TextView
    var last_name: String = ""
    var ed_name: String = ""
    var fullName: String = ""
    private lateinit var email: TextView
    private lateinit var editProfile: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCloud: FirebaseFirestore
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_profile)

        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()

        user = mAuth.currentUser!!

        email = findViewById(R.id.email)
        name = findViewById(R.id.first_name)
        editProfile = findViewById(R.id.editProfile)


        val bundle = intent.extras
        if (bundle != null) {
            fullName = intent.extras!!.getString("fullName").toString()
        }



        if (user != null) {
            val currUser = mCloud.collection("Users").document(user!!.uid)

            currUser
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.data != null) {
                            if (fullName == "") {
                                name.text =
                                    document["first"].toString() + " " + document["last"].toString()
                                email.text = user.email

                            } else if (fullName.isNotEmpty()) {
                                name.setText(fullName)
                                email.text = user.email
                            }
                            ed_name = document["first"].toString()
                            last_name = document["last"].toString()

                        } else if (document.data == null) {

                            user.providerData.forEach { profile ->
                                name.text = profile.displayName
                                email.text = profile.email
                            }

                            editProfile.visibility = View.GONE
                        }

                        editProfile.setOnClickListener {
                            val intent =
                                Intent(
                                    this@ProfileActivity,
                                    EditProfileActivity::class.java
                                )

                            //split full name
                            if (fullName.isNotEmpty()) {
                                val firstSpace = fullName.indexOf(" ") // detect the first space character

                                val firstName = fullName.substring( 0, firstSpace)  // get everything upto the first space character
                                val lastName = fullName.substring(firstSpace)
                                    .trim() // get everything after the first space, trimming the spaces off

                                intent.putExtra("name", firstName)
                                intent.putExtra("last_name", lastName)
                                startActivity(intent)
                            } else {
                                intent.putExtra("name", ed_name)
                                intent.putExtra("last_name", last_name)
                                startActivity(intent)

                            }
                        }


                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }




        } else {
            startActivity(
                Intent(
                    this@ProfileActivity,
                    MainActivity::class.java
                )
            )
            Toast.makeText(
                baseContext, "Nejprve se musíte přihlásit.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }


}
