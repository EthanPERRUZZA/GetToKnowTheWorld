package fi.centria.perruzza.ethan.gettoknowtheworld

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CountryListFragment : Fragment() {
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

        linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView = view.findViewById(R.id.country_recycler_view)
        recyclerView.layoutManager = linearLayoutManager

        val countryList : ArrayList<CountryData> = arrayListOf()
        countryList.add(CountryData("France", "Europe", "Western Europe"))
        countryList.add(CountryData("Germany", "Europe", "Western Europe"))
        countryList.add(CountryData("Finland", "Europe", "Northern Europe"))

        adapter = CountryAdapter(view.context, countryList)
        recyclerView.adapter = adapter
    }
}