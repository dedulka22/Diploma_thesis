package com.teamtreehouse.berbyt.data


/*
* Author: Deana Mareková
* Description: Create classes for JSON (function fromJson()) - UlovDomov feeds in List
* Licence: MIT
* */

class UlovDomovFeed(val offers: List<Offers>)
class Offers {
    var id: Number? = 0
    var price_rental: Number? = 0
    var disposition_id: Int? = 0
    var acreage: Number? = 0
    var street: Street
    var village_part: Village_part
    var village: Village
    var photos: List<Photos>
    var lng: Double = 0.0
    var lat: Double = 0.0

    constructor(Id: Number, Price: Number, Disp: Int, Acr: Number, Street: Street, Vill_part: Village_part, Vill: Village, Photos: List<Photos>, Lng: Double, Lat: Double) : super() {
        this.id = Id
        this.price_rental = Price
        this.disposition_id = Disp
        this.acreage = Acr
        this.street = Street
        this.village_part = Vill_part
        this.village = Vill
        this.photos = Photos
        this.lng = Lng
        this.lat = Lat
    }
}

class Street(val label: String)
class Village_part(val label: String)
class Village(val label: String)
class Photos(val path: String)
