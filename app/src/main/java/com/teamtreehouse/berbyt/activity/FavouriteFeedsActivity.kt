package com.teamtreehouse.berbyt.activity

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.teamtreehouse.berbyt.R
import com.teamtreehouse.berbyt.adapter.FavouriteFeedAdapter
import com.teamtreehouse.berbyt.data.*
import kotlinx.android.synthetic.main.content_favourite_feeds.*
import kotlinx.android.synthetic.main.lst_view_favourite_item.view.*
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException

class FavouriteFeedsActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCloud: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_feeds)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_favourite_feeds)

        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()

        getCountFavouriteFeeds {
            Log.d("TAG UD:", it.size.toString())
            var count = it

            recyclerView_favourite.layoutManager = LinearLayoutManager(this)
            recyclerView_favourite.adapter =
                FavouriteFeedAdapter(count)
        }

    }

    fun getCountFavouriteFeeds(myCallback: (List<String>) -> Unit) {
        var array_id = arrayListOf<String>()
        var size: Int = 0

        if (mAuth.currentUser != null) {
            var user = mAuth.currentUser!!.uid
            val feeds = mCloud.collection("FavouriteFeeds")
                .whereEqualTo("userId", user)
            // .whereEqualTo("portal", "UlovDomov")

            feeds
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            array_id.add(document.data.get("feedId").toString())
                            Log.d("", "${document.id} => ${document.data}")
                        }
                        size = array_id.size
                        println("size: " + size)
                        myCallback(array_id)

                    } else {
                        Log.d("", "Failed data")
                    }
                }
        } else {
            Toast.makeText(this, "Nejprve se musíte přihlásit.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}

