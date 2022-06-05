package com.dean.spellbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    companion object {
        var userID: Int? = null
        var bookID: Int? = null

        val REQUEST_CAMERA_PERMISSION = 1
        val REQUEST_CAMERA_USAGE = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}