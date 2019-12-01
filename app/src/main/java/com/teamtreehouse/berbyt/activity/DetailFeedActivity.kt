package com.teamtreehouse.berbyt.activity

import android.annotation.TargetApi
import android.os.Bundle
import com.teamtreehouse.berbyt.R
import okhttp3.*
import java.io.IOException
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_detail_feed.*
import java.text.SimpleDateFormat
import java.text.NumberFormat
import java.util.*
import android.widget.TableLayout
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.*
import com.teamtreehouse.berbyt.data.*
import com.teamtreehouse.berbyt.data.model.FavouriteFeedModel


/*
* Author: Deana Mareková
* Description: DetailFeedActivity for detail feed
* Licence: MIT
* */
class DetailFeedActivity : BaseActivity() {

    val client = OkHttpClient()
    lateinit var mAuth: FirebaseAuth
    lateinit var mCloud: FirebaseFirestore
    var index: Int = 0

    private lateinit var name: TextView
    private lateinit var desc: TextView
    private lateinit var locality: TextView
    private lateinit var price: TextView
    private lateinit var gallery: ImageView
    private lateinit var img_logo: ImageView
    private lateinit var img_unlike: ImageView
    private lateinit var prev: ImageView
    private lateinit var next: ImageView
    private lateinit var address: TextView
    private lateinit var priceDetail: TextView
    private lateinit var monthPrice: TextView
    private lateinit var priceDepos: TextView
    private lateinit var priceCommis: TextView
    private lateinit var acreag: TextView
    private lateinit var furnis: TextView
    private lateinit var availDate: TextView
    private lateinit var publishDate: TextView
    private lateinit var conveniences: TextView
    private lateinit var price_note: TextView

    private lateinit var relTable: RelativeLayout
    private lateinit var tableForSR: TableLayout
    private lateinit var tableForUD: TableLayout
    private lateinit var tbl1: TableRow
    private lateinit var tbl4: TableRow
    private lateinit var tbl5: TableRow
    private lateinit var tbl6: TableRow
    private lateinit var tbl7: TableRow
    private lateinit var tbl8: TableRow
    private lateinit var tbl9: TableRow
    private lateinit var tbl10: TableRow
    private lateinit var tbl11: TableRow
    private lateinit var tbl12: TableRow
    private lateinit var tbl13: TableRow


    private var hashidForLinkSR: String? = ""
    private var hashidForLinkUD: String? = ""
    var flag: Boolean = false
    var flagLike: Boolean = false

    var imageForLike: String = ""

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

    val price_comm = mapOf(
        "0" to "Bez provize",
        "1" to "Ano"
    )

    val conv_desc = mapOf(
        "washing_machine" to "Pračka",
        "dishwasher" to "Myčka",
        "fridge" to "Lednice",
        "cellar" to "Sklep",
        "balcony" to "Balkón"
    )

