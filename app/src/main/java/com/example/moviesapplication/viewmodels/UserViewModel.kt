package com.example.moviesapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.loginuser.UserLoginModel
import com.example.moviesapplication.models.registeruser.RegisterUserInput
import com.example.moviesapplication.models.registeruser.RegisterUserModel
import com.example.moviesapplication.models.user.UserModel
import com.example.moviesapplication.repositories.MoviesRepository
import com.example.moviesapplication.repositories.UsersRepository
import com.example.moviesapplication.utils.NetworkHelper

class UserViewModel(private val app: Application) : AndroidViewModel(app) {
    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    private lateinit var registerUser: MutableLiveData<RegisterUserModel?>
    private lateinit var userLogin: MutableLiveData<UserLoginModel?>
    private lateinit var user: MutableLiveData<UserModel?>
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

    fun loginUser(userName: String, password: String): MutableLiveData<UserLoginModel?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            userLogin = mRepository.loginUser(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

            }, userName, password)
        } else {
            mShowNetworkError.value = true
        }

        return userLogin
    }

    fun getUser(token: String): MutableLiveData<UserModel?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            user = mRepository.getUser(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

            }, token)
        } else {
            mShowNetworkError.value = true
        }

        return user
    }
}