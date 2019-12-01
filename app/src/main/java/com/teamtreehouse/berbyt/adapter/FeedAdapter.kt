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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.teamtreehouse.berbyt.R
import com.teamtreehouse.berbyt.activity.DetailFeedActivity
import com.teamtreehouse.berbyt.data.SrealityFeed
import com.teamtreehouse.berbyt.data.UlovDomovFeed
import com.teamtreehouse.berbyt.data.model.FavouriteFeedModel
import kotlinx.android.synthetic.main.lst_view_item.view.*
import kotlinx.android.synthetic.main.lst_view_item.view.img_pic2
import kotlinx.android.synthetic.main.lst_view_item.view.txt_locality2
import kotlinx.android.synthetic.main.lst_view_item.view.txt_name2
import kotlinx.android.synthetic.main.lst_view_item.view.txt_price2
import java.text.NumberFormat
import java.util.*



/*
* Author: Deana Mareková
* Description: FeedAdapter for show feeds in List
* Licence: MIT
* */
class FeedAdapter(
    val srealityFeed: SrealityFeed?,
    val ulovDomovFeed: UlovDomovFeed?
) : RecyclerView.Adapter<CustomViewHolder>() {

    val disposition_desc = mapOf(
        "1" to "garsonka",
        "2" to "1+kk",
        "3" to "1+1",
        "4" to "2+kk",
        "5" to "2+1",
        "6" to "3+kk",
        "7" to "3+1",
        "8" to "4+kk",
        "9" to "4+1",
        "16" to "atypický",
        "29" to "dúm",
        "5_and_more" to "5 a více"
    )

    lateinit var mProgressBar: ProgressDialog
    lateinit var mAuth: FirebaseAuth
    lateinit var mCloud: FirebaseFirestore

    override fun getItemCount(): Int {
        val result = srealityFeed!!._embedded.estates.size
        if (result == 0) {
            return 1  // for view Info
        } else {
            return result
        }
    }

    fun getItemSR(): Int {
        return srealityFeed!!._embedded.estates.size
    }

    fun getItemUD(): Int {
        return ulovDomovFeed!!.offers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        mProgressBar = ProgressDialog(parent.context)
        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()
        val cellForRow = layoutInflater.inflate(R.layout.lst_view_item, parent, false)
        return CustomViewHolder(cellForRow)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var resultSR = getItemSR()
        var resultUD = getItemUD()

        val feedSR = holder.view.findViewById<RelativeLayout>(R.id.feedSR)
        val feedUD = holder.view.findViewById<RelativeLayout>(R.id.feedUD)


        val list = srealityFeed!!._embedded.estates.get(position)
        val list2 = ulovDomovFeed!!.offers.get(position)
        val disp_desc = list2.disposition_id
        val disposition = disposition_desc[disp_desc.toString()]

        var nameSR = list.name
        var localitySR = list.locality
        val price_formatSR =
            NumberFormat.getNumberInstance(Locale.getDefault(Locale.Category.DISPLAY))
                .format(list.price)
        var imageSR = list._links.image_middle2[0].href
        var hash_id = list.hash_id

        var nameUD = "Pronájem bytu " + disposition + " " + list2.acreage + " m2"
        var localityUD =
            list2.village.label + " - " + list2.village_part.label + ", " + list2.street.label
        var price_formatUD =
            NumberFormat.getNumberInstance(Locale.getDefault(Locale.Category.DISPLAY))
                .format(list2.price_rental)
        var imageUD = list2.photos[0].path
        var id = list2.id

        var likeFeeds = arrayListOf<String>()

        feedSR.setOnClickListener { View ->
            if (mAuth.currentUser != null) {
                if (feedSR.isClickable) {
                    mProgressBar.setMessage("Načítaní...")
                    mProgressBar.show()

                    val intent = Intent(View.context, DetailFeedActivity::class.java)

                    if (hash_id != null) {
                        intent.putExtra("hash_id", hash_id.toString())
                        if (likeFeeds.contains(hash_id.toString()) == true) {
                            intent.putExtra("flagLike", true)
                        }
                    }
                    mProgressBar.dismiss()
                    View.context.startActivity(intent)
                }
            } else {
                Toast.makeText(View.context, "Nejprve se musíte přihlásit.", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        feedUD.setOnClickListener { View ->
            if (mAuth.currentUser != null) {
                if (feedUD.isClickable) {
                    mProgressBar.setMessage("Načítaní...")
                    mProgressBar.show()

                    val intent = Intent(View.context, DetailFeedActivity::class.java)

                    if (id != null) {
                        intent.putExtra("id", id.toString())
                        if (likeFeeds.contains(id.toString()) == true) {
                            intent.putExtra("flagLike", true)
                        }
                    }
                    mProgressBar.dismiss()
                    View.context.startActivity(intent)


                }
            } else {
                Toast.makeText(View.context, "Nejprve se musíte přihlásit.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if ((resultUD) == 0 && (resultSR) == 0) {
            holder?.view?.InfoText.visibility = View.VISIBLE
            holder?.view?.feedUD.visibility = View.GONE
            holder?.view?.feedSR.visibility = View.GONE
        } else {
            holder?.view?.InfoText.visibility = View.GONE

            // for Sreality
            if (resultSR != 0) {
                holder?.view?.txt_name.text = nameSR
                holder?.view?.txt_locality.text = localitySR
                holder?.view?.txt_price.text = price_formatSR + " Kč"

                val imageURL = holder?.view?.img_pic
                Picasso.get().load(imageSR).into(imageURL)


                if (mAuth.currentUser != null) {
                    controlIconForFavouriteFeedsSR {

                        if (it.size != 0) {
                            for (id in 0..it.size - 1) {

                                if (hash_id.toString() == it[id]) {
                                    likeFeeds.add(it[id])
                                } else {
                                    println("unlike")
                                }
                            }
                        } else {
                            println("Nebyl nalezen žádný oblíbený inzerát.")
                        }
                    }
                }


            } else {
                holder?.view?.feedSR.visibility = View.GONE
            }


            // for UlovDomov
            if (resultUD != 0) {

                println("position: " + ulovDomovFeed!!.offers.get(position))


                holder?.view?.txt_name2.text = nameUD
                holder?.view?.txt_locality2.text = localityUD
                holder?.view?.txt_price2.text = price_formatUD + " Kč"

                val imageURL2 = holder?.view?.img_pic2
                Picasso.get().load(imageUD).into(imageURL2)

                if (mAuth.currentUser != null) {
                    controlIconForFavouriteFeedsUD {

                        if (it.size != 0) {
                            for (idFav in 0..it.size - 1) {

                                if (id.toString() == it[idFav]) {
                                    likeFeeds.add(it[idFav])

                                } else {
                                    println("unlike")
                                }
                            }
                        } else {
                            println("Nebyl nalezen žádný oblíbený inzerát.")
                        }
                    }
                }

            } else {
                println("Nebyl nalezen žádný inzerát dle Vašich preferencí.")
                holder?.view?.feedUD.visibility = View.GONE
            }
        }


    }

    // control icon for detail feed (UlovDomov)
    fun controlIconForFavouriteFeedsUD(myCallback: (List<String>) -> Unit) {
        var userId = mAuth.currentUser!!.uid
        var array_id = arrayListOf<String>()

        if (userId != null) {

            val batch = mCloud.batch()
            val controlFeed = mCloud.collection("FavouriteFeeds")
                .whereEqualTo("userId", userId).whereEqualTo("portal", "UlovDomov")

            controlFeed.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        array_id.add(document.data.get("feedId").toString())
                        Log.d("", "${document.id} => ${document.data}")
                    }
                    myCallback(array_id)
                }
            }
            batch.commit()
            Log.d("", "Smazán inzerát z oblíbených")
        } else {
            println(" controlFavouriteFeeds failed")
        }
    }

    // control icon for detail feed (Sreality)
    fun controlIconForFavouriteFeedsSR(myCallback: (List<String>) -> Unit) {
        var userId = mAuth.currentUser!!.uid
        var array_id = arrayListOf<String>()

        if (userId != null) {

            val batch = mCloud.batch()
            val controlFeed = mCloud.collection("FavouriteFeeds")
                .whereEqualTo("userId", userId).whereEqualTo("portal", "Sreality")

            controlFeed.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        array_id.add(document.data.get("feedId").toString())
                        Log.d("", "${document.id} => ${document.data}")
                    }
                    myCallback(array_id)
                }
            }
            batch.commit()
            Log.d("", "Smazán inzerát z oblíbených")
        } else {
            println(" controlFavouriteFeeds failed")
        }
    }
}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
}