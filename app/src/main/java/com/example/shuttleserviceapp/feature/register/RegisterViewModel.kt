package com.example.shuttleserviceapp.feature.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shuttleserviceapp.database.entity.User
import com.example.shuttleserviceapp.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel (application: Application) : AndroidViewModel(application){
    private val repository: UserRepository

    private val _insertLiveData = MutableLiveData<Boolean>()
    val insertLiveData: LiveData<Boolean>
        get() = _insertLiveData

    init {
        repository = UserRepository(application)
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insert(user)
            _insertLiveData.postValue(true)
        }
    }
}