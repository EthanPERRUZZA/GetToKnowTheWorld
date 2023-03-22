package fi.centria.perruzza.ethan.gettoknowtheworld

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import fi.centria.perruzza.ethan.gettoknowtheworld.countrylist.CountryListFragment

class SelectCountryHomePage : Fragment() {
    lateinit var seeCountriesListButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_country_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // links the ui to the code
        seeCountriesListButton = view.findViewById(R.id.see_countries_list_button)

        // when clicked on the button launch the next fragment with the country list
        seeCountriesListButton.setOnClickListener {
            val fragmentTransaction = this.parentFragmentManager.beginTransaction()
            val countryListFragment = CountryListFragment()
            fragmentTransaction.replace(R.id.fragment_container_view, countryListFragment)
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}