package com.teamtreehouse.berbyt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamtreehouse.berbyt.R

/*
* Author: Deana Mareková
* Description: CreateNoteActivity for create note
* Licence: MIT
* */
class CreateNoteActivity : BaseActivity() {

    lateinit var txt_add_note: EditText
    lateinit var btn_add_note: Button
    private var hashidForLinkSR: String? = ""
    private var hashidForLinkUD: String? = ""
    private var added_note: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_create_note)

        txt_add_note = findViewById(R.id.txt_add_note)
        btn_add_note = findViewById(R.id.btn_add_note)

        hashidForLinkSR = intent.extras!!.getString("hash_id")
        hashidForLinkUD = intent.extras!!.getString("id")
        added_note = intent.extras!!.getString("add_note")

        if (!added_note.isNullOrEmpty()){
            txt_add_note.setText(added_note)
        }

        btn_add_note.setOnClickListener {
            val add_note = txt_add_note.text.toString()

            if (hashidForLinkSR != null) {
                addNoteIntoFeed(hashidForLinkSR!!, add_note)

            } else if (hashidForLinkUD != null) {
                addNoteIntoFeed(hashidForLinkUD!!, add_note)
            }
            val intent = Intent(this@CreateNoteActivity, FavouriteFeedsActivity::class.java)
            intent.putExtra("add_note", add_note)
            startActivity(intent)
        }
    }

    fun addNoteIntoFeed(feedId: String, notes: String) {
        val mAuth = FirebaseAuth.getInstance()
        val mCloud = FirebaseFirestore.getInstance()

        var userId = mAuth.currentUser!!.uid


        if (userId != null) {
            val batch = mCloud.batch()
            var findFeed = mCloud.collection("FavouriteFeeds").whereEqualTo("userId", userId)
                .whereEqualTo("feedId", feedId)


            findFeed.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        mCloud.collection("FavouriteFeeds").document(document.id).update("note", notes)
                    }
                }
            }
            batch.commit()
            Log.d("", "Smazán inzerát z oblíbených")
        } else {
            println(" addNoteIntoFeed failed")
        }
    }
}
