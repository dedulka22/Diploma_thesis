package com.teamtreehouse.berbyt.adapter

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.teamtreehouse.berbyt.R
import kotlinx.android.synthetic.main.lst_view_favourite_item.view.*
import kotlinx.android.synthetic.main.lst_view_favourite_item.view.img_pic
import kotlinx.android.synthetic.main.lst_view_favourite_item.view.txt_locality
import kotlinx.android.synthetic.main.lst_view_favourite_item.view.txt_name
import kotlinx.android.synthetic.main.lst_view_favourite_item.view.txt_price
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import com.teamtreehouse.berbyt.activity.*


/*
* Author: Deana Mareková
* Description: FavouriteFeedAdapter for favourite feeds
* Licence: MIT
* */
class FavouriteFeedAdapter(
    var feeds: List<String>?
) : RecyclerView.Adapter<FavouriteViewHolder>() {

    var client = OkHttpClient()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCloud: FirebaseFirestore
    var hash_id = arrayListOf<String>()
    var id = arrayListOf<String>()


    lateinit var mProgressBar: ProgressDialog


    override fun getItemCount(): Int {
        val result = feeds!!.size
        if (result == 0) {
            return 1 // for view Info
        } else {
            return result
        }
    }

    fun getItem(): Int {
        return feeds!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        mProgressBar = ProgressDialog(parent.context)
        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()
        val cellForRow = layoutInflater.inflate(R.layout.lst_view_favourite_item, parent, false)
        return FavouriteViewHolder(cellForRow)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        var size = getItem()

        val feed = holder.view.findViewById<RelativeLayout>(R.id.feed)

        var addNote = holder.view.findViewById<TextView>(R.id.txt_note)
        var deleteImg = holder.view.findViewById<ImageView>(R.id.imageDelete)


        var user = mAuth.currentUser!!.uid
        val feedsSR = mCloud.collection("FavouriteFeeds")
            .whereEqualTo("userId", user)

        var nameFeeds = arrayListOf<String>()
        var localityFeeds = arrayListOf<String>()
        var priceFeeds = arrayListOf<String>()
        var imageFeeds = arrayListOf<String>()
        var portalFeeds = arrayListOf<String>()
        var found = arrayListOf<String>()
        var notes = arrayListOf<String>()


        if (size != 0) {
            feedsSR
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            nameFeeds.add(document.data.get("name").toString())
                            localityFeeds.add(document.data.get("locality").toString())
                            priceFeeds.add(document.data.get("price").toString())
                            imageFeeds.add(document.data.get("image").toString())
                            portalFeeds.add(document.data.get("portal").toString())
                            found.add(document.data.get("found").toString())
                            notes.add(document.data.get("note").toString())


                            if (document.data.get("portal").toString() == "Sreality") {
                                hash_id.add(document.data.get("feedId").toString())
                                controlExistFeedForSR(hash_id.get(position))
                            } else if (document.data.get("portal").toString() == "UlovDomov") {
                                id.add(document.data.get("feedId").toString())
                                controlExistFeedForUD(id.get(position))
                            }

                            Log.d("", "${document.id} => ${document.data}")
                        }

                        if (task.result != null) {
                            holder.view.txt_name.text = nameFeeds.get(position)
                            holder.view.txt_locality.text = localityFeeds.get(position)

                            val forMonth = "za".toRegex().find(priceFeeds.get(position))
                            when (forMonth) {
                                null -> holder.view.txt_price.text =
                                    priceFeeds.get(position) + " Kč/ měsíc"
                                else -> {
                                    val result = priceFeeds.get(position).replace(" za", "/")
                                    holder.view.txt_price.text = result
                                }

                            }

                            val imageURL = holder?.view?.img_pic
                            Picasso.get().load(imageFeeds.get(position)).into(imageURL)

                            if (portalFeeds.get(position) == "UlovDomov") {
                                holder.view.img_logo.setImageResource(R.mipmap.ic_launcher_ulovdomov_logo)
                            } else if (portalFeeds.get(position) == "Sreality") {
                                holder.view.img_logo.setImageResource(R.mipmap.ic_launcher_sreality_logo)
                            }

                            if (found.get(position) == "false") {
                                holder.view.notFound.visibility = View.VISIBLE
                            } else {
                                holder.view.notFound.visibility = View.GONE
                            }

                            if (notes.size != 0) {
                                addNote.text = notes.get(position)
                            }

                            feed.setOnClickListener { View ->
                                if (mAuth.currentUser != null) {
                                    if (feed.isClickable) {
                                        val intent =
                                            Intent(
                                                View.context,
                                                DetailFeedActivity::class.java
                                            )
                                        if (found.get(position) == "true") {
                                            if (portalFeeds.get(position) == "UlovDomov") {
                                                if (id.size != 0) {
                                                    intent.putExtra("id", id.get(position))
                                                    intent.putExtra("flagLike", true)
                                                }


                                            } else if (portalFeeds.get(position) == "Sreality") {
                                                if (hash_id.size != 0) {
                                                    intent.putExtra(
                                                        "hash_id",
                                                        hash_id.get(position)
                                                    )
                                                    intent.putExtra("flagLike", true)
                                                }
                                            }

                                            View.context.startActivity(intent)

                                        } else if (found.get(position) == "false") {

                                            Toast.makeText(
                                                View.context,
                                                "Tento inzerát již neexistuje.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }

                                    }
                                } else if (mAuth.currentUser == null) {
                                    Toast.makeText(
                                        View.context,
                                        "Nejprve se musíte přihlásit.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            deleteImg.setOnClickListener { View ->
                                Toast.makeText(
                                    View.context,
                                    "Nyní můžete zrušit inzerát z oblíbených pomocí kliknutí na srdíčko",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent =
                                    Intent(
                                        View.context,
                                        DetailFeedActivity::class.java
                                    )

                                if (found.get(position) == "true") {
                                    if (portalFeeds.get(position) == "UlovDomov") {
                                        if (id.size != 0) {
                                            intent.putExtra("id", id.get(position))
                                        }
                                    } else if (portalFeeds.get(position) == "Sreality") {
                                        if (hash_id.size != 0) {
                                            intent.putExtra("hash_id", hash_id.get(position))
                                        }
                                    }
                                    intent.putExtra("flagLike", true)
                                    View.context.startActivity(intent)
                                } else if (found.get(position) == "false") {
                                    Toast.makeText(
                                        View.context,
                                        "Tento inzerát již neexistuje.",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }


                            }

                            addNote.setOnClickListener { View ->
                                if (feed.isClickable) {
                                    val intent =
                                        Intent(
                                            View.context,
                                            CreateNoteActivity::class.java
                                        )

                                    if (found.get(position) == "true") {
                                        if (portalFeeds.get(position) == "UlovDomov") {
                                            if (id.size != 0) {
                                                intent.putExtra("id", id.get(position))
                                                if (notes.get(position) != "") {
                                                    intent.putExtra("add_note", notes.get(position))
                                                }
                                            }

                                        } else if (portalFeeds.get(position) == "Sreality") {
                                            if (hash_id.size != 0) {
                                                intent.putExtra("hash_id", hash_id.get(position))
                                                if (notes.get(position) != "") {
                                                    intent.putExtra("add_note", notes.get(position))
                                                }
                                            }
                                        }

                                        View.context.startActivity(intent)

                                    } else if (found.get(position) == "false") {
                                        Toast.makeText(
                                            View.context,
                                            "Tento inzerát již neexistuje.",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }


                                }
                            }

                        }

                    } else {
                        Log.d("", "Not found")
                    }
                }


        } else {
            holder.view.feed.visibility = View.GONE
            holder.view.info.visibility = View.VISIBLE
        }
    }

    fun controlExistFeedForSR(hash_id: String) {
        /*
        * GET method
        */


        val httpUrl_sr = HttpUrl.Builder()
            .scheme("https")
            .host("www.sreality.cz")
            .addPathSegment("api")
            .addPathSegment("cs")
            .addPathSegment("v2")
            .addPathSegment("estates")
            .addPathSegment("${hash_id}")
            .build()
        /* My FULL url -> https://www.sreality.cz/api/cs/v2/estates/839511644*/

        val requestSR = Request.Builder()
            .url(httpUrl_sr)
            .build()

        val gson = GsonBuilder()
            .create()

        Thread(Runnable {
            val response = client.newCall(requestSR).execute()
            val strDesc_response = response?.body?.string()!!
            println("strDesc_response: " + strDesc_response)

            //for not exist feed --> get json: {"logged_in": false}
            if (strDesc_response != "{\"logged_in\": false}") {
                println("hash1: " + hash_id)
            } else if (strDesc_response == "{\"logged_in\": false}") {
                println("hash2: " + hash_id)

                changeFieldOnNotFound(hash_id)
            }
        }).start()
    }

    fun controlExistFeedForUD(id: String) {
        /*
        * GET method
        */

        val httpUrl_ud = HttpUrl.Builder()
            .scheme("https")
            .host("www.ulovdomov.cz")
            .addPathSegment("fe-api")
            .addPathSegment("offer")
            .addPathSegment("$id")
            .build()
        /* My FULL url -> https://www.ulovdomov.cz/fe-api/offer/3585046*/


        val requestUD = Request.Builder()
            .url(httpUrl_ud)
            .build()

        val gson = GsonBuilder().create()

        client.newCall(requestUD).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                Thread(Runnable {
                    val ulovDesc_response = response.body?.string()

                    if (ulovDesc_response == "{\"logged_in\": false}") {
                        changeFieldOnNotFound(id)
                    }
                }).start()
            }
        })
    }

    fun changeFieldOnNotFound(id: String) {

        var user = mAuth.currentUser!!.uid
        val feeds = mCloud.collection("FavouriteFeeds")
            .whereEqualTo("userId", user).whereEqualTo("feedId", id)

        feeds.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    mCloud.collection("FavouriteFeeds").document(document.id)
                        .update("found", false)
                }
            }

        }
    }

}

class FavouriteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
}