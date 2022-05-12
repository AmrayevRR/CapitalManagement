package com.example.capitalmanagement.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capitalmanagement.SecondActivity
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    var isLoged = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Login", "Successfully login user with uid: ${it.result.user?.uid}")

                isLoged.value = true
            }
            .addOnFailureListener {
                Log.d("Login", "Failed to create user: ${it.message}")
            }
    }
}