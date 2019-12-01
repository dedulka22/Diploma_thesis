package com.teamtreehouse.berbyt.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import com.teamtreehouse.berbyt.R
import okhttp3.*
import android.widget.TextView
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar


/*
* Author: Deana Mareková
* Description: SearchActivity for search form
* Licence: MIT
* */
class SearchActivity : BaseActivity() {


    lateinit var btn_search: Button

    lateinit var gar: CheckBox
    lateinit var onekk: CheckBox
    lateinit var oneone: CheckBox
    lateinit var twokk: CheckBox
    lateinit var twoone: CheckBox
    lateinit var threekk: CheckBox
    lateinit var threeone: CheckBox
    lateinit var fourkk: CheckBox
    lateinit var fourone: CheckBox
    lateinit var fiveandmore: CheckBox
    lateinit var house: CheckBox
    lateinit var atypic: CheckBox
    lateinit var priceFrom: EditText
    lateinit var priceTo: EditText
    lateinit var tvMin: TextView
    lateinit var tvMax: TextView
    lateinit var full: CheckBox
    lateinit var medium: CheckBox
    lateinit var none: CheckBox
    lateinit var radioAll: RadioButton
    lateinit var radioDay: RadioButton
    lateinit var radioSevenDays: RadioButton
    lateinit var radioMonth: RadioButton
    lateinit var locality: TextView

    lateinit var balcony: CheckBox
    lateinit var terase: CheckBox
    lateinit var loggia: CheckBox
    lateinit var cellar: CheckBox
    lateinit var parking: CheckBox
    lateinit var garage: CheckBox
    lateinit var lift: CheckBox
    lateinit var wheelch: CheckBox
    lateinit var washingM: CheckBox
    lateinit var dishwasher: CheckBox
    lateinit var fridge: CheckBox

    lateinit var radioPrice: RadioButton
    lateinit var radioDate: RadioButton


    //initializer
    var garId: String = ""
    var onekkId: String = ""
    var oneoneId: String = ""
    var twokkId: String = ""
    var twooneId: String = ""
    var threekkId: String = ""
    var threeoneId: String = ""
    var fourkkId: String = ""
    var fouroneId: String = ""
    var fivekkId: String = ""
    var fiveoneId: String = ""
    var fiveAndMore: String = "" // for UlovDomov
    var sixkkId: String = ""
    var atypicId: String = ""
    var houseId: String = ""

    var fullId = ""
    var mediumId = ""
    var noneId = ""

    var radioAllId = ""
    var radioDayId = ""
    var radioSevenDaysId = ""
    var radioMonthId = ""

    var balconyId = ""
    var teraseId = ""
    var loggiaId = ""
    var cellarId = ""
    var parkingId = ""
    var garageId = ""
    var liftId = ""
    var wheelchId = ""
    var washingMId = ""
    var dishwasherId = ""
    var fridgeId = ""


