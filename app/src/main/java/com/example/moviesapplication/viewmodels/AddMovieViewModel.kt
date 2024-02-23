package com.example.moviesapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.addmovie.AddMovieModel
import com.example.moviesapplication.models.addmovie.MovieInput

import com.example.moviesapplication.repositories.MoviesRepository
import com.example.moviesapplication.utils.NetworkHelper
import java.io.File

class AddMovieViewModel(private val app: Application) : AndroidViewModel(app) {
    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    private lateinit var addMovie: MutableLiveData<AddMovieModel?>
    private var mRepository = MoviesRepository.getInstance()

    fun addMovie(movieInput: MovieInput): MutableLiveData<AddMovieModel?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            addMovie = mRepository.addMovie(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

            }, movieInput)
        } else {
            mShowNetworkError.value = true
        }
        return addMovie
    }


}