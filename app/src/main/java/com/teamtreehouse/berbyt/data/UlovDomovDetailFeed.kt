package com.teamtreehouse.berbyt.data


/*
* Author: Deana Mareková
* Description: Create class for JSON (function fromJson()) - UlovDomov detail feed
* Licence: MIT
* */

class UlovDomovDetailFeed(val photos: List<Photos>, val disposition_id: Number, val acreage: Number,
                          val village: Village, val village_part: Village_part, val street: Street,
                          val description: String, val price_rental: Number,
                          val latitude: Double, val longitude: Double, val price_note: String,
                          val price_monthly_fee: Number, val price_deposit: Number, val price_commission: Number,
                          val conveniences: Array<String>, val furnishing_id: String,
                          val available_date: String,val publish_date: String)


