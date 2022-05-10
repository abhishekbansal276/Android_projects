package com.example.moviesearch

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.lang.Exception
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
        lateinit var year : TextView
        lateinit var actor : TextView
        lateinit var director : TextView
        lateinit var country : TextView
        lateinit var language : TextView
        lateinit var plot : TextView
        lateinit var img : ImageView
        lateinit var title : TextView
        lateinit var moName : EditText
        lateinit var btn : Button
        lateinit var genre : TextView
        lateinit var rating : TextView

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            year = findViewById(R.id.year)
            actor = findViewById(R.id.actor)
            director = findViewById(R.id.director)
            country = findViewById(R.id.country)
            language = findViewById(R.id.language)
            plot = findViewById(R.id.plot)
            img = findViewById(R.id.imageView)
            moName = findViewById(R.id.mName)
            btn = findViewById(R.id.btn)
            title = findViewById(R.id.title)
            genre = findViewById(R.id.genre)
            rating = findViewById(R.id.rating)
            btn.setOnClickListener {
                search()
            }
    }

    private fun search (){
        var name : String = moName.text.toString()
        var requestQueue = Volley.newRequestQueue(this)
        if(name.isEmpty()){
            moName.setError("Please enter name to search")
            return
        }
        var url : String = "https://www.omdbapi.com/?t=$name&plot=full&apikey=9fc36e09"
        val jsonObjectRequest =
            JsonObjectRequest(
                Request.Method.GET, url, null,
                { response: JSONObject ->
                    try {
                        title.text = "Title : " + response.getString("Title")
                        year.text = "Year : " + response.getString("Year") + ", Runtime : " + response.getString("Runtime")
                        actor.text = "Actors : " + response.getString("Actors")
                        director.text = "Director : " + response.getString("Director")
                        country.text = "Country : " + response.getString("Country")
                        language.text = "Language : " + response.getString("Language")
                        plot.text = "Plot : " + response.getString("Plot")
                        genre.text = "Genre : " + response.getString("Genre")
                        rating.text = "Rating : " + response.getString("imdbRating")
                        year.isVisible = true
                        actor.isVisible = true
                        director.isVisible = true
                        country.isVisible = true
                        language.isVisible = true
                        plot.isVisible = true
                        img.isVisible = true
                        title.isVisible = true
                        genre.isVisible = true
                        rating.isVisible = true
                        var pUrl : String = response.getString("Poster")
                        if(pUrl == "N/A"){
                            
                        }else{
                            Picasso.get().load(pUrl).into(img)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            ) { error: VolleyError ->
                println("Error  " + error.message)
                Toast.makeText(
                    applicationContext,
                    error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        requestQueue!!.add(jsonObjectRequest)
    }
}