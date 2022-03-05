package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var operand1 = ""
    private var operand2 = ""
    private var operator = ""

    private lateinit var btn: Array<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn4 = findViewById<Button>(R.id.btn4)
        val btn5 = findViewById<Button>(R.id.btn5)
        val btn6 = findViewById<Button>(R.id.btn6)
        val btn7 = findViewById<Button>(R.id.btn7)
        val btn8 = findViewById<Button>(R.id.btn8)
        val btn9 = findViewById<Button>(R.id.btn9)
        val btn10 = findViewById<Button>(R.id.btn10)
        val btn11 = findViewById<Button>(R.id.btn11)
        val btn12 = findViewById<Button>(R.id.btn12)
        val btn13 = findViewById<Button>(R.id.btn13)
        val btn14 = findViewById<Button>(R.id.btn14)
        val btn15 = findViewById<Button>(R.id.btn15)
        val btn16 = findViewById<Button>(R.id.btn16)
        val btn17 = findViewById<Button>(R.id.btn17)
        val btn18 = findViewById<Button>(R.id.btn18)
        val btn19 = findViewById<Button>(R.id.btn19)
        val btn20 = findViewById<Button>(R.id.btn20)

        btn = arrayOf(
            btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,
            btn11,btn12,btn13,btn14,btn15,btn16,btn17,btn18,btn19,btn20
        )

        for(i in btn){
            i.setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        val text = findViewById<TextView>(R.id.textView)
        when(view.id){
            R.id.btn6->{
                text.text = ""
                operand1 = ""
                operand2 = ""
                operator = ""
            }
            R.id.btn7->{
                val x : String = text.text.toString() + "7"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn8->{
                val x : String = text.text.toString() + "4"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn9->{
                val x : String = text.text.toString() + "1"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn10->{
                val x : String = text.text.toString() + "00"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn12->{
                val x : String = text.text.toString() + "8"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn13->{
                val x : String = text.text.toString() + "5"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn14->{
                val x : String = text.text.toString() + "2"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn15->{
                val x : String = text.text.toString() + "0"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn17->{
                val x : String = text.text.toString() + "9"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn18->{
                val x : String = text.text.toString() + "6"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn19->{
                val x : String = text.text.toString() + "3"
                text.text = x
                if(operator==""){
                    operand1 = x
                }
                else{
                    operand2 = x
                }
            }
            R.id.btn20->{
                val x : String = text.text.toString() + "."
            }
            R.id.btn1->{
                text.text = ""
                operator = "/"
            }
            R.id.btn2->{
                text.text = ""
                operator = "x"
            }
            R.id.btn3->{
                text.text = ""
                operator = "-"
            }
            R.id.btn4->{
                operator = "+"
                text.text = ""
            }
            R.id.btn11->{
                operator = "%"
                text.text = ""
            }
            R.id.btn5->{
                calculate(operator, operand1)
            }
        }
    }

    private fun calculate(operate : String, ope1  :String){
        val text = findViewById<TextView>(R.id.textView)
        var ans = ""
        if(operate == "+"){
            val a = ope1.toDouble()
            Log.d("operand1", "$ope1")
            operand2 = text.text.toString()
            Log.d("operand2", "$operand2")
            val b = operand2.toDouble()

            val c = a+b
            ans = String.format("%.5f", c)

            text.text = ans
        }

        if(operate == "-"){
            val a = ope1.toDouble()
            Log.d("operand1", "$ope1")
            operand2 = text.text.toString()
            Log.d("operand2", "$operand2")
            val b = operand2.toDouble()

            val c = a-b
            ans = String.format("%.5f", c)

            text.text = ans
        }

        if(operate == "x"){
            val a = ope1.toDouble()
            Log.d("operand1", "$ope1")
            operand2 = text.text.toString()
            Log.d("operand2", "$operand2")
            val b = operand2.toDouble()

            val c = a*b
            ans = String.format("%.5f", c)

            text.text = ans
        }

        if(operate == "/"){
            val a = ope1.toDouble()
            Log.d("operand1", "$ope1")
            operand2 = text.text.toString()
            Log.d("operand2", "$operand2")
            val b = operand2.toDouble()

            val c = a/b
            ans = String.format("%.5f", c)

            text.text = ans
        }

        if(operate == "%"){
            val a = ope1.toDouble()
            Log.d("operand1", "$ope1")
            operand2 = text.text.toString()
            Log.d("operand2", "$operand2")
            val b = operand2.toDouble()

            val c = (a/b)*100
            ans = String.format("%.5f", c)

            text.text = ans
        }
        operand1 = ans
    }
}