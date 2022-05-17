package com.dean.spellbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvCreateAccountLink: TextView = findViewById(R.id.tvCreateAccountLink)
        var etLoginUsername: EditText = findViewById(R.id.etLoginUsername)
        var etLoginPassword: EditText = findViewById(R.id.etLoginPassword)
    }
}