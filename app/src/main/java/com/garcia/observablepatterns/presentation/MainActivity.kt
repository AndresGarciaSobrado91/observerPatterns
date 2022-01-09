package com.garcia.observablepatterns.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.garcia.observablepatterns.databinding.ActivityMainBinding
import com.garcia.observablepatterns.factory.ViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var  viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this,viewModelFactory)[MainViewModel::class.java]

        with(binding) {
            btnLiveData.setOnClickListener {
                viewModel.triggerLiveData()
            }

            btnStateFlow.setOnClickListener {
                viewModel.triggerStateFlow()
            }

            btnFlow.setOnClickListener {
                lifecycleScope.launchWhenCreated {
                    viewModel.triggerFlow().collectLatest {
                        binding.tvFlow.text = it
                    }
                }
            }

            btnSharedFlow.setOnClickListener {
                viewModel.triggerSharedFLow()
            }
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.liveData.observe(this){
            binding.tvLiveData.text = it
        }

        // flows can be HOTFLOW (it emits values even if there are no collectors) and COLDFLOW (does not emit any value if there is no collector)
        // StateFlow is a HOTFLOW
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                binding.tvStateFlow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    LENGTH_LONG
                ).show()
            }
        }

        // ShareFlow is for SINGLE EVENTS
        // ShareFlow is a HOTFLOW
        lifecycleScope.launchWhenStarted {
            viewModel.sharedFLow.collectLatest {
                binding.tvSharedFlow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    LENGTH_LONG
                ).show()
            }
        }

        // on activity recreated, we get the last values from liveData and stateFlow, while normal flow does not save a state, its a single use observable.

        // a stateFlow does not emit the same previous value again, but when activity recreated > we collect the last value again
        // a sharedFlow is the opposite of stateFlow (can emit same value again, and we don't collect the last value on activity recreated)
    }
}