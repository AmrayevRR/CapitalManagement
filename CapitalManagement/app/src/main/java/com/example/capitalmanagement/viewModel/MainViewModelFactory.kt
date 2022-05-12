package com.example.capitalmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class MainViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TransactionsViewModel::class.java)){
            return TransactionsViewModel() as T
        }
        else if (modelClass.isAssignableFrom(AddTransactionViewModel::class.java)){
            return AddTransactionViewModel() as T
        }
        else if (modelClass.isAssignableFrom(ExchangeRatesViewModel::class.java)){
            return ExchangeRatesViewModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}