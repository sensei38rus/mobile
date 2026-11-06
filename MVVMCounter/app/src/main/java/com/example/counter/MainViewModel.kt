package com.example.counter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val counter = MutableLiveData<Int>(0)

    fun onIncrementClicked() {
        counter.value = (counter.value ?: 0) + 1
    }
}