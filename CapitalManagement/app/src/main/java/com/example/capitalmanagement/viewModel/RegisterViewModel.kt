package com.example.capitalmanagement.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capitalmanagement.SecondActivity
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel: ViewModel() {

    var isRegistered = MutableLiveData<Boolean>()
    var context: Context? = null

    fun register(email: String, password: String, username: String) {
        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Register", "Successfully created user with uid: ${it.result.user?.uid}")
                saveUserToFirebaseDatabase(email, password)
            }
            .addOnFailureListener{
                Log.d("Register", "Failed to create user: ${it.message}")
                Toast.makeText(context, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebaseDatabase(email: String, username: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid.toString(), email, username)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Finally we saved the user to Firebase Database")

                addCashAccount()

                isRegistered.value = true

            }
            .addOnFailureListener {
                Log.d("Register", "Failed to save user to Firebase Database: ${it.message}")
                Toast.makeText(context, "Failed to save user to Firebase Database: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addCashAccount() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/accounts")
        val childRef: DatabaseReference = ref.child("${ref.push().key}")

        val account = Account("Cash", "cash", 0)

        childRef.setValue(account)
            .addOnSuccessListener {
                Log.d("Add account", "Finally we saved the account to Firebase Database")
                Toast.makeText(context, "Finally we saved the account to Firebase Database", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("Add account", "Failed to save account to Firebase Database: ${it.message}")
                Toast.makeText(context, "Failed to save account to Firebase Database: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}