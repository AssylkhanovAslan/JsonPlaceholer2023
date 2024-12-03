package kz.tutorial.jsonplaceholdertypicode.presentation.add_post

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.request.AddPostRequest
import kz.tutorial.jsonplaceholdertypicode.presentation.register.RetrofitClient
import kz.tutorial.jsonplaceholdertypicode.presentation.register.TokenManager
import org.json.JSONObject

class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    lateinit var title: EditText
    lateinit var content: EditText
    lateinit var btnAddPost: Button
    lateinit var tvAuthor: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        var token = TokenManager.getToken(requireContext())
        val userId = JSONObject(TokenManager.decodeToken(token)).getString("username").toInt()
        lifecycleScope.launch {
            val response = try {
                tvAuthor.text = RetrofitClient.apiService.getUser(userId).username
            } catch (e: Exception) {
                Log.d("LoginFragment", "Error: ${e.message}")
                return@launch
            }
        }

        token = "Bearer $token"
        btnAddPost.setOnClickListener {
            val title = title.text.toString()
            val content = content.text.toString()

            val addPostRequest = AddPostRequest(title, content)
            lifecycleScope.launch {
                val response = try {
                    RetrofitClient.apiService.addPost(token, addPostRequest)
                } catch (e: Exception) {
                    Log.d("AddPostFragment", "Error: ${e.message}")
                    return@launch
                }

                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Successfully added new post!",
                        Toast.LENGTH_LONG
                    ).show()

                    findNavController().navigate(R.id.action_add_post_to_menu_posts)
                } else {
                    Log.d("LoginFragment", "Error: ${response.errorBody()?.string()}")
                }
            }
        }
    }



    private fun initViews(view: View) {
        title = view.findViewById(R.id.title_input)
        content = view.findViewById(R.id.content_input)
        btnAddPost = view.findViewById(R.id.add_post_btn)
        tvAuthor = view.findViewById(R.id.tv_author)
    }

}

