package com.teamtreehouse.berbyt.data.model

/*
* Author: Deana Mareková
* Description: UserDbModel for save data for collection Users (Firestore)
* Licence: MIT
* */
class UserDbModel {
    var first: String = ""
    var last: String = ""

    constructor(
        First: String, Last: String) : super() {
        this.first = First
        this.last = Last
    }

}