package com.dean.spellbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object {
        var userID: Int? = null
        var bookID: Int? = null

        const val REQUEST_CAMERA_PERMISSION = 1
        const val REQUEST_CAMERA_USAGE = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}