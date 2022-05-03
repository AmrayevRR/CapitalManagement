package com.example.capitalmanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.Transaction
import com.example.capitalmanagement.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    lateinit var alreadyHaveAnAccountTextView: TextView
    lateinit var usernameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            // Navigate to the next page

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        usernameEditText = view.findViewById(R.id.username_edit_text)
        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        alreadyHaveAnAccountTextView = view.findViewById(R.id.alredy_have_an_account_text_view)
        registerButton = view.findViewById(R.id.register_button)

        alreadyHaveAnAccountTextView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.loginFragment)
        }
        registerButton.setOnClickListener {
            performRegister()
        }

        return view
    }

    private fun performRegister() {
        Log.d("Register", "Register button clicked")

        if(emailEditText.text.toString().isEmpty() || passwordEditText.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Register", "Successfully created user with uid: ${it.result.user?.uid}")
                saveUserToFirebaseDatabase()
            }
            .addOnFailureListener{
                Log.d("Register", "Failed to create user: ${it.message}")
                Toast.makeText(requireContext(), "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid.toString(), emailEditText.text.toString(), usernameEditText.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Finally we saved the user to Firebase Database")

                addCashAccount()

                val intent = Intent(requireContext(), SecondActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("Register", "Failed to save user to Firebase Database: ${it.message}")
                Toast.makeText(requireContext(), "Failed to save user to Firebase Database: ${it.message}", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "Finally we saved the account to Firebase Database", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("Add account", "Failed to save account to Firebase Database: ${it.message}")
                Toast.makeText(requireContext(), "Failed to save account to Firebase Database: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}