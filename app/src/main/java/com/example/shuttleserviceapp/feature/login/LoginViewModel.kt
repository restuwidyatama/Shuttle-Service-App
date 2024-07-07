package com.example.shuttleserviceapp.feature.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shuttleserviceapp.database.entity.User
import com.example.shuttleserviceapp.database.entity.UserActive
import com.example.shuttleserviceapp.repository.UserActiveRepository
import com.example.shuttleserviceapp.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel (application: Application) : AndroidViewModel(application){
    private val repository: UserRepository
    private val userActiverepository: UserActiveRepository

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    init {
        repository = UserRepository(application)
        userActiverepository = UserActiveRepository(application)
    }

    fun checkUser(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.checkLogin(username, password)

            if (user != null) {
                val userActive = UserActive(
                    userId = user.userId,
                    name =  user.name,
                    username = user.username,
                    password = user.password,
                    umur = user.umur,
                    role = user.role
                )

                userActiverepository.insert(userActive)
                _loginResult.postValue(true)
            } else {
                // Jika user tidak ditemukan, login gagal
                _loginResult.postValue(false)
            }
        }
    }
}