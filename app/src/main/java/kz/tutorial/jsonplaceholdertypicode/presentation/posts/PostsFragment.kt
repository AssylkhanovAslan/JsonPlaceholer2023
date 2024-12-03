package kz.tutorial.jsonplaceholdertypicode.presentation.posts

import PostAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.ClickListener
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.SpaceItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostsFragment : Fragment() {

    private val vm: PostsViewModel by viewModel()
    private lateinit var rvPosts: ViewPager2
    private lateinit var adapter: PostAdapter
    private var currentPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initAdapter()
        initRecycler()
        initObservers()

        // Restore saved position if available
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition", 0)
        }
    }

    private fun initViews(view: View) {
        rvPosts = view.findViewById(R.id.viewPagerPosts)
    }

    private fun initAdapter() {
        adapter = PostAdapter(layoutInflater, requireContext(), findNavController())
        adapter.listener = ClickListener { post ->
            currentPosition = rvPosts.currentItem  // Save the current position
            findNavController().navigate(PostsFragmentDirections.toPostDetails(post.id))
        }
    }

    private fun initRecycler() {
        val currentContext = context ?: return
        rvPosts.adapter = adapter
        val spaceItemDecoration = SpaceItemDecoration(verticalSpaceInDp = 8, horizontalSpaceInDp = 8)
        rvPosts.addItemDecoration(spaceItemDecoration)
    }

    private fun initObservers() {
        vm.postsLiveData.observe(viewLifecycleOwner) { posts ->
            adapter.setData(posts)
            // Restore the saved position after setting data
            rvPosts.setCurrentItem(currentPosition, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentPosition", currentPosition)
    }
}
