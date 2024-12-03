package kz.tutorial.jsonplaceholdertypicode.presentation.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.domain.use_case.GetPostsUseCase
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Post

class PostsViewModel(private val getPostsUseCase: GetPostsUseCase) : ViewModel() {

    private val _postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val postsLiveData: LiveData<List<Post>> = _postsLiveData

    init {
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            val posts = getPostsUseCase.invoke().reversed()
            _postsLiveData.postValue(posts)
        }
    }

}