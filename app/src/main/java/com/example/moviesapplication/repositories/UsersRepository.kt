package com.example.moviesapplication.repositories

import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.movieslist.Movies
import com.example.moviesapplication.models.registeruser.RegisterUserInput
import com.example.moviesapplication.models.registeruser.RegisterUserModel
import com.example.moviesapplication.networks.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback

    private var registerUser: MutableLiveData<RegisterUserModel?> = MutableLiveData<RegisterUserModel?>()
    private lateinit var registerUserCall: Call<RegisterUserModel>


    fun registerUser(
        callback: NetworkResponseCallback,
        userInput: RegisterUserInput
    ): MutableLiveData<RegisterUserModel?> {
        mCallback = callback

        if (registerUser.value != null) {
            mCallback.onResponseSuccess()
            return registerUser
        }

        registerUserCall = RestClient.getInstance().getApiService().registerUser(userInput)
        registerUserCall.enqueue(object : Callback<RegisterUserModel> {
            override fun onResponse(p0: Call<RegisterUserModel>, response: Response<RegisterUserModel>) {
                registerUser.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<RegisterUserModel>, th: Throwable) {
                registerUser.value = null
                mCallback.onResponseFailure(th)
            }

        })
        return registerUser
    }


    companion object {
        private var mInstance: UsersRepository? = null
        fun getInstance(): UsersRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = UsersRepository()
                }
            }
            return mInstance!!
        }
    }
}