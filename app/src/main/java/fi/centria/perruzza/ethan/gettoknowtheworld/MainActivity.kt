package fi.centria.perruzza.ethan.gettoknowtheworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    lateinit var buttonPlay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonPlay = findViewById(R.id.button_play)

        val selectCountryHomePage = SelectCountryHomePage()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, selectCountryHomePage)
        }

        buttonPlay.setOnClickListener{

        }
    }
}