package kz.tutorial.jsonplaceholdertypicode.presentation.register

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.request.LoginRequest
import kz.tutorial.jsonplaceholdertypicode.domain.request.RegisterRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var firstname: EditText
    lateinit var lastname: EditText
    lateinit var email: EditText
    lateinit var repeatPassword: EditText
    lateinit var btnRegister: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        btnRegister.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()
            val firstnameText = firstname.text.toString()
            val lastnameText = lastname.text.toString()
            val emailText = email.text.toString()
            val repeatPasswordText = repeatPassword.text.toString()

            if (passwordText != repeatPasswordText) {
                Toast.makeText(
                    requireContext(),
                    "Passwords do not match",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            //TODO add conditions to password. For example, check if the length is greater than 7
            //TODO optional: check the email validity

            val request = RegisterRequest(
                usernameText, firstnameText, lastnameText,
                emailText, passwordText
            )

            lifecycleScope.launch {
                val response = try {
                    RetrofitClient.apiService.register(request)
                } catch (e: Exception) {
                    Log.d("RegisterFragment", "Error: ${e.message}")
                    return@launch
                }

                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Successfully registered!",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()

                } else {
                    Log.d("LoginFragment", "Error: ${response.errorBody()?.string()}")
                }
            }
        }


    }

    private fun initViews(view: View) {
        username = view.findViewById(R.id.username_input)
        password = view.findViewById(R.id.password_input)
        firstname = view.findViewById(R.id.firstname_input)
        lastname = view.findViewById(R.id.lastname_input)
        email = view.findViewById(R.id.email_input)
        repeatPassword = view.findViewById(R.id.repeat_password_input)
        btnRegister = view.findViewById(R.id.register_btn)
    }
}