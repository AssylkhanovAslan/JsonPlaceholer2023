package kz.tutorial.jsonplaceholdertypicode.presentation.register

import android.content.Intent
import android.media.session.MediaSession.Token
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.request.LoginRequest
import kz.tutorial.jsonplaceholdertypicode.presentation.SecondActivity


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    lateinit var tvRegister: TextView
    lateinit var btnLogin: Button
    lateinit var username: EditText
    lateinit var password: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        TokenManager.clearToken(requireContext())
        if (TokenManager.getToken(requireContext()) != null) {
            startActivity(Intent(context,SecondActivity::class.java))
            requireActivity().finish()
            return
        }

        initViews(view)
        tvRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        btnLogin.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()

            val request = LoginRequest(username, password)

            lifecycleScope.launch {
                val response = try {
                    RetrofitClient.apiService.login(request)
                } catch (e: Exception) {
                    Log.d("LoginFragment", "Error: ${e.message}")
                    return@launch
                }

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        TokenManager.saveToken(requireContext(), token)
                        Toast.makeText(
                            requireContext(),
                            "Successfully logged in!",
                            Toast.LENGTH_LONG
                        ).show()
//                        findNavController().navigate(R.id.action_loginFragment_to_nav_second)
                        startActivity(Intent(context,SecondActivity::class.java))
                        requireActivity().finish()
                        Log.d("LoginFragment", "Token: $token")
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "The username or password is incorrect. Please, try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("LoginFragment", "Error: ${response.errorBody()?.string()}")
                }

            }
        }
    }

    private fun initViews(view: View) {
        tvRegister = view.findViewById(R.id.tv_register)
        btnLogin = view.findViewById(R.id.login_btn)
        username = view.findViewById(R.id.username_input)
        password = view.findViewById(R.id.password_input)
    }

}