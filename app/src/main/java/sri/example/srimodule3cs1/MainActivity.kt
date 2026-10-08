package sri.example.srimodule3cs1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etPlayerName: EditText
    private lateinit var btnStartQuiz: Button
    private lateinit var tvInstructions: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPlayerName = findViewById(R.id.etPlayerName)
        btnStartQuiz = findViewById(R.id.btnStartQuiz)
        tvInstructions = findViewById(R.id.tvInstructions)

        btnStartQuiz.setOnClickListener {
            val playerName = etPlayerName.text.toString().trim()

            if (playerName.isNotEmpty()) {
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("PLAYER_NAME", playerName)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}