package fi.centria.perruzza.ethan.gettoknowtheworld

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CountryAdapter(context: Context, val countryList : List<CountryListData>) : RecyclerView.Adapter<CountryAdapter.CountryHolder>() {
    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    class CountryHolder(v : View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var countryname : TextView
        var countryregion : TextView
        var countrysubregion : TextView
        var view : View = v
        var country : CountryListData? = null

        init {
            countryname = view.findViewById(R.id.country_name)
            countryregion = view.findViewById(R.id.country_region)
            countrysubregion = view.findViewById(R.id.country_subregion)
            v.setOnClickListener(this)
        }

        fun addText(country: CountryListData) {
            this.country = country
            countryname.text = country.name
            countryregion.text = country.id.toString()
            countrysubregion.text = country.iso2
        }

        override fun onClick(p0: View?) {
            val toast = Toast.makeText(view.context, country?.name.toString(), Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val row = inflater.inflate(R.layout.country_row, parent, false)
        return CountryHolder(row)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        val country = countryList[position]
        holder.addText(country)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }
}