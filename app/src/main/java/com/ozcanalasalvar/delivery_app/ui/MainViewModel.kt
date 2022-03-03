package com.ozcanalasalvar.delivery_app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ozcanalasalvar.delivery_app.data.local.pref.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {


    private val timerEventChannel = Channel<TimerEvent>()
    val timerEvent = timerEventChannel.receiveAsFlow()

    private val damage = MutableStateFlow(0)

    private val ds = -1

    val hasDamage = damage.asLiveData()

    init {
        observePrefData()
    }


    private fun startTimer(delay: Int) = viewModelScope.launch {
        timerEventChannel.send(TimerEvent.StartTimer(delay.toLong()))
    }

    fun stopTimer() = viewModelScope.launch {
        timerEventChannel.send(TimerEvent.StopTimer)
    }

    private fun observePrefData() = viewModelScope.launch {
        preferencesManager.dsFlow.collect {
            if (it > 0 && ds != it) {
                startTimer(it)
            }

        }
    }

    fun sendDamageEvent(counter: Int) {
        damage.value = counter
    }


    sealed class TimerEvent {
        data class StartTimer(val delay: Long) : TimerEvent()
        object StopTimer : TimerEvent()
    }

}