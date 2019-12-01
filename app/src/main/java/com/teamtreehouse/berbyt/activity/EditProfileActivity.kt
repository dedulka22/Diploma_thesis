package com.teamtreehouse.berbyt.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.teamtreehouse.berbyt.R

/*
* Author: Deana Mareková
* Description: EditProfileActivity for edit profile
* Licence: MIT
* */
class EditProfileActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCloud: FirebaseFirestore
    private lateinit var user: FirebaseUser

    private lateinit var ed_name: EditText
    private lateinit var ed_last_name: EditText
    private lateinit var ed_editBtn: Button

    private var name:String? = ""
    private var last_name: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_edit_profile)

        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()
        user = mAuth.currentUser!!

        ed_name = findViewById(R.id.ed_first_name)
        ed_last_name = findViewById(R.id.ed_last_name)
        ed_editBtn = findViewById(R.id.ed_editProfile)

        name = intent.extras!!.getString("name")
        last_name = intent.extras!!.getString("last_name")

        if (!name.isNullOrEmpty()) {
            ed_name.setText(name)
        }

        if (!last_name.isNullOrEmpty()) {
            ed_last_name.setText(last_name)
        }

        ed_editBtn.setOnClickListener {
            updateProfile(ed_name.text.toString(), ed_last_name.text.toString())

            val intent = Intent(
                this@EditProfileActivity,
                ProfileActivity::class.java
            )
            intent.putExtra("fullName", ed_name.text.toString() + " " + ed_last_name.text.toString())
            startActivity(intent)

            Toast.makeText(
                baseContext, "Profil byl změněn.",
                Toast.LENGTH_SHORT
            ).show()

        }


    }

    fun updateProfile(name: String, lastName: String) {
        var userId = user!!.uid
        val batch = mCloud.batch()
        val userFeed = mCloud.collection("Users").document(userId)

        userFeed.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mCloud.collection("Users").document(userId)
                    .update( "first", name, "last", lastName )
            }
        }
        batch.commit()
    }
}
