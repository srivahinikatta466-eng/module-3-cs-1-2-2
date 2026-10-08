package sri.example.srimodule3cs1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnPlayAgain: Button
    private lateinit var btnShare: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        tvResult = findViewById(R.id.tvResult)
        tvScore = findViewById(R.id.tvScore)
        tvMessage = findViewById(R.id.tvMessage)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
        btnShare = findViewById(R.id.btnShare)

        val playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"
        val score = intent.getIntExtra("SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 5)

        tvResult.text = "Congratulations $playerName!"
        tvScore.text = "Your Score: $score/$totalQuestions"

        val percentage = (score * 100) / totalQuestions
        tvMessage.text = when {
            percentage >= 80 -> "Excellent! You're a quiz master!"
            percentage >= 60 -> "Good job! Keep learning!"
            percentage >= 40 -> "Not bad! Practice makes perfect!"
            else -> "Better luck next time! Keep studying!"
        }

        btnPlayAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        btnShare.setOnClickListener {
            shareResult(playerName, score, totalQuestions)
        }
    }

    private fun shareResult(playerName: String, score: Int, totalQuestions: Int) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,
                "$playerName scored $score/$totalQuestions in the Quiz App! Can you beat this score?")
        }
        startActivity(Intent.createChooser(shareIntent, "Share your result"))
    }
}