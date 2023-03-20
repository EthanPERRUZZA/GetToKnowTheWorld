package fi.centria.perruzza.ethan.gettoknowtheworld.countrymoreinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import fi.centria.perruzza.ethan.gettoknowtheworld.PrivateData
import fi.centria.perruzza.ethan.gettoknowtheworld.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CountryMoreInfoFragment(var iso2Country: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val jsonResult = getMoreInfoCountryAsync(iso2Country)

            if (jsonResult[0] != '[') {
                //error occurred and we don't want the app to crash
                return@launch
            }
        }

    }

    private suspend fun getMoreInfoCountryAsync(iso2Country:String): String {
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