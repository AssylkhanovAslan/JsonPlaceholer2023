package kz.tutorial.jsonplaceholdertypicode.presentation.show_all_comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Comment
import kz.tutorial.jsonplaceholdertypicode.domain.use_case.GetShowAllCommentsUseCase

class ShowAllViewModel(
    private val postId: Int,
    private val getAllCommentsUseCase: GetShowAllCommentsUseCase,
) : ViewModel() {
    private val _commentsLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
    val commentsLiveData: LiveData<List<Comment>> = _commentsLiveData

    init {
        getPostAllComments()
    }

    private fun getPostAllComments() {
        viewModelScope.launch {
            val postComments = getAllCommentsUseCase.invoke(postId)
            _commentsLiveData.value = postComments
        }
    }
}