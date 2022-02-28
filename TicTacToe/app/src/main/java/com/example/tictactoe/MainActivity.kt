package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var board: Array<Array<Button>>
    var PLAYER = true
    var Turn_COUNT = 0
    var boardStatus = Array(3) {
        IntArray(3)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button3 = findViewById<Button>(R.id.button3)
        val button9 = findViewById<Button>(R.id.button9)
        board = arrayOf(
            arrayOf(button1,button2,button3),
            arrayOf(button4,button5,button6),
            arrayOf(button7,button8,button9)
        )
        for(i in board){
            for(button in i){
                button.setOnClickListener(this)
            }
        }

        initalize_board_status()

        val resetbtn = findViewById<Button>(R.id.resetbtn)

        resetbtn.setOnClickListener{
            PLAYER = true
            Turn_COUNT = 0
            initalize_board_status()
        }
    }

    private fun initalize_board_status() {
        for(i in 0..2){
            for(j in 0..2){
                boardStatus[i][j]=-1
            }
        }
        for(i in board){
            for(j in i){
                j.isEnabled = true
                j.text = ""
            }
        }
    }

    override fun onClick(view:View) {
        when(view.id){
            R.id.button1->{
                updateValue(row=0, col=0, player=PLAYER)
            }
            R.id.button2->{
                updateValue(row=0, col=1, player=PLAYER)
            }
            R.id.button3->{
                updateValue(row=0, col=2, player=PLAYER)
            }
            R.id.button4->{
                updateValue(row=1, col=0, player=PLAYER)
            }
            R.id.button5->{
                updateValue(row=1, col=1, player=PLAYER)
            }
            R.id.button6->{
                updateValue(row=1, col=2, player=PLAYER)
            }
            R.id.button7->{
                updateValue(row=2, col=0, player=PLAYER)
            }
            R.id.button8->{
                updateValue(row=2, col=1, player=PLAYER)
            }
            R.id.button9-> {
                updateValue(row=2, col=2, player=PLAYER)
            }
        }
        Turn_COUNT++
        PLAYER = !PLAYER
        if(PLAYER){
            updateDisplay("Player X turn")
        }
        else{
            updateDisplay("Player O turn")
        }
        if(Turn_COUNT==9){
            updateDisplay("Draw")
        }
        winner()
    }
    private fun winner(){
        for(i in 0..2){
            if(boardStatus[i][0]==boardStatus[i][1] && boardStatus[i][0]==boardStatus[i][2]){
                if(boardStatus[i][0]==1){
                    updateDisplay("Player X is winner")
                    break
                }
                else if(boardStatus[i][0]==0){
                    updateDisplay("Player 0 is winner")
                    break
                }
            }
        }

        for(i in 0..2){
            if(boardStatus[0][i]==boardStatus[1][i] && boardStatus[0][i]==boardStatus[2][i]){
                if(boardStatus[0][i]==1){
                    updateDisplay("Player X is winner")
                    break
                }
                else if(boardStatus[0][i]==0){
                    updateDisplay("Player 0 is winner")
                    break
                }
            }
        }

        if(boardStatus[0][0] == boardStatus[1][1] && boardStatus[0][0] == boardStatus[2][2]){
            if(boardStatus[0][0]==1){
                updateDisplay("Player X is winner")
            }
            else if(boardStatus[0][0]==0){
                updateDisplay("Player 0 is winner")
            }
        }

        if(boardStatus[0][2] == boardStatus[1][1] && boardStatus[0][2] == boardStatus[2][0]){
            if(boardStatus[0][2]==1){
                updateDisplay("Player X is winner")
            }
            else if(boardStatus[0][2]==0){
                updateDisplay("Player 0 is winner")
            }
        }
    }

    private fun updateDisplay(s: String) {
        val displayTv = findViewById<TextView>(R.id.displayTv)
        displayTv.text = s
        if(s.contains("winner")){
            disableButton()
        }
    }

    private fun disableButton(){
        for(i in board){
            for(j in i){
                j.isEnabled=false
            }
        }
    }

    private fun updateValue(row: Int, col: Int, player: Boolean) {
        val text:String = if(player) "X" else "0"
        val value:Int = if(player) 1 else 0
        board[row][col].apply {
            isEnabled=false
            setText(text)
        }

        boardStatus[row][col] = value
    }
}