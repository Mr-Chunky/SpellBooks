package com.dean.spellbooks

import android.content.SharedPreferences
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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}