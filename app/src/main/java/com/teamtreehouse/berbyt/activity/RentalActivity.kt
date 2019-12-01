package com.teamtreehouse.berbyt.activity

import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.teamtreehouse.berbyt.R
import com.teamtreehouse.berbyt.adapter.FeedAdapter
import com.teamtreehouse.berbyt.data.SrealityFeed
import com.teamtreehouse.berbyt.data.UlovDomovFeed
import kotlinx.android.synthetic.main.content_rental.*
import kotlinx.coroutines.*
import okhttp3.*
import android.location.Address
import android.location.Geocoder
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamtreehouse.berbyt.data.model.CacheFeedsModel
import java.util.*

/*
* Author: Deana Mareková
* Description: RentalActivity for searching real
* Licence: MIT
* */
class RentalActivity : BaseActivity() {

    val client = OkHttpClient()
    lateinit var getInfo: TextView
    private lateinit var mAuth : FirebaseAuth
    private lateinit var  mCloud : FirebaseFirestore
    var  userId : String = ""

    // for ID locality
    var localityForSr = mapOf(
        "1" to "Jihočeský",
        "2" to "Plzeňský",
        "3" to "Karlovarský",
        "4" to "Ústecký",
        "5" to "Liberecký",
        "6" to "Královéhradecký",
        "7" to "Pardubický",
        "8" to "Olomoucký",
        "9" to "Zlínský",
        "10" to "Praha",
        "11" to "Středočeský",
        "12" to "Moravskoslezský",
        "13" to "Vysočina",
        "14" to "Jihomoravský",
        "5001" to "Praha 1",
        "5002" to "Praha 2",
        "5003" to "Praha 3",
        "5004" to "Praha 4",
        "5005" to "Praha 5",
        "5006" to "Praha 6",
        "5007" to "Praha 7",
        "5008" to "Praha 8",
        "5009" to "Praha 9",
        "5010" to "Praha 10",
        "1" to "Ćeské Budějovice",
        "2" to "Český Krumlov",
        "3" to "Jindřichův Hradec",
        "4" to "Písek",
        "5" to "Prachatice",
        "6" to "Strakonice",
        "7" to "Tábor",
        "71" to "Blansko",
        "72" to "Břeclav",
        "73" to "Brno-město",
        "74" to "Brno-venkov",
        "75" to "Hodonín",
        "76" to "Vyškov",
        "77" to "Znojmo",
        "9" to "Cheb",
        "10" to "Karlovy Vary",
        "16" to "Sokolov",
        "20" to "Chomutov",
        "19" to "Děčín",
        "23" to "Litoměřice",
        "24" to "Louny",
        "25" to "Most",
        "26" to "Teplice",
        "27" to "Ústí nad Labem",
        "18" to "Česká Líba",
        "21" to "Jablonec nad Nisou",
        "22" to "Liberec",
        "34" to "Semily",
        "28" to "Hradec Králové",
        "30" to "Jičín",
        "31" to "Náchod",
        "33" to "Rychnov nad Kněžnou",
        "36" to "Trutnov",
        "29" to "Chrudim",
        "32" to "Pardubice",
        "35" to "Svitavy",
        "37" to "Ústí nad Orlicí",
        "40" to "Jeseník",
        "42" to "Olomouc",
        "43" to "Přerov",
        "44" to "Prostějov",
        "46" to "Šumperk",
        "38" to "Kroměříž",
        "39" to "Uherské Hradiště",
        "41" to "Vsetín",
        "45" to "Zlín",
        "48" to "Benešov",
        "49" to "Beroun",
        "50" to "Kladno",
        "51" to "Kolín",
        "52" to "Kutná Hora",
        "53" to "Mladá Boleslav",
        "54" to "Mělník",
        "55" to "Nymburk",
        "56" to "Praha-východ",
        "57" to "Praha-západ",
        "58" to "Příbram",
        "59" to "Rakovník",
        "60" to "Bruntál",
        "61" to "Frýdek-Místek",
        "62" to "Karviná",
        "63" to "Nový Jičín",
        "64" to "Opava",
        "65" to "Ostrava-město",
        "66" to "Havlíčkův Brod",
        "67" to "Jihlava",
        "68" to "Pelhřimov",
        "69" to "Třebíč",
        "70" to "Žďár nad Sázavou",
        "8" to "Domažlice",
        "11" to "Klatovy",
        "12" to "Plzeň-město",
        "13" to "Plzeň-jih",
        "14" to "Plzeň-sever",
        "15" to "Rokycany",
        "17" to "Tachov"
    )

