package kz.tutorial.jsonplaceholdertypicode.presentation.profile

import UserPostAdapter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Post
import kz.tutorial.jsonplaceholdertypicode.presentation.register.RetrofitClient
import kz.tutorial.jsonplaceholdertypicode.presentation.register.TokenManager
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.ClickListener
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.SpaceItemDecoration

class UserProfileFragment : Fragment() {
    lateinit var username: TextView
    lateinit var name: TextView
    lateinit var rvPosts: RecyclerView
    lateinit var postsAdapter: UserPostAdapter
    lateinit var myPosts: List<Post>
    lateinit var postCount: TextView
    lateinit var subButton: Button
    lateinit var isSubscribed: String
    lateinit var subscribersCount: TextView
    lateinit var subscriptionsCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initAdapter()
        val token = TokenManager.getToken(requireContext())

        setValues(token)
        initRecycler()

        subButton.setOnClickListener {
            handleSubscription(token)
        }
    }

    private fun initViews(view: View) {
        username = view.findViewById(R.id.titleUsername)
        name = view.findViewById(R.id.titleName)
        rvPosts = view.findViewById(R.id.rv_posts)
        postCount = view.findViewById(R.id.postsNumber)
        subButton = view.findViewById(R.id.subscribeButton)
        subscribersCount = view.findViewById(R.id.tv_followersNumber)
        subscriptionsCount = view.findViewById(R.id.tv_followingNumber)
    }

    private fun setValues(token: String?) {
        val tokenRequest = "Bearer $token"
        val userId = arguments?.getInt("user_id", 0)!!

        lifecycleScope.launch {
            try {
                val user = RetrofitClient.apiService.getUser(userId)
                myPosts = RetrofitClient.apiService.getPostsByUserId(userId).data
                isSubscribed = RetrofitClient.apiService.checkUserSubscription(userId, tokenRequest).data
                subscribersCount.setText(RetrofitClient.apiService.getSubscribers(userId).metadata.count.toString())
                subscriptionsCount.setText(RetrofitClient.apiService.getSubscriptions(userId).metadata.count.toString())
                postCount.text = myPosts.size.toString()
                username.text = user.username
                name.text = user.name

                postsAdapter.setData(myPosts)
                setButtonState()
            } catch (e: Exception) {
                Log.d("ProfileFragment", "Error: ${e.message}")
            }
        }
    }

    private fun handleSubscription(token: String?) {
        val tokenRequest = "Bearer $token"
        val userId = arguments?.getInt("user_id", 0)!!

        lifecycleScope.launch {
            try {
                if (isSubscribed == "Not subscribed") {
                    RetrofitClient.apiService.subscribe(userId, tokenRequest)
                    isSubscribed = "Subscribed"
                } else {
                    RetrofitClient.apiService.unsubscribe(userId, tokenRequest)
                    isSubscribed = "Not subscribed"
                }
                setButtonState() // Update button state after changing subscription status
            } catch (e: Exception) {
                Log.d("ProfileFragment sub", "Error: ${e.message}")
            }
        }
    }

    private fun setButtonState() {
        if (isSubscribed == "Not subscribed") {
            subButton.setTextColor(Color.parseColor("#000000"))
            subButton.text = "Subscribe"
            subButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), androidx.cardview.R.color.cardview_light_background)
        } else {
            subButton.setTextColor(Color.parseColor("#FFFFFF"))
            subButton.text = "Unsubscribe"
            subButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.main_01)
        }

        val userId = arguments?.getInt("user_id", 0)!!
        lifecycleScope.launch {
            subscribersCount.setText(RetrofitClient.apiService.getSubscribers(userId).metadata.count.toString())
            subscriptionsCount.setText(RetrofitClient.apiService.getSubscriptions(userId).metadata.count.toString())
        }
    }

    private fun initAdapter() {
        postsAdapter = UserPostAdapter(layoutInflater, requireContext(), findNavController())
        postsAdapter.listener = ClickListener {
            val action = UserProfileFragmentDirections.actionUserProfileFragmentToPostDetails(it.id)
            findNavController().navigate(action)
        }
    }

    private fun initRecycler() {
        val currentContext = context ?: return

        rvPosts.adapter = postsAdapter
        rvPosts.layoutManager = LinearLayoutManager(currentContext)

        val spaceItemDecoration = SpaceItemDecoration(verticalSpaceInDp = 8, horizontalSpaceInDp = 16)
        rvPosts.addItemDecoration(spaceItemDecoration)
    }
}