    val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_search)

        btn_search = findViewById(R.id.btn_search)

        gar = findViewById(R.id.gar)
        onekk = findViewById(R.id.onekk)
        oneone = findViewById(R.id.oneone)
        twokk = findViewById(R.id.twokk)
        twoone = findViewById(R.id.twoone)
        threekk = findViewById(R.id.threekk)
        threeone = findViewById(R.id.threeone)
        fourkk = findViewById(R.id.fourkk)
        fourone = findViewById(R.id.fourone)
        fiveandmore = findViewById(R.id.fiveandmore)
        atypic = findViewById(R.id.atypic)
        house = findViewById(R.id.house)

        priceFrom = findViewById(R.id.priceFrom)
        priceTo = findViewById(R.id.priceTo)

        full = findViewById(R.id.full)
        medium = findViewById(R.id.medium)
        none = findViewById(R.id.none)

        radioAll = findViewById(R.id.radioAll)
        radioDay = findViewById(R.id.radioDay)
        radioSevenDays = findViewById(R.id.radioSevenDays)
        radioMonth = findViewById(R.id.radioMonth)

        balcony = findViewById(R.id.balcony)
        terase = findViewById(R.id.terase)
        loggia = findViewById(R.id.loggia)
        cellar = findViewById(R.id.cellar)
        parking = findViewById(R.id.parking)
        garage = findViewById(R.id.garage)
        lift = findViewById(R.id.lift)
        wheelch = findViewById(R.id.wheelchairAccessible)
        washingM = findViewById(R.id.washingMachine)
        dishwasher = findViewById(R.id.dishwasher)
        fridge = findViewById(R.id.fridge)

        radioPrice = findViewById(R.id.radioPrice)
        radioDate = findViewById(R.id.radioDate)

        mProgressBar = ProgressDialog(this)


        // get seekbar from view
        val rangeSeekbar = findViewById(R.id.rangeSeekbar1) as CrystalRangeSeekbar

        // get min and max text view
        tvMin = findViewById(R.id.textMin1)
        tvMax = findViewById(R.id.textMax1)

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            tvMin.text = minValue.toString()
            tvMax.text = maxValue.toString()
        }


        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            Log.d(
                "CRS=>",
                "$minValue : $maxValue"
            )
        }

        locality = findViewById(R.id.locality)


        var checkedLocality: String
        var checkedRegion: String = ""
        val bundle = intent.extras
        if (bundle != null) {
            checkedLocality = bundle.getString("checkedLocality", "Vybrat lokalitu")
            checkedRegion = bundle.getString("locality_region_id").toString()
            println(checkedLocality)
            locality.setText(checkedLocality)
        }


        btn_search.setOnClickListener {


            var checkedLocality: String? = ""
            if (locality.text == "Vybrat lokalitu") {
                Toast.makeText(
                    this@SearchActivity,
                    "Prosím vyberte si lokalitu.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                mProgressBar.setMessage("Vyhledávam Váš požadavek...")
                mProgressBar.show()


                checkedLocality = intent.extras!!.getString("checkedLocality")
                val arrayUD = getSearchFromUD()
                val arraySR = getSearchFromSR()
                val priceF = getPriceFrom()
                val priceT = getPriceTo()
                val acreageFrom = getTvMin()
                val acreageTo = getTvMax()
                val furnishing = getFurnishingUD()
                val furnished = getFurnishingSR()
                val estateAge = getEstateAgeForSR()
                val addedBefore = getAddedBeforeForUD()
                val somethingMore = getSomethingMoreSR()
                val conveniences = getConveniencesUD()
                val sort = getSortSR()
                val sortBy = getSortByUD()


                val info = getInfo()

                val intent = Intent(this@SearchActivity, RentalActivity::class.java)
                intent.putExtra("arrayUD", arrayUD)
                intent.putExtra("arraySR", arraySR)
                intent.putExtra("priceFrom", priceF)
                intent.putExtra("priceTo", priceT)
                intent.putExtra("acreageFrom", acreageFrom)
                intent.putExtra("acreageTo", acreageTo)
                intent.putExtra("furnishing", furnishing)
                intent.putExtra("furnished", furnished)
                intent.putExtra("estateAge", estateAge)
                intent.putExtra("addedBefore", addedBefore)
                intent.putExtra("checkedLocality", checkedLocality)
                intent.putExtra("info", info)
                intent.putExtra("somethingMore", somethingMore)
                intent.putExtra("conveniences", conveniences)
                intent.putExtra("sort", sort)
                intent.putExtra("sortBy", sortBy)
                intent.putExtra("locality_region_id", checkedRegion)

                startActivity(intent)
                mProgressBar.dismiss()
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        mProgressBar.dismiss()
    }

    private fun getSomethingMoreSR(): String {

        if (balcony.isChecked) {
            balconyId = "3090"
        } else {
            balconyId = "0"
        }

        if (terase.isChecked) {
            teraseId = "3110"
        } else {
            teraseId = "0"
        }

        if (loggia.isChecked) {
            loggiaId = "3100"
        } else {
            loggiaId = "0"
        }

        if (cellar.isChecked) {
            cellarId = "3120"
        } else {
            cellarId = "0"
        }

        if (parking.isChecked) {
            parkingId = "3140"
        } else {
            parkingId = "0"
        }

        if (garage.isChecked) {
            garageId = "3150"
        } else {
            garageId = "0"
        }

        if (lift.isChecked) {
            liftId = "3310"
        } else {
            liftId = "0"
        }

        if (wheelch.isChecked) {
            wheelchId = "1820"
        } else {
            wheelchId = "0"
        }

        val arraySomethingSR = arrayListOf<String>(
            balconyId,
            teraseId,
            loggiaId,
            cellarId,
            parkingId,
            garageId,
            liftId,
            wheelchId
        )

        arraySomethingSR.removeAll(listOf("0"))
        println("arraySomethingSR  $arraySomethingSR")

        return arraySomethingSR.toString()
    }

    private fun getConveniencesUD(): String {

        if (washingM.isChecked) {
            washingMId = "\"washing_machine\""
        } else {
            washingMId = "0"
        }

        if (dishwasher.isChecked) {
            dishwasherId = "\"dishwasher\""
        } else {
            dishwasherId = "0"
        }

        if (fridge.isChecked) {
            fridgeId = "\"fridge\""
        } else {
            fridgeId = "0"
        }

        if (cellar.isChecked) {
            cellarId = "\"cellar\""
        } else {
            cellarId = "0"
        }

        if (balcony.isChecked) {
            balconyId = "\"balcony\""
        } else {
            balconyId = "0"
        }

        val arrayConveniencesUD = arrayListOf<String>(
            parkingId,
            teraseId,
            loggiaId,
            cellarId,
            balconyId
        )

        arrayConveniencesUD.removeAll(listOf("0"))
        println("arrayConveniencesUD  $arrayConveniencesUD")

        return arrayConveniencesUD.toString()
    }


    fun onClickLocality(view: View) {
        var click = view as TextView

        click.setOnClickListener {
            startActivity(
                Intent(
                    this@SearchActivity,
                    LocalityActivity::class.java
                )
            )
        }
    }

    private fun getSortSR(): String {

        var arraySortSR = ""

        if (radioPrice.isChecked) {
            arraySortSR = "1"
        }

        if (radioDate.isChecked) {
            arraySortSR = "0"
        }

        println("filter pre SR: $arraySortSR")

        return arraySortSR

    }


    private fun getSortByUD(): String {

        var arraySortByUD = ""

        if (radioPrice.isChecked) {
            arraySortByUD = "\"price:asc\""
        }

        if (radioDate.isChecked) {
            arraySortByUD = "\"date:desc\""
        }

        println("filter pre UD: $arraySortByUD")

        return arraySortByUD
    }

    private fun getEstateAgeForSR(): String {

        var arrayEstateAgeSR = ""

        if (radioAll.isChecked) {
            arrayEstateAgeSR = ""
        }

        if (radioDay.isChecked) {
            arrayEstateAgeSR = "2"
        }

        if (radioSevenDays.isChecked) {
            arrayEstateAgeSR = "8"
        }

        if (radioMonth.isChecked) {
            arrayEstateAgeSR = "31"
        }

        println("stari inzeraty pre SR: $arrayEstateAgeSR")

        return arrayEstateAgeSR
    }

    private fun getAddedBeforeForUD(): String {
        var arrayAddedBeforeUD = ""

        if (radioAll.isChecked) {
            arrayAddedBeforeUD = "\"\""
        }

        if (radioDay.isChecked) {
            arrayAddedBeforeUD = "\"1\""
        }

        if (radioSevenDays.isChecked) {
            arrayAddedBeforeUD = "\"7\""
        }

        if (radioMonth.isChecked) {
            arrayAddedBeforeUD = "\"14\""
        }

        println("stari inzeraty pre UD: $arrayAddedBeforeUD")

        return arrayAddedBeforeUD
    }

    private fun getFurnishingUD(): String {
        if (full.isChecked) {
            fullId = "\"FULL\""
        } else {
            fullId = "0"
        }

        if (medium.isChecked) {
            mediumId = "\"MEDIUM\""
        } else {
            mediumId = "0"
        }

        if (none.isChecked) {
            noneId = "\"NONE\""
        } else {
            noneId = "0"
        }

        val arrayFurnishingUD = arrayListOf<String>(
            fullId,
            mediumId,
            noneId
        )

        arrayFurnishingUD.removeAll(listOf("0"))
        println("furnished UD $arrayFurnishingUD")

        return arrayFurnishingUD.toString()
    }

    private fun getFurnishingSR(): String {
        if (full.isChecked) {
            fullId = "1"
        } else {
            fullId = "0"
        }

        if (medium.isChecked) {
            mediumId = "3"
        } else {
            mediumId = "0"
        }

        if (none.isChecked) {
            noneId = "2"
        } else {
            noneId = "0"
        }

        val arrayFurnishingSR = arrayListOf<String>(
            fullId,
            mediumId,
            noneId
        )

        arrayFurnishingSR.removeAll(listOf("0"))
        println("furnishing SR $arrayFurnishingSR")

        return arrayFurnishingSR.toString()
    }

    fun getInfo(): String {

        var locality = intent.extras!!.getString("checkedLocality")

        if (gar.isChecked) {
            this.garId = "Garsonka"
        } else {
            this.garId = 0.toString()
        }

        if (onekk.isChecked) {
            this.onekkId = "1+kk"
        } else {
            this.onekkId = 0.toString()

        }


        if (oneone.isChecked) {
            this.oneoneId = "1+1"

        } else {
            this.oneoneId = 0.toString()

        }


        if (twokk.isChecked) {
            this.twokkId = "2+kk"


        } else {
            this.twokkId = 0.toString()

        }


        if (twoone.isChecked) {
            this.twooneId = "2+1"

        } else {
            this.twooneId = 0.toString()

        }

        if (threekk.isChecked) {
            this.threekkId = "3+kk"

        } else {
            this.threekkId = 0.toString()

        }


        if (threeone.isChecked) {
            this.threeoneId = "3+1"

        } else {
            this.threeoneId = 0.toString()

        }


        if (fourkk.isChecked) {
            this.fourkkId = "4+kk"

        } else {
            this.fourkkId = 0.toString()

        }


        if (fourone.isChecked) {
            this.fouroneId = "4+1"

        } else {
            this.fouroneId = 0.toString()

        }

        if (fiveandmore.isChecked) {
            this.fivekkId = "5 a více"

        } else {
            this.fivekkId = 0.toString()

        }

        if (atypic.isChecked) {
            this.atypicId = "Atypický"

        } else {
            this.atypicId = 0.toString()

        }

        if (house.isChecked) {
            this.houseId = "Dům"

        } else {
            this.houseId = 0.toString()

        }
        val checkedDispos = arrayListOf<String>(
            garId,
            onekkId,
            oneoneId,
            twokkId,
            twooneId,
            threekkId,
            threeoneId,
            fourkkId,
            fouroneId,
            fivekkId,
            atypicId,
            houseId
        )

        checkedDispos.removeAll(listOf("0"))

        var info = locality + " - Pronájem: " + checkedDispos.toString()

        when (info != null) {

            true -> {
                val x = info.replace("[", "")
                info = x.replace("]", "")
            }

        }

        return info
    }

    private fun getTvMin(): String {
        return tvMin.text.toString()
    }

    private fun getTvMax(): String {
        return tvMax.text.toString()
    }

    private fun getPriceFrom(): String {
        var priceFrom = this.priceFrom.text.toString()
        var priceTo = this.priceTo.text.toString()

        if (priceFrom.isEmpty()) {
            priceFrom = "0"
        }

        if (priceTo.isEmpty() && priceFrom.isEmpty()) {
            priceFrom = ""
        }

        return priceFrom
    }

    private fun getPriceTo(): String {
        var priceFrom = this.priceFrom.text.toString()
        var priceTo = this.priceTo.text.toString()

        if (priceTo.isEmpty() && priceFrom.isEmpty()) {
            priceTo = ""
        }

        return priceTo
    }

    private fun getSearchFromSR(): String {

        if (onekk.isChecked) {
            this.onekkId = 2.toString()
        } else {
            this.onekkId = 0.toString()

        }


        if (oneone.isChecked) {
            this.oneoneId = 3.toString()

        } else {
            this.oneoneId = 0.toString()

        }


        if (twokk.isChecked) {
            this.twokkId = 4.toString()


        } else {
            this.twokkId = 0.toString()

        }


        if (twoone.isChecked) {
            this.twooneId = 5.toString()

        } else {
            this.twooneId = 0.toString()

        }

        if (threekk.isChecked) {
            this.threekkId = 6.toString()

        } else {
            this.threekkId = 0.toString()

        }


        if (threeone.isChecked) {
            this.threeoneId = 7.toString()

        } else {
            this.threeoneId = 0.toString()

        }


        if (fourkk.isChecked) {
            this.fourkkId = 8.toString()

        } else {
            this.fourkkId = 0.toString()

        }


        if (fourone.isChecked) {
            this.fouroneId = 9.toString()

        } else {
            this.fouroneId = 0.toString()

        }

        if (fiveandmore.isChecked) {
            this.fivekkId = 10.toString()
            this.fiveoneId = 11.toString()
            this.sixkkId = 12.toString()

        } else {
            this.fivekkId = 0.toString()
            this.fiveoneId = 0.toString()
            this.sixkkId = 0.toString()

        }

        if (atypic.isChecked) {
            this.atypicId = 16.toString()

        } else {
            this.atypicId = 0.toString()

        }
        val arraySR = arrayListOf<String>(
            onekkId.toString(),
            oneoneId.toString(),
            twokkId.toString(),
            twooneId.toString(),
            threekkId.toString(),
            threeoneId.toString(),
            fourkkId.toString(),
            fouroneId.toString(),
            atypicId.toString(),
            fivekkId,
            fiveoneId,
            sixkkId
        )

        arraySR.removeAll(listOf("0"))

        println(arraySR)
        return arraySR.toString()
    }

    private fun getSearchFromUD(): String {
        if (gar.isChecked) {
            this.garId = 1.toString()
            //println(gar.tag.toString())
            //message(gar.text.toString() + if (gar.isChecked) " Checked " else " UnChecked ")
        } else {
            this.garId = 0.toString()
            //message(gar.text.toString() + if (gar.isChecked) " Checked " else " UnChecked ")
        }

        if (onekk.isChecked) {
            this.onekkId = 2.toString()
        } else {
            this.onekkId = 0.toString()

        }


        if (oneone.isChecked) {
            this.oneoneId = 3.toString()

        } else {
            this.oneoneId = 0.toString()

        }


        if (twokk.isChecked) {
            this.twokkId = 4.toString()


        } else {
            this.twokkId = 0.toString()

        }


        if (twoone.isChecked) {
            this.twooneId = 5.toString()

        } else {
            this.twooneId = 0.toString()

        }



        if (threekk.isChecked) {
            this.threekkId = 6.toString()

        } else {
            this.threekkId = 0.toString()

        }


        if (threeone.isChecked) {
            this.threeoneId = 7.toString()

        } else {
            this.threeoneId = 0.toString()

        }


        if (fourkk.isChecked) {
            this.fourkkId = 8.toString()

        } else {
            this.fourkkId = 0.toString()

        }


        if (fourone.isChecked) {
            this.fouroneId = 9.toString()

        } else {
            this.fouroneId = 0.toString()

        }

        if (fiveandmore.isChecked) {
            this.fiveAndMore = "\"5_and_more\""

        } else {
            this.fiveAndMore = 0.toString()

        }
        if (atypic.isChecked) {
            this.atypicId = 16.toString()

        } else {
            this.atypicId = 0.toString()

        }

        if (house.isChecked) {
            this.houseId = 29.toString()

        } else {
            this.houseId = 0.toString()

        }

        val arrayUD = arrayListOf<String>(
            garId.toString(),
            onekkId.toString(),
            oneoneId.toString(),
            twokkId.toString(),
            twooneId.toString(),
            threekkId.toString(),
            threeoneId.toString(),
            fourkkId.toString(),
            fouroneId.toString(),
            atypicId.toString(),
            houseId.toString(),
            fiveAndMore
        )

        println(arrayUD)
        arrayUD.removeAll(listOf("0"))

        return arrayUD.toString()

    }


    fun onCheckboxClicked(view: View) {
        var checked = view as CheckBox

        if (gar == checked) {
            if (gar.isChecked) {
                this.garId = 1.toString()
            } else {
                this.garId = 0.toString()
            }
        }
        if (onekk == checked) {
            if (onekk.isChecked) {
                this.onekkId = 2.toString()
            } else {
                this.onekkId = 0.toString()

            }
        }
        if (oneone == checked) {
            if (oneone == checked) {
                this.oneoneId = 3.toString()

            } else {
                this.oneoneId = 0.toString()

            }
        }
        if (twokk == checked) {
            if (twokk.isChecked) {
                this.twokkId = 4.toString()


            } else {
                this.twokkId = 0.toString()

            }
        }
        if (twoone == checked) {
            if (twoone.isChecked) {
                this.twooneId = 5.toString()

            } else {
                this.twooneId = 0.toString()

            }
        }
        if (threekk == checked) {

            if (threekk.isChecked) {
                this.threekkId = 6.toString()

            } else {
                this.threekkId = 0.toString()

            }
        }
        if (threeone == checked) {
            if (threeone.isChecked) {
                this.threeoneId = 7.toString()

            } else {
                this.threeoneId = 0.toString()

            }
        }
        if (fourkk == checked) {
            if (fourkk.isChecked) {
                this.fourkkId = 8.toString()

            } else {
                this.fourkkId = 0.toString()

            }
        }
        if (fourone == checked) {
            if (fourone.isChecked) {
                this.fouroneId = 9.toString()

            } else {
                this.fouroneId = 0.toString()

            }
        }
        if (fiveandmore == checked) {
            if (fiveandmore.isChecked) {
                this.fivekkId = 10.toString()
                this.fiveoneId = 11.toString()
                this.sixkkId = 12.toString()
                this.fiveAndMore = "\"5_and_more\""

            } else {
                this.fivekkId = 0.toString()
                this.fiveoneId = 0.toString()
                this.sixkkId = 0.toString()
                this.fiveAndMore = 0.toString()

            }
        }

        if (atypic == checked) {
            if (atypic.isChecked) {
                this.atypicId = 16.toString()

            } else {
                this.atypicId = 0.toString()

            }
        }

        if (house == checked) {
            if (house.isChecked) {
                this.houseId = 29.toString()

            } else {
                this.houseId = 0.toString()

            }
        }

    }
}



