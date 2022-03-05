package com.example.list_view_custom_array_adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    val obj = ClassForCustomAdapter()
    val listOfNames : ArrayList<ClassForCustomAdapter> = obj.listOfNamesWithSkills()
    lateinit var list : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = findViewById(R.id.list)
        val obj1 = customAdapter()
        list.adapter = obj1
    }

    inner class customAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return listOfNames.size
        }

        override fun getItem(position: Int): ClassForCustomAdapter {
            return listOfNames.get(position)
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val itemView: View = LayoutInflater.from(this@MainActivity).inflate(
                R.layout.custom_layout,
                parent,
                false
            )

            val name : TextView = itemView.findViewById(R.id.name)
            val skill : TextView = itemView.findViewById(R.id.skill)

            name.text = getItem(position).getName()
            skill.text = getItem(position).getSkill()

            return itemView
        }

    }
}