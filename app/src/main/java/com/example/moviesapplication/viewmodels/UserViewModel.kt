package com.example.moviesapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.registeruser.RegisterUserInput
import com.example.moviesapplication.models.registeruser.RegisterUserModel
import com.example.moviesapplication.repositories.MoviesRepository
import com.example.moviesapplication.repositories.UsersRepository
import com.example.moviesapplication.utils.NetworkHelper

class UserViewModel(private val app: Application) : AndroidViewModel(app) {
    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    private lateinit var registerUser: MutableLiveData<RegisterUserModel?>
    private var mRepository = UsersRepository.getInstance()


    fun registerUser(userInput: RegisterUserInput): MutableLiveData<RegisterUserModel?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            registerUser = mRepository.registerUser(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

            }, userInput)
        } else {
            mShowNetworkError.value = true
        }
        return registerUser
    }
}