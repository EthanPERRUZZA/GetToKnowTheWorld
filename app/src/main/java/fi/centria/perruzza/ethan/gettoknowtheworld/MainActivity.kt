package fi.centria.perruzza.ethan.gettoknowtheworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val selectCountryHomePage = SelectCountryHomePage()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, selectCountryHomePage)
        }
    }
}