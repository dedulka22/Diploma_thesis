package com.teamtreehouse.berbyt.activity

import android.os.Bundle
import android.widget.*
import com.teamtreehouse.berbyt.R
import android.widget.LinearLayout
import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox

/*
* Author: Deana Mareková
* Description: LocalityActivity for choose locality and dynamic create checkbox
* Licence: MIT
* */
class LocalityActivity : BaseActivity() {

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

    val childMapPraha = mapOf(
        "5001" to "Praha 1",
        "5002" to "Praha 2",
        "5003" to "Praha 3",
        "5004" to "Praha 4",
        "5005" to "Praha 5",
        "5006" to "Praha 6",
        "5007" to "Praha 7",
        "5008" to "Praha 8",
        "5009" to "Praha 9",
        "5010" to "Praha 10"
    )

    val childMapJihocesky = mapOf(
        "1" to "Ćeské Budějovice",
        "2" to "Český Krumlov",
        "3" to "Jindřichův Hradec",
        "4" to "Písek",
        "5" to "Prachatice",
        "6" to "Strakonice",
        "7" to "Tábor"
    )

    val childMapJihomoravsky = mapOf(
        "71" to "Blansko",
        "72" to "Břeclav",
        "73" to "Brno-město",
        "74" to "Brno-venkov",
        "75" to "Hodonín",
        "76" to "Vyškov",
        "77" to "Znojmo"
    )

    val childMapKarlovarsky = mapOf(
        "9" to "Cheb",
        "10" to "Karlovy Vary",
        "16" to "Sokolov"
    )

    val childMapUstecky = mapOf(
        "20" to "Chomutov",
        "19" to "Děčín",
        "23" to "Litoměřice",
        "24" to "Louny",
        "25" to "Most",
        "26" to "Teplice",
        "27" to "Ústí nad Labem"
    )

    val childMapLiberecky = mapOf(
        "18" to "Česká Líba",
        "21" to "Jablonec nad Nisou",
        "22" to "Liberec",
        "34" to "Semily"
    )

    val childMapKralovehradecky = mapOf(
        "28" to "Hradec Králové",
        "30" to "Jičín",
        "31" to "Náchod",
        "33" to "Rychnov nad Kněžnou",
        "36" to "Trutnov"
    )

    val childMapPardubicky = mapOf(
        "29" to "Chrudim",
        "32" to "Pardubice",
        "35" to "Svitavy",
        "37" to "Ústí nad Orlicí"
    )

    val childMapOlomoucky = mapOf(
        "40" to "Jeseník",
        "42" to "Olomouc",
        "43" to "Přerov",
        "44" to "Prostějov",
        "46" to "Šumperk"
    )

    val childMapZlinsky = mapOf(
        "38" to "Kroměříž",
        "39" to "Uherské Hradiště",
        "41" to "Vsetín",
        "45" to "Zlín"
    )

    val childMapStredocesky = mapOf(
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
        "59" to "Rakovník"
    )

    val childMapMoravskoslezsky = mapOf(
        "60" to "Bruntál",
        "61" to "Frýdek-Místek",
        "62" to "Karviná",
        "63" to "Nový Jičín",
        "64" to "Opava",
        "65" to "Ostrava-město"
    )

    val childMapVysocina = mapOf(
        "66" to "Havlíčkův Brod",
        "67" to "Jihlava",
        "68" to "Pelhřimov",
        "69" to "Třebíč",
        "70" to "Žďár nad Sázavou"
    )

    val childMapPlzensky = mapOf(
        "8" to "Domažlice",
        "11" to "Klatovy",
        "12" to "Plzeň-město",
        "13" to "Plzeň-jih",
        "14" to "Plzeň-sever",
        "15" to "Rokycany",
        "17" to "Tachov"
    )

    lateinit var linearLayout : LinearLayout
    lateinit var childCheckBoxContainer : LinearLayout
    lateinit var checkBox : CheckBox
    lateinit var radioButton : RadioButton
    lateinit var radioGroup : RadioGroup

