package com.example.capitalmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkIsUserLoggedIn()
    }

    private fun checkIsUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}