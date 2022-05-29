package com.techmania.mathe_game

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.preference.PreferenceManager
import com.techmania.mathe_game.helpers.DBHelper
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class AdditionActivity : AppCompatActivity() {
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
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addition)

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
        val numberOne = numGenerator.nextInt(100f.pow(difficultyLevel).roundToInt())
        val numberTwo = numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt())

        questionField.text =
            StringBuilder().append("$numberOne").append(" + ").append("$numberTwo").toString()

        val buttonArray = arrayOf(
            buttonSolutionOne,
            buttonSolutionTwo,
            buttonSolutionThree
        )

        correctButton = numGenerator.nextInt().mod(3)

        buttonArray[correctButton].text = numberOne.plus(numberTwo).toString()
        buttonArray[(correctButton.plus(1).mod(3))].text =
            (numberOne.plus(numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt()))).toString()
        buttonArray[(correctButton.plus(2).mod(3))].text =
            (numberOne.plus(numGenerator.nextInt(10f.pow(difficultyLevel).roundToInt()))).toString()

        while (buttonSolutionOne.text.equals(buttonSolutionTwo.text) || buttonSolutionOne.text.equals(
                buttonSolutionThree.text
            ) || buttonSolutionTwo.text.equals(buttonSolutionThree.text)
        ) {
            buttonArray[(correctButton.plus(1)).mod(3)].text = (numberOne.plus(
                numGenerator.nextInt(
                    10f.pow(difficultyLevel).roundToInt()
                )
            )).toString()
            buttonArray[(correctButton.plus(2)).mod(3)].text = (numberTwo.plus(
                numGenerator.nextInt(
                    10f.pow(difficultyLevel).roundToInt()
                )
            )).toString()

        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
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
            buttonLogic(0)
        }
        buttonSolutionTwo.setOnClickListener {
            buttonLogic(1)
        }
        buttonSolutionThree.setOnClickListener {
            buttonLogic(2)
        }
    }

    private fun buttonLogic(correct:Int) {
        if (correctButton == correct) {
            scoreValue.text = (scoreValue.text.toString().toInt().plus(timeLeft)).toString()
        } else {
            reduceLives()
        }
        if (lives >= 1) {
            countDownTimer.cancel()
            countDownTimer.start()
        }
        playSound(R.raw.blob)

        generateQuestion()
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
                intent.putExtra("Gamemode", "Addition")
                intent.putExtra("Score", scoreValue.text)
                startActivity(intent) //result activity will open
            }
        }
        vibratePhone()
    }

    private fun vibratePhone() {
        if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("vibration", false)) {
            val vibrator = applicationContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }
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

                if (timeLeft == 5L && PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("sound", false)) {
                    playSound(R.raw.time_bomb_6sec)
                }
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

    private fun playSound(resid:Int) {
        if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("sound", false)) {
            mediaPlayer = MediaPlayer.create(this, resid)
            mediaPlayer.start()
        }
    }
}