    val furnish_desc = mapOf(
        "FULL" to "Úplně",
        "MEDIUM" to "Částečně",
        "NONE" to "Vůbec"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_feed)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_detail_feed)

        name = findViewById(R.id.d_txt_name)
        desc = findViewById(R.id.d_txt_desc)
        locality = findViewById(R.id.d_txt_locality)
        price = findViewById(R.id.d_txt_price)
        gallery = findViewById(R.id.d_img_pic)
        img_logo = findViewById(R.id.d_img_logo)
        img_unlike = findViewById(R.id.d_imageUnlike1)
        prev = findViewById(R.id.d_prev)
        next = findViewById(R.id.d_next)
        priceDetail = findViewById(R.id.priceDetail)
        monthPrice = findViewById(R.id.monthPrice)
        priceDepos = findViewById(R.id.priceDepos)
        priceCommis = findViewById(R.id.priceCommis)
        acreag = findViewById(R.id.acreag)
        furnis = findViewById(R.id.furnis)
        availDate = findViewById(R.id.availDate)
        publishDate = findViewById(R.id.publishDate)
        conveniences = findViewById(R.id.conveniences)
        price_note = findViewById(R.id.priceNote)

        relTable = findViewById(R.id.relTable)
        tableForSR = findViewById(R.id.tableForSR)
        tableForUD = findViewById(R.id.tableForUD)
        tbl1 = findViewById(R.id.tbl1)
        tbl4 = findViewById(R.id.tbl4)
        tbl5 = findViewById(R.id.tbl5)
        tbl6 = findViewById(R.id.tbl6)
        tbl7 = findViewById(R.id.tbl7)
        tbl8 = findViewById(R.id.tbl8)
        tbl9 = findViewById(R.id.tbl9)
        tbl10 = findViewById(R.id.tbl10)
        tbl11 = findViewById(R.id.tbl11)
        tbl12 = findViewById(R.id.tbl12)
        tbl13 = findViewById(R.id.tbl13)

        hashidForLinkSR = intent.extras!!.getString("hash_id")
        hashidForLinkUD = intent.extras!!.getString("id")
        flag = intent.extras!!.getBoolean("flagLike")

        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()


        if (hashidForLinkSR != null) {
            fetchDetailFeedForSR(hashidForLinkSR!!)
            controlIconForFavouriteFeeds(hashidForLinkSR!!)
            if (flag == true) {
                img_unlike.setImageResource(R.drawable.ic_action_like)
            }

        } else if (hashidForLinkUD != null) {
            img_logo.setImageResource(R.mipmap.ic_launcher_ulovdomov_logo)
            fetchDetailFeedForUD(hashidForLinkUD!!)
            controlIconForFavouriteFeeds(hashidForLinkUD!!)
            if (flag == true) {
                img_unlike.setImageResource(R.drawable.ic_action_like)
            }

        } else {
            Toast.makeText(
                this@DetailFeedActivity,
                "Tento inzerát již neexistuje.",
                Toast.LENGTH_SHORT
            ).show()
        }


        img_unlike.setOnClickListener { View ->

            if (flag == false) {
                img_unlike.setImageResource(R.drawable.ic_action_like)
                flag = true
                if (hashidForLinkSR != null) {
                    saveToFavoriteFeed(
                        hashidForLinkSR.toString(),
                        "Sreality",
                        d_txt_name.text.toString(),
                        d_txt_locality.text.toString(),
                        d_txt_price.text.toString(),
                        imageForLike,
                        true
                    )

                    Snackbar.make(View, "Přidán inzerát (${d_txt_name.text}) do oblíbených", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()

                } else if (hashidForLinkUD != null) {
                    saveToFavoriteFeed(
                        hashidForLinkUD.toString(),
                        "UlovDomov",
                        d_txt_name.text.toString(),
                        d_txt_locality.text.toString(),
                        d_txt_price.text.toString(),
                        imageForLike,
                        true
                    )

                    Snackbar.make(View, "Přidán inzerát (${d_txt_name.text}) do oblíbených", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
                }
            } else {
                img_unlike.setImageResource(R.drawable.ic_action_unlike)
                flag = false
                if (hashidForLinkSR != null) {
                    deleteToFavoriteFeed(hashidForLinkSR.toString())
                } else if (hashidForLinkUD != null) {
                    deleteToFavoriteFeed(hashidForLinkUD.toString())
                }

                Snackbar.make(View, "Smazán inzerát (${d_txt_name.text}) z oblíbených", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            }
        }

    }

    fun controlIconForFavouriteFeeds(id: String) {
        var userId = mAuth.currentUser!!.uid
        if (userId != null) {

            val batch = mCloud.batch()
            val controlFeed = mCloud.collection("FavouriteFeeds")
                .whereEqualTo("userId", userId).whereEqualTo("feedId", id)

            controlFeed.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    flagLike = true
                }
            }
            batch.commit()
            Log.d("", "Kontrola inzerátu ${id}")
        } else {
            println(" controlFavouriteFeeds failed")
        }
    }

    fun deleteToFavoriteFeed(id: String) {
        var userId = mAuth.currentUser!!.uid
        if (userId != null) {
            println("userID: " + userId)
            println("feedID: " + id)

            val batch = mCloud.batch()
            val deleteFeed = mCloud.collection("FavouriteFeeds")
                .whereEqualTo("userId", userId).whereEqualTo("feedId", id)

            println("result: " + deleteFeed.get())
            deleteFeed.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        mCloud.collection("FavouriteFeeds").document(document.id).delete()
                    }
                }
            }
            batch.commit()
            Log.d("", "Smazán inzerát z oblíbených")
        } else {
            println(" deleteToFavoriteFeed failed")
        }
    }

    fun saveToFavoriteFeed(
        id: String,
        portal: String,
        name: String,
        locality: String,
        price: String,
        image: String,
        found: Boolean
    ) {
        var userId = mAuth.currentUser!!.uid
        if (userId != null) {
            println("userID: " + userId)

            val saveFeed = FavouriteFeedModel(userId, id, portal, name, locality, price, image, found,"")

            mCloud.collection("FavouriteFeeds").document()
                .set(saveFeed)
                .addOnSuccessListener { document ->
                    Log.d("", "Uložen inzerát do oblíbených: ${saveFeed}")
                }
        } else {
            println(" saveToFavoriteFeed failed")
        }
    }

    private fun fetchDetailFeedForSR(hash_id: String) {
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
            .addPathSegment("$hash_id")
            .build()
        /* My FULL url -> https://www.sreality.cz/api/cs/v2/estates/839511644*/

        val requestSR = Request.Builder()
            .url(httpUrl_sr)
            .build()

        val gson = GsonBuilder()
            .create()

        client.newCall(requestSR).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                Thread(Runnable {
                    val strDesc_response = response.body?.string()
                    println(strDesc_response)

                    val detailFeed = gson.fromJson(strDesc_response, SrealityDetailFeed::class.java)

                    // try to touch View of UI thread
                    this@DetailFeedActivity.runOnUiThread(java.lang.Runnable {
                        name.text = detailFeed.name.value.toString()
                        desc.text = detailFeed.text.value.toString()
                        locality.text = detailFeed.locality.value.toString()


                        if (detailFeed.price_czk.value != null) {
                            price.text =
                                detailFeed.price_czk.value.toString() + " Kč " + detailFeed.price_czk.unit.toString()
                        } else {
                            price.setText("Není uvedeno")
                        }


                        val imageURL2 = gallery
                        var i = detailFeed._embedded.images.size

                        for (id in 0..i - 1) {
                            Picasso.get()
                                .load(detailFeed._embedded.images[id]._links.gallery.href)
                                .into(imageURL2)

                            prev.setOnClickListener {
                                index = if (index - 1 >= 0) index - 1 else 1
                                Picasso.get()
                                    .load(detailFeed._embedded.images[index]._links.gallery.href)
                                    .into(imageURL2)
                            }

                            next.setOnClickListener {
                                index = if (index + 1 < i) index + 1 else 0
                                Picasso.get()
                                    .load(detailFeed._embedded.images[index]._links.gallery.href)
                                    .into(imageURL2)
                            }
                        }

                        imageForLike = detailFeed._embedded.images[0]._links.gallery.href

                        tableForUD.visibility = View.GONE

                    })
                }).start()

            }
        })
    }

    private fun fetchDetailFeedForUD(id: String) {
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

                    val detailFeed =
                        gson.fromJson(ulovDesc_response, UlovDomovDetailFeed::class.java)

                    val disp_desc = detailFeed.disposition_id
                    val disposition = disposition_desc[disp_desc.toString()]

                    // try to touch View of UI thread
                    this@DetailFeedActivity.runOnUiThread(java.lang.Runnable {
                        name.text =
                            "Pronájem bytu " + disposition + " " + detailFeed.acreage.toString() + "m2"
                        desc.text = detailFeed.description.toString()
                        locality.text =
                            detailFeed.village.label + " - " + detailFeed.village_part.label + ", " + detailFeed.street.label


                        if (detailFeed.price_rental != null) {
                            val price_format =
                                NumberFormat.getNumberInstance(Locale.getDefault(Locale.Category.DISPLAY))
                                    .format(detailFeed.price_rental)
                            price.text = price_format + " Kč za měsíc"
                        } else {
                            price.setText("Není uvedeno")
                        }



                        val imageURL2 = gallery
                        var i = detailFeed.photos.size

                        for (id in 0..i - 1) {
                            Picasso.get().load(detailFeed.photos[id].path)
                                .into(imageURL2)

                            prev.setOnClickListener {
                                index = if (index - 1 >= 0) index - 1 else 1
                                Picasso.get()
                                    .load(detailFeed.photos[index].path)
                                    .into(imageURL2)
                            }

                            next.setOnClickListener {
                                index = if (index + 1 < i) index + 1 else 0
                                Picasso.get()
                                    .load(detailFeed.photos[index].path)
                                    .into(imageURL2)
                            }
                        }

                        imageForLike = detailFeed.photos[0].path


                        if (detailFeed.price_rental != null) {
                            val price_format =
                                NumberFormat.getNumberInstance(Locale.getDefault(Locale.Category.DISPLAY))
                                    .format(detailFeed.price_rental)
                            priceDetail.text = price_format + " Kč/měsíc"
                        } else {
                            priceDetail.setText("Není uvedeno")
                            tbl4.visibility = View.GONE
                        }

                        if (detailFeed.price_note != "") {
                            price_note.text = detailFeed.price_note
                        } else {
                            price_note.text = "Není uvedeno"
                            tbl13.visibility = View.GONE
                        }

                        if (detailFeed.price_monthly_fee != null) {
                            val price_format =
                                NumberFormat.getNumberInstance(Locale.getDefault(Locale.Category.DISPLAY))
                                    .format(detailFeed.price_monthly_fee)
                            monthPrice.text = price_format + " Kč/měsíc"
                        } else {
                            monthPrice.text = "Není uvedeno"
                            tbl5.visibility = View.GONE
                        }

                        if (detailFeed.price_deposit != null) {
                            val price_format =
                                NumberFormat.getNumberInstance(Locale.getDefault(Locale.Category.DISPLAY))
                                    .format(detailFeed.price_deposit)
                            priceDepos.text = price_format + " Kč"
                        } else {
                            priceDepos.text = "Není uvedeno"
                            tbl6.visibility = View.GONE
                        }

                        if (detailFeed.price_commission != null) {
                            val priceCom = detailFeed.price_commission
                            val priceComm_result = price_comm[priceCom.toString()]
                            priceCommis.text = priceComm_result
                        } else {
                            priceCommis.text = "Není uvedeno"
                            tbl7.visibility = View.GONE
                        }

                        if (detailFeed.acreage != 0) {
                            acreag.text = detailFeed.acreage.toString() + " m2"
                        } else {
                            acreag.text = "Není uvedeno"
                            tbl8.visibility = View.GONE
                        }

                        var arrayCon = detailFeed.conveniences.size

                        if (arrayCon != 0) {
                            var array = arrayListOf<String?>()
                            for (i in 0..arrayCon - 1) {
                                val con = detailFeed.conveniences[i]
                                val result = conv_desc[con]
                                array.add(result)
                            }
                            var x1 = array.toString().replace("[", "")
                            val conven = x1.replace("]", "")

                            conveniences.text = conven
                        } else {
                            conveniences.text = "Není uvedeno"
                            tbl12.visibility = View.GONE
                        }

                        if (detailFeed.furnishing_id != null) {
                            val furnishing_id = detailFeed.furnishing_id
                            val furnish_result = furnish_desc[furnishing_id]
                            furnis.text = furnish_result
                        } else {
                            furnis.text = "Není uvedeno"
                            tbl9.visibility = View.GONE
                        }

                        if (detailFeed.available_date != null) {
                            availDate.text = detailFeed.available_date
                        } else {
                            availDate.text = "Není uvedeno"
                            tbl10.visibility = View.GONE
                        }

                        if (detailFeed.publish_date != null) {
                            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                            val formatter = SimpleDateFormat("dd.MM.yyyy")
                            val output = formatter.format(parser.parse(detailFeed.publish_date))
                            println(output)
                            publishDate.text = output
                        } else {
                            publishDate.text = "Není uvedeno"
                            tbl11.visibility = View.GONE
                        }
                    })
                }).start()

            }
        })
    }
}
