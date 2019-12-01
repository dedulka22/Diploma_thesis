package com.teamtreehouse.berbyt.data


/*
* Author: Deana Mareková
* Description: Create classes for JSON (function fromJson()) - Sreality detail feed
* Licence: MIT
* */
class SrealityDetailFeed(
    val _embedded: Embedded2, val name: Name, val locality: Locality,
    val text: Text, val price_czk: PriceCzk, val map: Map
)

class Embedded2(val images: List<Images>)
class Images(val _links: Links2)
class Links2(val gallery: Gallery)
class Gallery {
    var href: String

    constructor(Href: String) : super() {
        this.href = Href
    }
}

class Name {
    var value: String? = ""

    constructor(Value: String) : super() {
        this.value = Value
    }
}

class Locality {
    var value: String? = ""

    constructor(Value: String) : super() {
        this.value = Value
    }
}

class Text {
    var value: String? = ""

    constructor(Value: String) : super() {
        this.value = Value
    }
}

class PriceCzk {
    var value: String? = ""
    var unit: String? = ""

    constructor(Value: String, Unit: String) : super() {
        this.value = Value
        this.unit = Unit
    }
}

class Map {
    var lat: Double = 0.0
    var lon: Double = 0.0

    constructor(Lat: Double, Lon: Double) : super() {
        this.lat = Lat
        this.lon = Lon
    }

    fun getLat(): Double? {
        return lat
    }

    fun getLon(): Double? {
        return lon
    }
}










