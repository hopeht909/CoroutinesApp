package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var btGetAdvice : Button
    lateinit var btPause : Button
    lateinit var tvAdvice: TextView

    val adviceUrl = "https://api.adviceslip.com/advice"
    var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btGetAdvice = findViewById(R.id.button)
        btPause = findViewById(R.id.button2)
        tvAdvice = findViewById(R.id.textView)

        btGetAdvice.setOnClickListener {
            while(!isPaused){
                requestApi()
        }

        }
        btPause.setOnClickListener {
            isPaused= true
        }

    }


    private fun requestApi() {

        CoroutineScope(Dispatchers.IO).launch {


            val data = async {

                fetchRandomAdvice()

            }.await()

            if (data.isNotEmpty())
            {

                updateAdviceText(data)
            }

        }

    }

    private fun fetchRandomAdvice():String{

        var response=""
        try {
            response = URL(adviceUrl).readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")

        }
        return response

    }

    private suspend fun updateAdviceText(data:String)
    {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            tvAdvice.text = advice

        }

    }
}