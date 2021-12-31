package com.garcia.observablepatterns.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.garcia.observablepatterns.presentation.MainViewModel

class ViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel() as T
        throw IllegalAccessException("Unknown VM class")
    }
}