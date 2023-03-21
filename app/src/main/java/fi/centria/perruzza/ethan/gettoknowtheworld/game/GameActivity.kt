package fi.centria.perruzza.ethan.gettoknowtheworld.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fi.centria.perruzza.ethan.gettoknowtheworld.MainActivity
import fi.centria.perruzza.ethan.gettoknowtheworld.PrivateData
import fi.centria.perruzza.ethan.gettoknowtheworld.R
import fi.centria.perruzza.ethan.gettoknowtheworld.countrylist.CountryListData
import fi.centria.perruzza.ethan.gettoknowtheworld.countrymoreinfo.CountryMoreInfoData
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity() {
    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button
    lateinit var button4: Button
    lateinit var flag: TextView
    lateinit var answer : CountryListData
    lateinit var countryList: List<CountryListData>
    lateinit var buttonDiscover: ImageButton
    lateinit var correctDisplay: RelativeLayout
    lateinit var wrongDisplay: RelativeLayout
    lateinit var answerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        lifecycleScope.launch {
            val jsonResult = getCountriesAsync()

            if (jsonResult[0] != '[') {
                //error occurred and we don't want the app to crash
                return@launch
            }

            val listType = object : TypeToken<List<CountryListData>>() {}.type
            countryList = Gson().fromJson(jsonResult, listType)

            generateNewQuestion()
        }

        button1 = findViewById(R.id.button_1)
        button2 = findViewById(R.id.button_2)
        button3 = findViewById(R.id.button_3)
        button4 = findViewById(R.id.button_4)
        flag = findViewById(R.id.country_flag)
        buttonDiscover = findViewById(R.id.button_discover)
        correctDisplay = findViewById(R.id.correct_answer_display)
        wrongDisplay = findViewById(R.id.wrong_answer_display)
        answerTextView = findViewById(R.id.answer)

        button1.setOnClickListener{
            answer(button1)
        }
        button2.setOnClickListener{
            answer(button2)
        }
        button3.setOnClickListener{
            answer(button3)
        }
        button4.setOnClickListener{
            answer(button4)
        }

        buttonDiscover.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateNewQuestion() {
        answer =  countryList[(0..countryList.size).random()]
        val choices : ArrayList<CountryListData> = ArrayList()
        choices.add(answer)
        while (choices.size != 4)
        {
            val rand = (0..countryList.size).random()
            if (!choices.contains(countryList[rand])) {
                choices.add(countryList[rand])
            }
        }
        choices.shuffle()
        button1.text = choices[0].name
        button2.text = choices[1].name
        button3.text = choices[2].name
        button4.text = choices[3].name

        lifecycleScope.launch {
            val jsonResult = getMoreInfoCountryAsync(answer.iso2)

            if (jsonResult[2] != 'i') {
                //error occurred and we don't want the app to crash
                return@launch
            }

            val listType = object : TypeToken<CountryMoreInfoData>() {}.type
            val countryMoreInfoData: CountryMoreInfoData = Gson().fromJson(jsonResult, listType)

            flag.text = countryMoreInfoData.emoji
        }
    }

    private fun answer(button: Button) {
        if (button.text == answer.name) {
            correctDisplay.visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(1_000)
                correctDisplay.visibility = View.GONE
            }
        }
        else {
            answerTextView.text = answer.name
            wrongDisplay.visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(1_000)
                wrongDisplay.visibility = View.GONE
            }
        }

        generateNewQuestion()
    }

    suspend fun getCountriesAsync(): String {
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.countrystatecity.in/v1/countries")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("X-CSCAPI-KEY", PrivateData.API_KEY)

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                response
            } else {
                "Error response code: $responseCode"
            }
        }
    }

    suspend fun getMoreInfoCountryAsync(iso2Country:String): String {
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.countrystatecity.in/v1/countries/$iso2Country")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("X-CSCAPI-KEY", PrivateData.API_KEY)

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                response
            } else {
                "Error response code: $responseCode"
            }
        }
    }
}