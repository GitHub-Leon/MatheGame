package com.techmania.mathe_game

import android.content.Context
import android.content.Intent
import android.os.*
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.techmania.mathe_game.helpers.DBHelper
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random


class SubtractionActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_subtraction)

        db = DBHelper(applicationContext)
        timer = 61000  //TODO: GET VALUE FROM INTENT VIA SETTINGS

        startTimer()
        initViews()
        initListeners()
        generateQuestion()
        hideSystemBars()
    }

    private fun generateQuestion() {
        /*
        Generates random solutions and a random question
         */
        val numGenerator: Random = Random(System.currentTimeMillis())
        val numberOne = numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt())
        val numberTwo = numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt())

        questionField.text = "$numberOne - $numberTwo" //set question with correct string

        //set correctButton and put the right value into it
        val buttonArray = arrayOf(
            buttonSolutionOne,
            buttonSolutionTwo,
            buttonSolutionThree
        ) //create array of solutionButtons
        correctButton = numGenerator.nextInt().mod(3) //get a random Button and set it as the correctButton
        buttonArray[correctButton].text = (numberOne.minus(numberTwo)).toString() //set the right value into the button

        buttonArray[(correctButton.plus(1)).mod(3)].text = (numberOne.minus(numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt()))).toString()
        buttonArray[(correctButton.plus(2)).mod(3)].text = (numberTwo.minus(numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt()))).toString()

        while(buttonSolutionOne.text.equals(buttonSolutionTwo.text) || buttonSolutionOne.text.equals(buttonSolutionThree.text) || buttonSolutionTwo.text.equals(buttonSolutionThree.text)) {
            buttonArray[(correctButton.plus(1)).mod(3)].text = (numberOne.minus(numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt()))).toString()
            buttonArray[(correctButton.plus(2)).mod(3)].text = (numberTwo.minus(numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt()))).toString()
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
        /*
        Set all Views that are needed
         */

        //solution buttons
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
        buttonSolutionOne.setOnClickListener(View.OnClickListener {
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
        })
        buttonSolutionTwo.setOnClickListener(View.OnClickListener {
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
        })
        buttonSolutionThree.setOnClickListener(View.OnClickListener {
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
        })
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
                intent.putExtra("Gamemode", "Subtraction")
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
                    Toast.makeText(applicationContext, "YOU LOST A FUCKING LIFE", Toast.LENGTH_SHORT).show()
                }
                this.start()
                reduceLives()
                generateQuestion()
            }
        }.start()
    }
}