package com.techmania.mathe_game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.techmania.mathe_game.helpers.DBHelper
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class MultiplicationActivity : AppCompatActivity() {
    private var difficultyLevel = 1  //get difficulty level from main activity as intent ?!
    private var correctButton = -1
    private var lives = 3
    private var timer = 0L
    private var timeLeft = 0L
    private lateinit var buttonSolutionOne: Button
    private lateinit var buttonSolutionTwo: Button
    private lateinit var buttonSolutionThree: Button
    private lateinit var timerValue: TextView
    private lateinit var scoreValue: TextView
    private lateinit var livesImage: ImageView
    private lateinit var questionField: TextView

    private lateinit var db: DBHelper
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplication)

        db = DBHelper(applicationContext)
        timer = 61000

        startTimer()
        initViews()
        initListeners()
        generateQuestion()
        hideSystemBars()
    }

    private fun generateQuestion() {
        val numGenerator = Random(System.currentTimeMillis())
        var numberOne = numGenerator.nextInt(10f.pow(difficultyLevel - 1).roundToInt())
        var numberTwo = numGenerator.nextInt(10f.pow(difficultyLevel - 1).roundToInt())


        questionField.text =
            StringBuilder().append("$numberOne").append(" * ").append("$numberTwo").toString()

        val buttonArray = arrayOf(
            buttonSolutionOne,
            buttonSolutionTwo,
            buttonSolutionThree
        )


        while (numberOne == 0 || numberTwo == 0) { //reassign values if they are equal to 0 (we don't want 8 + 0 as a calculation)
            numberOne = numGenerator.nextInt(100f.pow(difficultyLevel - 1).roundToInt())
            numberTwo = numGenerator.nextInt(10f.pow(difficultyLevel - 1).roundToInt())
        }


        correctButton = numGenerator.nextInt().mod(3)//random Button = correct

        buttonArray[correctButton].text =
            numberOne.times(numberTwo).toString()//richtige Ergebnis in den Button
        buttonArray[correctButton].text =
            numberOne.times(numberTwo).toString()//richtige Ergebnis in den Button
        buttonArray[(correctButton.plus(1).mod(3))].text =
            (numberOne.times(
                numGenerator.nextInt(
                    10f.pow(difficultyLevel - 1).roundToInt()
                )
            )).toString()
        buttonArray[(correctButton.plus(2).mod(3))].text =
            (numberTwo.times(
                numGenerator.nextInt(
                    10f.pow(difficultyLevel - 1).roundToInt()
                )
            )).toString()

        while (buttonSolutionOne.text.equals(buttonSolutionTwo.text) || buttonSolutionOne.text.equals(
                buttonSolutionThree.text
            ) || buttonSolutionTwo.text.equals(buttonSolutionThree.text)
        ) {
            buttonArray[(correctButton.plus(1)).mod(3)].text = (numberOne.times(
                numGenerator.nextInt(
                    10f.pow(difficultyLevel - 1).roundToInt()
                )
            )).toString()
            buttonArray[(correctButton.plus(2)).mod(3)].text = (numberTwo.times(
                numGenerator.nextInt(
                    10f.pow(difficultyLevel - 1).roundToInt()
                )
            )).toString()
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    private fun initViews() {
        buttonSolutionOne = findViewById(R.id.buttonSolutionOne)
        buttonSolutionTwo = findViewById(R.id.buttonSolutionTwo)
        buttonSolutionThree = findViewById(R.id.buttonSolutionThree)

        //other value fields
        timerValue = findViewById(R.id.textViewTimeNumber)
        scoreValue = findViewById(R.id.textViewScoreNumber)

        //question field
        questionField = findViewById(R.id.textViewQuestion)

        //life image view
        livesImage = findViewById(R.id.livesImage)
    }

    private fun initListeners() {
        /*
        Initializes listeners
         */
        buttonSolutionOne.setOnClickListener {
            if (correctButton == 0) {
                scoreValue.text = (scoreValue.text.toString().toInt().plus(timeLeft)).toString()
            } else {
                reduceLives()
            }
            if (lives >= 1) {
                countDownTimer.cancel()
                countDownTimer.start()
            }
            generateQuestion()
        }
        buttonSolutionTwo.setOnClickListener {
            if (correctButton == 1) {
                scoreValue.text = (scoreValue.text.toString().toInt().plus(timeLeft)).toString()
            } else {
                reduceLives()
            }
            if (lives >= 1) {
                countDownTimer.cancel()
                countDownTimer.start()
            }
            generateQuestion()
        }
        buttonSolutionThree.setOnClickListener {
            if (correctButton == 2) {
                scoreValue.text = (scoreValue.text.toString().toInt().plus(timeLeft)).toString()
            } else {
                reduceLives()
            }
            if (lives >= 1) {
                countDownTimer.cancel()
                countDownTimer.start()
            }
            generateQuestion()
        }
    }

    private fun reduceLives() {
        /*
        Sets ImageView Source depending on lives left
         */
        when (--lives) {
            2 -> livesImage.setImageResource(R.drawable.lives2)
            1 -> livesImage.setImageResource(R.drawable.lives1)
            else -> {
                livesImage.setImageResource(R.drawable.lives0)

                countDownTimer.cancel()

                buttonSolutionOne.isEnabled = false
                buttonSolutionTwo.isEnabled = false
                buttonSolutionThree.isEnabled = false

                db.addHighScore(scoreValue.text.toString().toInt())

                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("Gamemode", "Multiplication")
                intent.putExtra("Score", scoreValue.text)
                startActivity(intent) //result activity will open
            }
        }
        vibratePhone()
    }

    private fun vibratePhone() {
        val vibrator = applicationContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    private fun startTimer() {
        /*
        Timer function -> Counts down from 60 secs to 0 and then disables buttons
         */
        countDownTimer = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerValue.text = (millisUntilFinished / 1000).toString()
                timeLeft = millisUntilFinished / 1000
            }

            //gets called when timer finishes
            override fun onFinish() {
                if (lives > 1) {
                    Toast.makeText(
                        applicationContext,
                        "YOU LOST A LIFE",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                this.start()
                reduceLives()
                generateQuestion()
            }
        }.start()
    }
}