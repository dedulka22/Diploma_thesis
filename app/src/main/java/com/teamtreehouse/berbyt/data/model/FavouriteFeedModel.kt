package com.teamtreehouse.berbyt.data.model

/*
* Author: Deana Mareková
* Description: FavouriteFeedModel for save data for collection FavouriteFeed (Firestore)
* Licence: MIT
* */
class FavouriteFeedModel {
    var userId: String = ""
    var feedId: String = ""
    var portal: String = ""
    var name: String = ""
    var locality: String = ""
    var price: String = ""
    var image: String = ""
    var note: String = ""
    var found: Boolean = false

    constructor(UserId: String, FeedId: String, Portal: String, Name: String,
                Locality: String, Price: String, Image: String, Found: Boolean,
                Note: String) : super() {
        this.userId = UserId
        this.feedId = FeedId
        this.portal = Portal
        this.name = Name
        this.locality = Locality
        this.price = Price
        this.image = Image
        this.found = Found
        this.note = Note
    }

}