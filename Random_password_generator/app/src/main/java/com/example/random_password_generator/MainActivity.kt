package com.example.random_password_generator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import java.util.*
import kotlin.random.Random as Random1

class MainActivity : AppCompatActivity(), View.OnClickListener{
    val str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#!$*"
    val pass = str.toCharArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.button)

        btn.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        val text = findViewById<TextView>(R.id.textView2)
        generate(text.text.toString().toInt())
    }

    fun generate(len:Int){
        val text = findViewById<TextView>(R.id.textView)
        val text2 = findViewById<TextView>(R.id.textView2)
        text.isVisible = true
        text2.visibility = View.GONE

        val ans = StringBuilder()

        val rand = Random()

        for (i in 0 until len) {
            val c = pass!![rand.nextInt(pass.size)]
            ans.append(c)
        }
        text.text = ans.toString()
    }

}