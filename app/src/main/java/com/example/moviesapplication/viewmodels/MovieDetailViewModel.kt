package com.example.moviesapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.moviedetail.MovieDetailModel
import com.example.moviesapplication.models.movieslist.Movies
import com.example.moviesapplication.repositories.MoviesRepository
import com.example.moviesapplication.utils.NetworkHelper

class MovieDetailViewModel(private val app: Application) : AndroidViewModel(app) {

    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    private lateinit var movie: MutableLiveData<MovieDetailModel?>
    private var mRepository = MoviesRepository.getInstance()


    fun getMovie(forceFetch: Boolean, movieId: Int): MutableLiveData<MovieDetailModel?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            movie = mRepository.getMovieDetail(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }
                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }
            }, forceFetch, movieId)

        } else {
            mShowNetworkError.value = true
        }
        return movie
    }

}