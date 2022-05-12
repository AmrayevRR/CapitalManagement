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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.capitalmanagement.viewModel.LoginViewModel
import com.example.capitalmanagement.viewModel.MainViewModelFactory
import com.example.capitalmanagement.viewModel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        initUI(view)
        bindViewModel()

        return view
    }

    private fun initUI(view: View) {
        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        loginButton = view.findViewById(R.id.login_button)

        initListener()
    }

    private fun initListener() {
        loginButton.setOnClickListener {
            Log.d("Login", "Log in button clicked")

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }
    }

    private fun bindViewModel() {
        initViewModel()
        observeData()
    }

    private fun initViewModel() {
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    private fun observeData() {
        viewModel.isLoged.observe(requireActivity(), Observer {
            if (it == true) {
                val intent = Intent(requireContext(), SecondActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }

    private fun login() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Login", "Successfully login user with uid: ${it.result.user?.uid}")

                val intent = Intent(requireContext(), SecondActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("Login", "Failed to create user: ${it.message}")
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}