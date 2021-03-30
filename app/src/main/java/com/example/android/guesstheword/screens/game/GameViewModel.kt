package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameViewModel : ViewModel()
{
    companion object
    {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 60000L
    }

    private val timer : CountDownTimer
    private var _elapsedTime = MutableLiveData<String>()
    val elapsedTime : LiveData<String>
    get() = _elapsedTime
    private var timerCounter : Int = COUNTDOWN_TIME.toInt()

    // The current word
        private var _word = MutableLiveData<String>()
        val word : LiveData<String>
        get() = _word
        // The current score
        private var _score = MutableLiveData<Int>()
        val score : LiveData<Int>
        get() = _score
        private var _eventGameFinished = MutableLiveData<Boolean>()
        val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

        // The list of words - the front of the list is the next word to guess
        private lateinit var wordList: MutableList<String>

        init
        {
            Timber.i("Game view model created")
            resetList()
            nextWord()
            _score.value = 0
            _eventGameFinished.value = false

            _elapsedTime.value = DateUtils.formatElapsedTime(COUNTDOWN_TIME.toLong())
            timer = object : CountDownTimer(COUNTDOWN_TIME , ONE_SECOND){
                override fun onTick(p0: Long) {
                    timerCounter--
                    //Timber.i("in on tick : p0 = ${(p0/1000).toString()}")
                    _elapsedTime.value = DateUtils.formatElapsedTime(p0/1000)
                }

                override fun onFinish() {
                    _eventGameFinished.value = true
                }
            }
            timer.start()
        }

        override fun onCleared() {
            super.onCleared()
            timer.cancel()
            Timber.i("Game view model cleared")
        }

        /** Methods for buttons presses **/

        fun onSkip() {
            _score.value = score.value?.minus(1)
            nextWord()
        }

        fun onCorrect() {
            _score.value = score.value?.plus(1)
            nextWord()
        }

        /**
         * Moves to the next word in the list
         */
        fun nextWord() {
            //Select and remove a word from the list
            if (wordList.isEmpty()) {
                //gameFinished()
                //_eventGameFinished.value = true
                resetList()
            }
            //else {
            _word.value = wordList.removeAt(0)
            //}
        }


        /**
         * Resets the list of words and randomizes the order
         */
        fun resetList() {
            wordList = mutableListOf(
                    "queen",
                    "hospital",
                    "basketball",
                    "cat",
                    "change",
                    "snail",
                    "soup",
                    "calendar",
                    "sad",
                    "desk",
                    "guitar",
                    "home",
                    "railway",
                    "zebra",
                    "jelly",
                    "car",
                    "crow",
                    "trade",
                    "bag",
                    "roll",
                    "bubble"
            )
            wordList.shuffle()
        }

        fun onGameFnishComplete()
        {
            _eventGameFinished.value = false
        }
}
