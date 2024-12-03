package kz.tutorial.jsonplaceholdertypicode.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Post
import kz.tutorial.jsonplaceholdertypicode.domain.entity.User
import kz.tutorial.jsonplaceholdertypicode.domain.use_case.GetUsersUseCase

class UsersViewModel(private val getUsersUseCase:GetUsersUseCase):ViewModel() {
    private val _usersLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val usersLiveData: LiveData<List<User>> = _usersLiveData

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            val users = getUsersUseCase.invoke()
            _usersLiveData.postValue(users)
        }
    }
}