package com.teamtreehouse.berbyt.data.model

/*
* Author: Deana Mareková
* Description: CacheFeedsModel for save data for collection CacheFeeds (Firestore)
* Licence: MIT
* */
class CacheFeedsModel {
    var feedsUD: String = ""
    var feedsSR: String = ""
    var userId: String = ""

    constructor(FeedsUD: String, FeedsSR: String, UserId: String) : super() {
        this.feedsUD = FeedsUD
        this.feedsSR = FeedsSR
        this.userId = UserId
    }
}