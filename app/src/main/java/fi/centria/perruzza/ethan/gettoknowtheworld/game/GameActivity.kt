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
            // get the coutries list
            val jsonResult = getCountriesAsync()

            // check if the request was successful
            if (jsonResult[0] != '[') {
                //error occurred and we don't want the app to crash
                return@launch
            }

            // convert the data from json
            val listType = object : TypeToken<List<CountryListData>>() {}.type
            countryList = Gson().fromJson(jsonResult, listType)

            generateNewQuestion()
        }

        // links the different ui elements to the variables
        button1 = findViewById(R.id.button_1)
        button2 = findViewById(R.id.button_2)
        button3 = findViewById(R.id.button_3)
        button4 = findViewById(R.id.button_4)
        flag = findViewById(R.id.country_flag)
        buttonDiscover = findViewById(R.id.button_discover)
        correctDisplay = findViewById(R.id.correct_answer_display)
        wrongDisplay = findViewById(R.id.wrong_answer_display)
        answerTextView = findViewById(R.id.answer)

        // sets the action on buttons when clicked
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

        // allows you to switch to discover activity
        buttonDiscover.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateNewQuestion() {
        // select a random country from the country list
        answer =  countryList[(0..countryList.size).random()]
        val choices : ArrayList<CountryListData> = ArrayList()
        choices.add(answer)
        while (choices.size != 4)
        {
            // add 3 other countries thats* are not already in the slection os there is no doublon
            val rand = (0..countryList.size).random()
            if (!choices.contains(countryList[rand])) {
                choices.add(countryList[rand])
            }
        }
        // shuffle the choice so the right answer is not always at the same place
        choices.shuffle()
        //display the text on the buttons
        button1.text = choices[0].name
        button2.text = choices[1].name
        button3.text = choices[2].name
        button4.text = choices[3].name

        lifecycleScope.launch {
            // get the specific info on the answer country so we get the flag
            val jsonResult = getMoreInfoCountryAsync(answer.iso2)

            // check if there was no error in the request
            if (jsonResult[2] != 'i') {
                //error occurred and we don't want the app to crash
                return@launch
            }

            // convert from json
            val listType = object : TypeToken<CountryMoreInfoData>() {}.type
            val countryMoreInfoData: CountryMoreInfoData = Gson().fromJson(jsonResult, listType)

            // display the flag
            flag.text = countryMoreInfoData.emoji
        }
    }

    private fun answer(button: Button) {
        // Function that tell if the answer we gave is right or not regarding which was pressed
        if (button.text == answer.name) {
            // if answer is right
            // tell the user
            correctDisplay.visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(1_000)
                correctDisplay.visibility = View.GONE
            }
        }
        else {
            // if answer is not right
            // tell the user nad tell him what was the right answer
            answerTextView.text = answer.name
            wrongDisplay.visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(1_000)
                wrongDisplay.visibility = View.GONE
            }
        }

        // new question
        generateNewQuestion()
    }

    suspend fun getCountriesAsync(): String {
        // Function that retrieves the country list from the API
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.countrystatecity.in/v1/countries")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            // API key is stored in a local file that will never be pushed to server.
            // WARNING : Must not put the API KEY in hard! use the variable
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
        // Function that retrieve the specific info on a specific country from the API
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.countrystatecity.in/v1/countries/$iso2Country")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            // API key is stored in a local file that will never be pushed to server.
            // WARNING : Must not put the API KEY in hard! use the variable
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