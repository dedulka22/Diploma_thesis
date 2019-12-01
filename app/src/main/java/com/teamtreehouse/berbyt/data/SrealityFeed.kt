package com.teamtreehouse.berbyt.data


/*
* Author: Deana Mareková
* Description: Create classes for JSON (function fromJson()) - Sreality feeds in List
* Licence: MIT
* */

class SrealityFeed(val _embedded: Embedded)
class Embedded (val estates: List<Estate>)
class Estate {
    var hash_id: Number? = 0
    var price: Number? = 0
    var name: String? = ""
    var locality: String? = ""
    var _links: Links
    var gps: Gps

    constructor(Price: Number, Name: String, Locality: String, Links: Links, Gps: Gps) : super(){
        this.price = Price
        this.name = Name
        this.locality = Locality
        this._links = Links
        this.gps = Gps
    }
}
class Links (val image_middle2: List<Image_middle2>)

class Gps {
    var lat: Double = 0.0
    var lon: Double = 0.0

    constructor(Lat: Double, Lon: Double) : super() {
        this.lat = Lat
        this.lon = Lon
    }

    fun getLat() : Double? {
        return lat
    }

    fun getLon() : Double? {
        return lon
    }
}

class Image_middle2 {
    var href: String

    constructor(Href: String) : super() {
        this.href = Href
    }
}


