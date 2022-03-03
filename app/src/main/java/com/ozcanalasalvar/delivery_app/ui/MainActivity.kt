package com.ozcanalasalvar.delivery_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ozcanalasalvar.delivery_app.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    var counter = 0
    private var delay = 0L

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable // reference to the runnable object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            counter++
            viewModel.sendDamageEvent(counter)
            handler.postDelayed(runnable, delay)
        }

        lifecycleScope.launch {
            viewModel.timerEvent.collect {
                when (it) {
                    is MainViewModel.TimerEvent.StartTimer -> {
                        delay = it.delay
                        handler.post(runnable)

                    }
                    MainViewModel.TimerEvent.StopTimer -> {
                        handler.removeCallbacks(runnable)
                    }
                }
            }

        }
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


}
