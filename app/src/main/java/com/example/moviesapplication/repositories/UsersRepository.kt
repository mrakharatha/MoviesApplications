package com.example.moviesapplication.repositories

import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.loginuser.UserLoginModel
import com.example.moviesapplication.models.movieslist.Movies
import com.example.moviesapplication.models.registeruser.RegisterUserInput
import com.example.moviesapplication.models.registeruser.RegisterUserModel
import com.example.moviesapplication.models.user.UserModel
import com.example.moviesapplication.networks.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback

    private var registerUser: MutableLiveData<RegisterUserModel?> = MutableLiveData<RegisterUserModel?>()
    private lateinit var registerUserCall: Call<RegisterUserModel>


    private var userLogin: MutableLiveData<UserLoginModel?> = MutableLiveData<UserLoginModel?>()
    private lateinit var userLoginCall: Call<UserLoginModel>


    private var user: MutableLiveData<UserModel?> = MutableLiveData<UserModel?>()
    private lateinit var userCall: Call<UserModel>

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


    fun loginUser(
        callback: NetworkResponseCallback,
        username: String,
        password: String
    ): MutableLiveData<UserLoginModel?> {
        mCallback = callback
        if (userLogin.value != null) {
            mCallback.onResponseSuccess()
            return userLogin
        }

        userLoginCall = RestClient.getInstance().getApiService().loginUser("password", username, password)
        userLoginCall.enqueue(object : Callback<UserLoginModel> {
            override fun onResponse(p0: Call<UserLoginModel>, response: Response<UserLoginModel>) {
                userLogin.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<UserLoginModel>, th: Throwable) {
                userLogin.value = null
                mCallback.onResponseFailure(th)
            }

        })

        return userLogin
    }


    fun getUser(callback: NetworkResponseCallback,token:String):MutableLiveData<UserModel?>{
        mCallback = callback
        if (user.value!=null){
            mCallback.onResponseSuccess()
            return user
        }

        userCall=RestClient.getInstance().getApiService().getUser("Bearer ${token.toString()}","application/json")
        userCall.enqueue(object :Callback<UserModel>{
            override fun onResponse(p0: Call<UserModel>, response: Response<UserModel>) {
                user.value=response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<UserModel>, th: Throwable) {
                user.value=null
                mCallback.onResponseFailure(th)
            }
        })

        return user
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