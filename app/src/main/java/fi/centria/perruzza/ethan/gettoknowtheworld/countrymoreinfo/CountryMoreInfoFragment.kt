package fi.centria.perruzza.ethan.gettoknowtheworld.countrymoreinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fi.centria.perruzza.ethan.gettoknowtheworld.PrivateData
import fi.centria.perruzza.ethan.gettoknowtheworld.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CountryMoreInfoFragment(var iso2Country: String) : Fragment() {
    lateinit var country_name: TextView
    lateinit var country_real_name: TextView
    lateinit var country_flag: TextView
    lateinit var country_capital: TextView
    lateinit var country_currency: TextView
    lateinit var country_phonecode: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        country_name = view.findViewById(R.id.country_name)
        country_real_name = view.findViewById(R.id.country_real_name)
        country_flag = view.findViewById(R.id.country_flag)
        country_capital = view.findViewById(R.id.country_capital)
        country_currency = view.findViewById(R.id.country_currency)
        country_phonecode = view.findViewById(R.id.country_phonecode)

        lifecycleScope.launch {
            val jsonResult = getMoreInfoCountryAsync(iso2Country)

            if (jsonResult[2] != 'i') {
                //error occurred and we don't want the app to crash
                return@launch
            }

            val listType = object : TypeToken<CountryMoreInfoData>() {}.type
            val countryMoreInfoData: CountryMoreInfoData = Gson().fromJson(jsonResult, listType)

            country_name.text = countryMoreInfoData.name
            country_real_name.text = countryMoreInfoData.native
            country_flag.text =countryMoreInfoData.emoji
            country_capital.text =countryMoreInfoData.capital
            country_currency.text =countryMoreInfoData.currency
            country_phonecode.text =countryMoreInfoData.phonecode
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