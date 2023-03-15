package fi.centria.perruzza.ethan.gettoknowtheworld

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CountryAdapter(context: Context, val countryList : ArrayList<CountryData>) : RecyclerView.Adapter<CountryAdapter.CountryHolder>() {
    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    class CountryHolder(v : View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var countryname : TextView
        var countryregion : TextView
        var countrysubregion : TextView
        var view : View = v
        var country : CountryData? = null

        init {
            countryname = view.findViewById(R.id.country_name)
            countryregion = view.findViewById(R.id.country_region)
            countrysubregion = view.findViewById(R.id.country_subregion)
            v.setOnClickListener(this)
        }

        fun addText(country: CountryData) {
            this.country = country
            countryname.text = country.countryname
            countryregion.text = country.countryregion
            countrysubregion.text = country.countrysubregion
        }

        override fun onClick(p0: View?) {
            val toast = Toast.makeText(view.context, country?.countryname.toString(), Toast.LENGTH_LONG)
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