    var arrayChecked = arrayListOf<String?>()
    var locality_region_id : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locality)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_locality)
        linearLayout = findViewById(R.id.checkbox_container)
        childCheckBoxContainer = findViewById(R.id.child_checkbox_container)
        radioGroup = RadioGroup(this)
        checkBox = CheckBox(this)
        radioButton = RadioButton(this)

        dynamicCreateCheckbox()

    }


    override fun onCreateOptionsMenu (menu: Menu): Boolean{

        val inflater = menuInflater
        inflater.inflate(R.menu.locality_confirm, menu)
        for (i in 0 until menu.size()) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.Ok -> {
                var checkedLoc = checkedLocality(this)
                var x1 = checkedLoc.replace("[", "")
                checkedLoc = x1.replace("]", "")

                val intent = Intent(this@LocalityActivity, SearchActivity::class.java)
                intent.putExtra("checkedLocality", checkedLoc)

                locality_region_id = mapRegion.filterValues{it == locality_region_id}.keys.toString()
                var resultRegion = locality_region_id.replace("[", "").replace("]", "")

                intent.putExtra("locality_region_id", resultRegion)
                if (checkedLoc != "") {
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LocalityActivity, "Prosím vyberte si lokalitu.", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    @SuppressLint("ResourceAsColor")
    fun dynamicCreateCheckbox() {

        val sizeMap = mapRegion.size

        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val textView = TextView(this)
        textView.setText(R.string.locality)
        textView.setTextSize(20F)
        textView.setPadding(20, 50, 20, 50)
        textView.setTextColor(R.color.colorPrimaryDark)

        linearLayout.addView(textView)
        linearLayout.addView(radioGroup, p)

        // Create Checkbox Dynamically

        for (i in 1..sizeMap) {

            val radioButton = RadioButton(this)
            radioButton.setText(mapRegion[i.toString()])

            radioGroup.addView(radioButton, p)

            val b = radioButton.setOnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked == true) {
                    if (radioButton.text == "Jihočeský") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapJihocesky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapJihocesky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Plzeňský") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapPlzensky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapPlzensky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Karlovarský") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapKarlovarsky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapKarlovarsky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Ústecký") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapUstecky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapUstecky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Liberecký") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapLiberecky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapLiberecky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Královéhradecký") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapKralovehradecky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapKralovehradecky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Pardubický") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapPardubicky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapPardubicky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Olomoucký") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapOlomoucky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapOlomoucky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Zlínský") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapZlinsky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapZlinsky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Praha") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapPraha.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapPraha[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }

                        }

                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Středočeský") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapStredocesky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapStredocesky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Moravskoslezský") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapMoravskoslezsky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapMoravskoslezsky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }

                if (isChecked == true) {
                    if (radioButton.text == "Vysočina") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()
                        for (i in childMapVysocina.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapVysocina[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    arrayChecked.add(checkBox.text.toString())
                                    println("arrayChecked: " + arrayChecked.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                    println("removeCheckbox: " + arrayChecked.toString())
                                }
                            }
                        }
                    }
                }



                if (isChecked == true) {
                    if (radioButton.text == "Jihomoravský") {
                        locality_region_id = mapRegion[i.toString()].toString()
                        childCheckBoxContainer.removeAllViewsInLayout()

                        for (i in childMapJihomoravsky.keys) {
                            val checkBox = CheckBox(this)
                            checkBox.text = childMapJihomoravsky[i]
                            childCheckBoxContainer.addView(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (checkBox.isChecked) {
                                    //val keys = childMapJihomoravsky.filterValues { it == checkBox.text.toString() }.keys.toString()
                                    arrayChecked.add(checkBox.text.toString())
                                } else {
                                    arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
                                }
                            }
                        }

                    }
                }

            }

        }

    }

    fun checkedLocality(view: LocalityActivity) : String {

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (checkBox.isChecked) {
                arrayChecked.add(checkBox.text.toString())
            } else {
                arrayChecked.removeAt(arrayChecked.lastIndexOf(checkBox.text.toString()))
            }

        }

        return arrayChecked.toString()
    }


}



