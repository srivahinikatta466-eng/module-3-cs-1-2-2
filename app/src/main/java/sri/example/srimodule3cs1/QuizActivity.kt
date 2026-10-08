package sri.example.srimodule3cs1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvQuestionNumber: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var btnNext: Button

    private lateinit var cardOption1: MaterialCardView
    private lateinit var cardOption2: MaterialCardView
    private lateinit var cardOption3: MaterialCardView
    private lateinit var cardOption4: MaterialCardView

    private lateinit var rbOption1: RadioButton
    private lateinit var rbOption2: RadioButton
    private lateinit var rbOption3: RadioButton
    private lateinit var rbOption4: RadioButton

    private var playerName: String = ""
    private var currentQuestionIndex = 0
    private var score = 0
    private var timer: CountDownTimer? = null
    private val timePerQuestion: Long = 15000 // 15 seconds

    private var selectedOptionIndex: Int = -1
    private var isAnswerSubmitted: Boolean = false

    private val questions = listOf(
        Question(
            "What is the capital of France?",
            listOf("London", "Berlin", "Paris", "Madrid"),
            2
        ),
        Question(
            "Which planet is known as the Red Planet?",
            listOf("Venus", "Mars", "Jupiter", "Saturn"),
            1
        ),
        Question(
            "What is 5 + 7?",
            listOf("10", "11", "12", "13"),
            2
        ),
        Question(
            "Who wrote 'Romeo and Juliet'?",
            listOf("Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"),
            1
        ),
        Question(
            "What is the largest ocean on Earth?",
            listOf("Atlantic", "Indian", "Arctic", "Pacific"),
            3
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        initializeViews()
        setupListeners()

        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"
        loadQuestion()
    }

    private fun initializeViews() {
        tvQuestion = findViewById(R.id.tvQuestion)
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber)
        tvTimer = findViewById(R.id.tvTimer)
        tvFeedback = findViewById(R.id.tvFeedback)
        btnNext = findViewById(R.id.btnNext)

        cardOption1 = findViewById(R.id.cardOption1)
        cardOption2 = findViewById(R.id.cardOption2)
        cardOption3 = findViewById(R.id.cardOption3)
        cardOption4 = findViewById(R.id.cardOption4)

        rbOption1 = findViewById(R.id.rbOption1)
        rbOption2 = findViewById(R.id.rbOption2)
        rbOption3 = findViewById(R.id.rbOption3)
        rbOption4 = findViewById(R.id.rbOption4)
    }

    private fun setupListeners() {
        cardOption1.setOnClickListener { selectOption(0) }
        cardOption2.setOnClickListener { selectOption(1) }
        cardOption3.setOnClickListener { selectOption(2) }
        cardOption4.setOnClickListener { selectOption(3) }

        btnNext.setOnClickListener {
            if (!isAnswerSubmitted) {
                if (selectedOptionIndex != -1) {
                    showFeedback()
                } else {
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                    loadQuestion()
                } else {
                    navigateToResult()
                }
            }
        }
    }

    private fun selectOption(index: Int) {
        if (isAnswerSubmitted) return

        selectedOptionIndex = index

        // Update RadioButtons
        rbOption1.isChecked = (index == 0)
        rbOption2.isChecked = (index == 1)
        rbOption3.isChecked = (index == 2)
        rbOption4.isChecked = (index == 3)

        // Update Card Strokes/Highlights
        updateCardSelection(index)
    }

    private fun updateCardSelection(selectedIndex: Int) {
        val cards = listOf(cardOption1, cardOption2, cardOption3, cardOption4)
        val primaryColor = ContextCompat.getColorStateList(this, R.color.primary)

        cards.forEachIndexed { index, card ->
            if (index == selectedIndex) {
                card.setStrokeColor(primaryColor)
                card.strokeWidth = 6 // Thicker stroke for selection
                card.cardElevation = 12f
            } else {
                card.setStrokeColor(Color.TRANSPARENT)
                card.strokeWidth = 2
                card.cardElevation = 2f
            }
        }
    }

    private fun loadQuestion() {
        timer?.cancel()
        isAnswerSubmitted = false
        selectedOptionIndex = -1

        val question = questions[currentQuestionIndex]
        tvQuestion.text = question.text
        tvQuestionNumber.text = "Question ${currentQuestionIndex + 1}/${questions.size}"

        rbOption1.text = question.options[0]
        rbOption2.text = question.options[1]
        rbOption3.text = question.options[2]
        rbOption4.text = question.options[3]

        rbOption1.isChecked = false
        rbOption2.isChecked = false
        rbOption3.isChecked = false
        rbOption4.isChecked = false

        // Reset card styles
        listOf(cardOption1, cardOption2, cardOption3, cardOption4).forEach { card ->
            card.setStrokeColor(Color.TRANSPARENT)
            card.strokeWidth = 2
            card.cardElevation = 4f
        }

        tvFeedback.visibility = View.GONE
        btnNext.text = "Submit"

        enableOptions(true)
        startTimer()
    }

    private fun enableOptions(enabled: Boolean) {
        cardOption1.isEnabled = enabled
        cardOption2.isEnabled = enabled
        cardOption3.isEnabled = enabled
        cardOption4.isEnabled = enabled
    }

    private fun showFeedback() {
        timer?.cancel()
        isAnswerSubmitted = true
        enableOptions(false)

        val currentQuestion = questions[currentQuestionIndex]
        val correctIndex = currentQuestion.correctAnswer

        val cards = listOf(cardOption1, cardOption2, cardOption3, cardOption4)
        val radioButtons = listOf(rbOption1, rbOption2, rbOption3, rbOption4)

        val greenColor = Color.parseColor("#4CAF50")
        val redColor = Color.parseColor("#F44336")

        // Highlight correct and incorrect
        cards[correctIndex].setStrokeColor(greenColor)
        cards[correctIndex].strokeWidth = 8
        radioButtons[correctIndex].buttonTintList = android.content.res.ColorStateList.valueOf(greenColor)

        if (selectedOptionIndex != -1) {
            if (selectedOptionIndex == correctIndex) {
                score++
                tvFeedback.text = "✓ Correct! Great job."
                tvFeedback.setTextColor(greenColor)
            } else {
                cards[selectedOptionIndex].setStrokeColor(redColor)
                cards[selectedOptionIndex].strokeWidth = 8
                radioButtons[selectedOptionIndex].buttonTintList = android.content.res.ColorStateList.valueOf(redColor)
                tvFeedback.text = "✗ Incorrect!\nCorrect Answer: ${currentQuestion.options[correctIndex]}"
                tvFeedback.setTextColor(redColor)
            }
        } else {
            tvFeedback.text = "Time's Up!\nCorrect Answer: ${currentQuestion.options[correctIndex]}"
            tvFeedback.setTextColor(redColor)
        }

        tvFeedback.visibility = View.VISIBLE
        btnNext.text = if (currentQuestionIndex < questions.size - 1) "Next Question" else "View Results"
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timePerQuestion, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                tvTimer.text = "Time: ${secondsRemaining}s"
            }

            override fun onFinish() {
                tvTimer.text = "Time's up!"
                if (!isAnswerSubmitted) {
                    selectedOptionIndex = -1 // No selection
                    showFeedback()
                }
            }
        }.start()
    }

    private fun navigateToResult() {
        timer?.cancel()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("PLAYER_NAME", playerName)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL_QUESTIONS", questions.size)
        startActivity(intent)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentQuestionIndex", currentQuestionIndex)
        outState.putInt("score", score)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0)
        score = savedInstanceState.getInt("score", 0)
        loadQuestion()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    data class Question(
        val text: String,
        val options: List<String>,
        val correctAnswer: Int
    )
}