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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.Transaction
import com.example.capitalmanagement.model.User
import com.example.capitalmanagement.viewModel.MainViewModelFactory
import com.example.capitalmanagement.viewModel.RegisterViewModel
import com.example.capitalmanagement.viewModel.TransactionsViewModel
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

    private lateinit var alreadyHaveAnAccountTextView: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var viewModel: RegisterViewModel

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

        initUI(view)
        bindViewModel()

        return view
    }

    private fun initUI(view: View) {
        usernameEditText = view.findViewById(R.id.username_edit_text)
        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        alreadyHaveAnAccountTextView = view.findViewById(R.id.alredy_have_an_account_text_view)
        registerButton = view.findViewById(R.id.register_button)

        initListeners(view)
    }

    private fun initListeners(view: View) {
        alreadyHaveAnAccountTextView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.loginFragment)
        }
        registerButton.setOnClickListener {
            performRegister()
        }
    }

    private fun bindViewModel() {
        initViewModel()
        observeData()
    }

    private fun initViewModel() {
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
        viewModel.context = requireContext()
    }

    private fun observeData() {
        viewModel.isRegistered.observe(requireActivity(), Observer {
            if (it == true) {
                val intent = Intent(requireContext(), SecondActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }

    private fun performRegister() {
        Log.d("Register", "Register button clicked")

        if(emailEditText.text.toString().isEmpty() || passwordEditText.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.register(emailEditText.text.toString(), passwordEditText.text.toString(), usernameEditText.text.toString())

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