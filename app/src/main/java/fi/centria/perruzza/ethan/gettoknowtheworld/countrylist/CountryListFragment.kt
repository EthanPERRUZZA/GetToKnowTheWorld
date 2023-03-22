package fi.centria.perruzza.ethan.gettoknowtheworld.countrylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fi.centria.perruzza.ethan.gettoknowtheworld.PrivateData.Companion.API_KEY
import fi.centria.perruzza.ethan.gettoknowtheworld.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CountryListFragment : Fragment() {
    // Fragment that display a list of the countries
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Link the different screen object to the variables
        linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView = view.findViewById(R.id.country_recycler_view)
        recyclerView.layoutManager = linearLayoutManager

        lifecycleScope.launch {
            // get the info
            val jsonResult = getCountriesAsync()

            // check if the request was successful
            if (jsonResult[0] != '[') {
                //error occurred and we don't want the app to crash
                return@launch
            }

            // convert to json format
            val listType = object : TypeToken<List<CountryListData>>() {}.type
            val countryList: List<CountryListData> = Gson().fromJson(jsonResult, listType)

            // display the info on the list via our adapter
            adapter = CountryAdapter(view.context, countryList, this@CountryListFragment)
            recyclerView.adapter = adapter
        }

    }

    suspend fun getCountriesAsync(): String {
        // Function that retrieves the country list from the API
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.countrystatecity.in/v1/countries")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            // API key is stored in a local file that will never be pushed to server.
            // WARNING : Must not put the API KEY in hard! use the variable
            connection.setRequestProperty("X-CSCAPI-KEY", API_KEY)

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