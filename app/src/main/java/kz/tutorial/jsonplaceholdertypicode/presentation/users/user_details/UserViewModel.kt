package kz.tutorial.jsonplaceholdertypicode.presentation.users.user_details

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.domain.entity.User
import kz.tutorial.jsonplaceholdertypicode.domain.use_case.GetUserUseCase

class UserViewModel(private val getUserUseCase: GetUserUseCase, private val id:Int): ViewModel() {

    private val _userDetailsLiveData: MutableLiveData<User> = MutableLiveData()
    val userDetailsLiveData: LiveData<User> = _userDetailsLiveData
    private lateinit var  address: Uri

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            val user = getUserUseCase.invoke(id)

            if (user != null ) {
//                val geo = user.address?.geo
//                val userAddress = user.address
//                address = Uri.parse("geo:" + geo?.lat + "," + geo?.lng + "?q=" + Uri.encode(userAddress?.street + ", " + userAddress?.city + ", " + userAddress?.zipcode))
            } else {
            }
        }
    }
}