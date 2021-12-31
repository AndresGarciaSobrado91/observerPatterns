package com.garcia.observablepatterns.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _liveData = MutableLiveData("LiveData: $DEFAULT_VALUE")
    val liveData: LiveData<String> = _liveData

    private val _stateFlow = MutableStateFlow("StateFLow: $DEFAULT_VALUE")
    val stateFlow = _stateFlow.asStateFlow()

    // normal flow is not defined here

    private val _sharedFlow = MutableSharedFlow<String>() // as a normal flow, it hasn't got a state, so initial value is not required
    val sharedFLow = _sharedFlow.asSharedFlow()

    fun triggerLiveData() {
        _liveData.value = "LiveData"
    }

    fun triggerStateFlow() {
        _stateFlow.value = "StateFlow"
    }

    fun triggerFlow(): Flow<String>{
        return flow {
            // here we can run suspend functions
            repeat(5) {
                emit("Item $it")
                delay(1000L)
            }
        }
    }

    fun triggerSharedFLow(){
        viewModelScope.launch {
            // here we can run suspend functions
            _sharedFlow.emit("SharedFlow")
        }
    }

    companion object{
        const val DEFAULT_VALUE = "My default value"
    }
}