    val mapRegion = mapOf(
        "1" to "Jihočeský",
        "2" to "Plzeňský",
        "3" to "Karlovarský",
        "4" to "Ústecký",
        "5" to "Liberecký",
        "6" to "Královéhradecký",
        "7" to "Pardubický",
        "8" to "Olomoucký",
        "9" to "Zlínský",
        "10" to "Praha",
        "11" to "Středočeský",
        "12" to "Moravskoslezský",
        "13" to "Vysočina",
        "14" to "Jihomoravský"
    )

    /* My FULL url -> "https://www.ulovdomov.cz/fe-api/find"*/
    val httpUrl_ud = HttpUrl.Builder()
        .scheme("https")
        .host("www.ulovdomov.cz")
        .addPathSegment("fe-api")
        .addPathSegment("find")
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rental)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_rental)

        initBottomNav()

        getInfo = findViewById(R.id.getInfo)

        getInfo.setText(getInfo())

        mAuth = FirebaseAuth.getInstance()
        mCloud = FirebaseFirestore.getInstance()
        if (mAuth.currentUser != null) {
            userId = mAuth.currentUser!!.uid
        }

        runBlocking<Unit> {
            if (userId != "") {
                saveCacheFeeds()
            }
            runInParallel()
        }

        recyclerView_rental.layoutManager = LinearLayoutManager(this)

    }

    private var job: Job = Job()
    private var scope = CoroutineScope(Dispatchers.Main + job)

    fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }

    fun runInParallel() {
        scope.launch {
            // launch a coroutine
            // runs in parallel
            val deferredList = listOf(
                scope.asyncIO {
                    val srealityFeed = getBodyDataSR()
                    val ulovDomovFeed = getBodyDataUD()

                    runOnUiThread {
                        recyclerView_rental.adapter =
                            FeedAdapter(srealityFeed, ulovDomovFeed)
                    }

                })
        }
    }

    @WorkerThread
    fun getLatFromAddress(strAddress: String): Double {

        var address: List<Address>? = null
        val coder = Geocoder(this, Locale.getDefault())

        address = coder.getFromLocationName(strAddress, 5)
        if (address == null) {
            println("Nenasla sa adresa")
            return null!!
        }
        val location: Address? = address.get(0)
        location!!.getLatitude().toDouble()
        location!!.getLongitude().toDouble()

        val lat = location!!.getLatitude().toDouble()

        return lat
    }

    @WorkerThread
    fun getLongFromAddress(strAddress: String): Double {

        var address: List<Address>? = null
        val coder = Geocoder(this, Locale.getDefault())

        address = coder.getFromLocationName(strAddress, 5)
        if (address == null) {
            println("Nenasla sa adresa")
            return null!!
        }
        val location: Address? = address.get(0)
        location!!.getLatitude().toDouble()
        location!!.getLongitude().toDouble()

        val long = location!!.getLongitude().toDouble()

        return long
    }

    fun getInfo(): String {
        var info = intent.extras!!.getString("info")

        return info!!
    }

    fun getBodyDataSR(): SrealityFeed {

        var priceFrom = intent.extras!!.getString("priceFrom")
        var priceTo = intent.extras!!.getString("priceTo")
        var acreageFrom = intent.extras!!.getString("acreageFrom")
        var acreageTo = intent.extras!!.getString("acreageTo")
        var estateAge = intent.extras!!.getString("estateAge")
        var checkedLocality = intent.extras!!.getString("checkedLocality")
        var checkedRegion = intent.extras!!.getString("locality_region_id")
        var somethingMore = getFixArrayForSR("somethingMore")
        var sort = intent.extras!!.getString("sort")


        var locality_district_id = localityForSr.filterValues { it == checkedLocality }.keys
        val locality = locality_district_id.toString().replace("[", "").replace("]", "")


        val result = getFixArrayForSR("arraySR")

        var priceFromTo = "$priceFrom|$priceTo"

        val acreageFromTo = "$acreageFrom|$acreageTo"

        val furnished = getFixArrayForSR("furnished")


        val httpUrl_sr = HttpUrl.Builder()
            .scheme("https")
            .host("www.sreality.cz")
            .addPathSegment("api")
            .addPathSegment("cs")
            .addPathSegment("v2")
            .addPathSegment("estates")
            .addQueryParameter("category_main_cb", "1")
            .addQueryParameter("category_sub_cb", result)
            .addQueryParameter("category_type_cb", "2")
            // 2000%7C20000
            .addQueryParameter("czk_price_summary_order2", "$priceFromTo")
            .addQueryParameter("locality_country_id", "112") // for Czech republic
            .addQueryParameter("locality_region_id", "$checkedRegion")
            .addQueryParameter("locality_district_id", "$locality")
            .addQueryParameter("usable_area", "$acreageFromTo")
            .addQueryParameter("furnished", "$furnished")
            .addQueryParameter("estateAge", "$estateAge")
            .addQueryParameter("something_more1", "$somethingMore")
            .addQueryParameter("sort", "$sort")
            .build()
        /* My FULL url -> "https://www.sreality.cz/api/cs/v2/estates?category_main_cb=1&" +
                "category_sub_cb=2%7C7" +
                "&category_type_cb=2&" +
                "czk_price_summary_order2=0%7C10000&" +
                "locality_district_id=5003%7C5001%7C5002%7C5004&" +
                "locality_region_id=10"*/

        /*
        * GET method
        */
        val requestSr = Request.Builder()
            .url(httpUrl_sr)
            .build()

        val gson = GsonBuilder().create()

        val response = client.newCall(requestSr).execute()
        val resSr = response?.body?.string()!!
        println(resSr)
        val srealityFeed = gson.fromJson(resSr, SrealityFeed::class.java)

        return srealityFeed

    }

    fun saveCacheFeeds() {

        Thread(Runnable {
            // for Sreality
            var priceFrom = intent.extras!!.getString("priceFrom")
            var priceTo = intent.extras!!.getString("priceTo")
            var acreageFrom = intent.extras!!.getString("acreageFrom")
            var acreageTo = intent.extras!!.getString("acreageTo")
            var estateAge = intent.extras!!.getString("estateAge")
            var checkedLocality = intent.extras!!.getString("checkedLocality")
            var checkedRegion = intent.extras!!.getString("locality_region_id")
            var somethingMore = getFixArrayForSR("somethingMore")
            var sort = intent.extras!!.getString("sort")


            var locality_district_id = localityForSr.filterValues { it == checkedLocality }.keys
            val locality = locality_district_id.toString().replace("[", "").replace("]", "")

            val result = getFixArrayForSR("arraySR")

            var priceFromTo = "$priceFrom|$priceTo"

            println(priceFromTo)
            val acreageFromTo = "$acreageFrom|$acreageTo"

            val furnished = getFixArrayForSR("furnished")


            val httpUrl_sr = HttpUrl.Builder()
                .scheme("https")
                .host("www.sreality.cz")
                .addPathSegment("api")
                .addPathSegment("cs")
                .addPathSegment("v2")
                .addPathSegment("estates")
                .addQueryParameter("category_main_cb", "1")
                .addQueryParameter("category_sub_cb", result)
                .addQueryParameter("category_type_cb", "2")
                .addQueryParameter("czk_price_summary_order2", "$priceFromTo")
                .addQueryParameter("locality_country_id", "112") // for Czech republic
                .addQueryParameter("locality_region_id", "$checkedRegion")
                .addQueryParameter("locality_district_id", "$locality")
                .addQueryParameter("usable_area", "$acreageFromTo")
                .addQueryParameter("furnished", "$furnished")
                .addQueryParameter("estateAge", "$estateAge")
                .addQueryParameter("something_more1", "$somethingMore")
                .addQueryParameter("sort", "$sort")
                .build()
            /* My FULL url -> "https://www.sreality.cz/api/cs/v2/estates?category_main_cb=1&" +
                "category_sub_cb=2%7C7" +
                "&category_type_cb=2&" +
                "czk_price_summary_order2=0%7C10000&" +
                "locality_district_id=5003%7C5001%7C5002%7C5004&" +
                "locality_region_id=10"*/

            /*
            * GET method
            */
            val requestSr = Request.Builder()
                .url(httpUrl_sr)
                .build()

            val gson = GsonBuilder().create()

            val response = client.newCall(requestSr).execute()
            val resSr = response?.body?.string()!!
            println(resSr)
            val srealityFeed = gson.fromJson(resSr, SrealityFeed::class.java)

            // for UlovDomov
            var priceFrom1 = intent.extras!!.getString("priceFrom")
            var priceTo1 = intent.extras!!.getString("priceTo")
            var acreageFrom1 = intent.extras!!.getString("acreageFrom")
            var acreageTo1 = intent.extras!!.getString("acreageTo")
            var addedBefore1 = intent.extras!!.getString("addedBefore")

            val result1 = getFixArrayForUD("arrayUD")
            val furnishing1 = getFixArrayForUD("furnishing")
            val checkedLocality1 = getFixArrayForUD("checkedLocality")
            val conveniences1 = getFixArrayForUD("conveniences")
            var sortBy1 = getFixArrayForUD("sortBy")


            val lat = getLatFromAddress(checkedLocality1)
            val long = getLongFromAddress(checkedLocality1)

            val latS = lat - 0.1.toDouble()
            val longS = long + 0.1.toDouble()


            val reqbody2 = RequestBody.create(
                null,
                "{\"query\":\"$checkedLocality1\",\"offer_type_id\":\"1\"," +
                        "\"dispositions\":$result1," +
                        "\"price_from\":\"$priceFrom1\",\"price_to\":\"$priceTo1\"," +
                        "\"acreage_from\":\"$acreageFrom1\",\"acreage_to\":\"$acreageTo1\"," +
                        "\"added_before\":$addedBefore1,\"furnishing\":$furnishing1," +
                        "\"conveniences\":$conveniences1,\"is_price_commision_free\":null," +
                        "\"sort_by\":$sortBy1,\"page\":1,\"limit\":20," +
                        "\"bounds\":{\"north_east\":{" +
                        "\"lng\":$longS,\"lat\":$lat} ," +
                        "\"south_west\":{\"lng\":$long,\"lat\":$latS}}}"
            )

            /*
            * POST method
            */
            val requestUd = Request.Builder()
                .url(httpUrl_ud)
                .header("Content-Type", "application/json")
                .method("POST", reqbody2)
                .build()


            val responseUd = client.newCall(requestUd).execute()
            val resUd = responseUd?.body?.string()!!
            println(resUd)
            val ulovDomovFeed = gson.fromJson(resUd, UlovDomovFeed::class.java)

            // save data for map
            val cacheFeed = mCloud.collection("CacheFeeds").document(userId)
            var saveData =
                CacheFeedsModel(resUd, resSr, userId)
            cacheFeed.set(saveData)

        }).start()

    }

    fun getFixArrayForSR(array: String): String {

        var arraySR = intent.extras!!.getString(array)

        when (arraySR) {
            "[]" -> {
                println("Nenašel se inzerát pro Sreality")
            }
            else -> {
                val x = arraySR!!.replace(",", "|")
                val x1 = x.replace(" ", "")
                val x2 = x1.replace("[", "")
                arraySR = x2.replace("]", "")
            }
        }

        return arraySR
    }

    fun getFixArrayForUD(array: String): String {
        var arrayUD = intent.extras!!.getString(array)

        val comma = "[,]".toRegex().find(arrayUD.toString())

        when (arrayUD) {
            "[]" -> {
                println("Nenašel se inzerát pro UlovDomov")
            }
            else -> {
                when (comma) {
                    null -> {
                        val x = arrayUD!!.replace("[", "")
                        arrayUD = x.replace("]", "")
                    }

                }
            }

        }

        when (array == "checkedLocality") {

            true -> {
                val chL = arrayUD!!.replace("[", "")
                arrayUD = chL.replace("]", "")
            }

        }

        return arrayUD.toString()
    }

    fun getBodyDataUD(): UlovDomovFeed {

        var priceFrom = intent.extras!!.getString("priceFrom")
        var priceTo = intent.extras!!.getString("priceTo")
        var acreageFrom = intent.extras!!.getString("acreageFrom")
        var acreageTo = intent.extras!!.getString("acreageTo")
        var addedBefore = intent.extras!!.getString("addedBefore")

        val result = getFixArrayForUD("arrayUD")
        val furnishing = getFixArrayForUD("furnishing")
        val checkedLocality = getFixArrayForUD("checkedLocality")
        val conveniences = getFixArrayForUD("conveniences")
        var sortBy = getFixArrayForUD("sortBy")


        val lat = getLatFromAddress(checkedLocality)
        val long = getLongFromAddress(checkedLocality)

        val latS = lat - 0.1.toDouble()
        val longS = long + 0.1.toDouble()


        val reqbody2 = RequestBody.create(
            null,
            "{\"query\":\"$checkedLocality\",\"offer_type_id\":\"1\"," +
                    "\"dispositions\":$result," +
                    "\"price_from\":\"$priceFrom\",\"price_to\":\"$priceTo\"," +
                    "\"acreage_from\":\"$acreageFrom\",\"acreage_to\":\"$acreageTo\"," +
                    "\"added_before\":$addedBefore,\"furnishing\":$furnishing," +
                    "\"conveniences\":$conveniences,\"is_price_commision_free\":null," +
                    "\"sort_by\":$sortBy,\"page\":1,\"limit\":20," +
                    "\"bounds\":{\"north_east\":{" +
                    "\"lng\":$longS,\"lat\":$lat} ," +
                    "\"south_west\":{\"lng\":$long,\"lat\":$latS}}}"
        )

        /*
        * POST method
        */
        val requestUd = Request.Builder()
            .url(httpUrl_ud)
            .header("Content-Type", "application/json")
            .method("POST", reqbody2)
            .build()


        val gson = GsonBuilder().create()

        val responseUd = client.newCall(requestUd).execute()
        val resUd = responseUd?.body?.string()!!
        println(resUd)
        val ulovDomovFeed = gson.fromJson(resUd, UlovDomovFeed::class.java)

        return ulovDomovFeed
    }
}




