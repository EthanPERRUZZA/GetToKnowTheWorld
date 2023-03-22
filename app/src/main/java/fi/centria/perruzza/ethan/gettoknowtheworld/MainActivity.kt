package fi.centria.perruzza.ethan.gettoknowtheworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.commit
import fi.centria.perruzza.ethan.gettoknowtheworld.game.GameActivity

class MainActivity : AppCompatActivity() {
    lateinit var buttonPlay: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // link ui elements
        buttonPlay = findViewById(R.id.button_play)

        // launch the first fragment of the discover part
        val selectCountryHomePage = SelectCountryHomePage()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, selectCountryHomePage)
        }

        // if click on game button then switch to game activity
        buttonPlay.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}