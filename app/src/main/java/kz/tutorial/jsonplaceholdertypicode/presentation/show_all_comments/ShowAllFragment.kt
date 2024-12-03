package kz.tutorial.jsonplaceholdertypicode.presentation.show_all_comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.constants.POST_ID_KEY
import kz.tutorial.jsonplaceholdertypicode.domain.request.AddCommentRequest
import kz.tutorial.jsonplaceholdertypicode.presentation.comments.CommentsAdapter
import kz.tutorial.jsonplaceholdertypicode.presentation.extensions.openEmailWithAddress

import kz.tutorial.jsonplaceholdertypicode.presentation.posts.PostsFragmentDirections
import kz.tutorial.jsonplaceholdertypicode.presentation.register.RetrofitClient
import kz.tutorial.jsonplaceholdertypicode.presentation.register.TokenManager
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.ClickListener
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.SpaceItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ShowAllFragment : Fragment() {

    private val vm: ShowAllViewModel by viewModel {
        parametersOf(arguments?.getInt(POST_ID_KEY, 0))
    }

    lateinit var rvComments: RecyclerView
    lateinit var commentsAdapter: CommentsAdapter
    lateinit var etAddComment: EditText
    lateinit var btnAddComment: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        initAdapter()
        initRecycler()
        initObservers()

        btnAddComment.setOnClickListener {
            val content = etAddComment.text.toString()
            if (content.length == 0) {
                Toast.makeText(
                    requireContext(),
                    "Comment must not be an empty!",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            val postId = arguments?.getInt(POST_ID_KEY)
            val token = TokenManager.getToken(requireContext())
            val tokenRequest = "Bearer $token"

            val request = AddCommentRequest(content, postId)

            lifecycleScope.launch {
                val response = try {
                    RetrofitClient.apiService.addComment(tokenRequest, request)
                } catch (e: Exception) {
                    Log.d("PostDetailsFragment", "Error: ${e.message}")
                    return@launch
                }
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Successfully added a comment!",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                    findNavController().navigate(PostsFragmentDirections.toPostDetails(postId!!))
                } else {
                    Log.d("PostDetailsFragment", "Error: ${response.errorBody()?.string()}")
                }
            }
        }
    }

    private fun initViews(view: View) {
        rvComments = view.findViewById(R.id.rv_show_all)
        etAddComment = view.findViewById(R.id.et_add_comment)
        btnAddComment = view.findViewById(R.id.add_comment_btn)
    }

    private fun initAdapter() {
        commentsAdapter = CommentsAdapter(layoutInflater, findNavController(), true)
    }

    private fun initRecycler() {
        val currentContext = context ?: return

        rvComments.adapter = commentsAdapter
        rvComments.layoutManager = LinearLayoutManager(currentContext)

        val spaceItemDecoration =
            SpaceItemDecoration(verticalSpaceInDp = 8, horizontalSpaceInDp = 16)
        rvComments.addItemDecoration(spaceItemDecoration)
    }
    private fun initObservers() {
        vm.commentsLiveData.observe(viewLifecycleOwner) { comments ->
            commentsAdapter.submitList(comments)
        }
    }

}