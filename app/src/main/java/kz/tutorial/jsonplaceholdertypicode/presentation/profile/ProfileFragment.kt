package kz.tutorial.jsonplaceholdertypicode.presentation.profile

import android.content.Intent
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
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Post
import kz.tutorial.jsonplaceholdertypicode.domain.request.EditUserRequest
import kz.tutorial.jsonplaceholdertypicode.presentation.MainActivity
import kz.tutorial.jsonplaceholdertypicode.presentation.register.RetrofitClient
import kz.tutorial.jsonplaceholdertypicode.presentation.register.TokenManager
import org.json.JSONObject

class ProfileFragment : Fragment() {
    lateinit var username: TextView
    lateinit var name: TextView
    lateinit var changeUsername: EditText
    lateinit var changeName: EditText
    lateinit var changeLastname: EditText
    lateinit var changeEmail: EditText
    lateinit var changePassword: EditText
    lateinit var oldPassword: EditText
    lateinit var btnEditProfile: Button
    lateinit var btnLogout: Button
    lateinit var myPosts : List<Post>
    lateinit var postCount: TextView
    lateinit var subscribersCount: TextView
    lateinit var subscriptionsCount: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        initViews(view)
        val token = TokenManager.getToken(requireContext())
        setValues(token)

        val request_token = "Bearer $token"
        btnEditProfile.setOnClickListener{
            val request = EditUserRequest(changeUsername.text.toString(),
                oldPassword.text.toString(), changePassword.text.toString(),
                changeName.text.toString(), changeLastname.text.toString(),
                changeEmail.text.toString())
            Log.d("edit profile request - ", request.toString())
            Log.d("token: - ", request_token)

            lifecycleScope.launch {
                val response = try {
                    RetrofitClient.apiService.editUser(request_token, request)
                } catch (e: Exception) {
                    Log.d("ProfileFragment", "Error: ${e.message}")
                    return@launch
                }
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Successfully edited profile!",
                        Toast.LENGTH_LONG
                    ).show()
                    setValues(token)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Wrong password",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("ProfileFragment", "Error: ${response.errorBody()?.string()}")
                }
            }
        }

        btnLogout.setOnClickListener{
            TokenManager.clearToken(requireContext())

            Toast.makeText(
                requireContext(),
                "Logged out",
                Toast.LENGTH_LONG
            ).show()

            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()


        }

    }

    private fun initViews(view: View) {
        username = view.findViewById(R.id.titleUsername)
        name = view.findViewById(R.id.titleName)
        changeUsername = view.findViewById(R.id.profileUsername)
        changeName = view.findViewById(R.id.profileName)
        changeLastname = view.findViewById(R.id.profileLastname)
        changeEmail = view.findViewById(R.id.profileEmail)
        changePassword = view.findViewById(R.id.profilePassword)
        oldPassword = view.findViewById(R.id.profileOldPassword)
        btnEditProfile = view.findViewById(R.id.profileButton)
        btnLogout = view.findViewById(R.id.subscribeButton)
        postCount = view.findViewById(R.id.postsNumber)
        subscribersCount = view.findViewById(R.id.tv_followersNumber)
        subscriptionsCount = view.findViewById(R.id.tv_followingNumber)
    }

    private fun setValues(token: String?){
        changePassword.setText("")
        oldPassword.setText("")
        val userId = JSONObject(TokenManager.decodeToken(token)).getString("username").toInt()


        lifecycleScope.launch {
            val response = try {
                val user = RetrofitClient.apiService.getUser(userId)
                myPosts = RetrofitClient.apiService.getPostsByUserId(userId).data
                subscribersCount.text = RetrofitClient.apiService.getSubscribers(userId).metadata.count.toString()
                subscriptionsCount.text = RetrofitClient.apiService.getSubscriptions(userId).metadata.count.toString()
                postCount.text = myPosts.size.toString()
                username.text = user.username
                name.text = user.name
                changeUsername.setText(user.username)
                changeName.setText(user.name)
                changeLastname.setText(user.LastName)
                changeEmail.setText(user.email)
            } catch (e: Exception) {
                Log.d("ProfileFragment", "Error: ${e.message}")
                return@launch
            }
        }
    }
}
