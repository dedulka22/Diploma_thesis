package com.teamtreehouse.berbyt.activity

import android.os.Bundle
import com.teamtreehouse.berbyt.R

/*
* Author: Deana Mareková
* Description: ContactActivity for show contact
* Licence: MIT
* */
class ContactActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        initToolbar(R.id.toolbar)

        initLayout(R.layout.content_contact)

    }
}
