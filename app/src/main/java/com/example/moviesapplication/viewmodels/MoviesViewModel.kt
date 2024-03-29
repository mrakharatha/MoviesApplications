package com.example.moviesapplication.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.genre.GenreModel
import com.example.moviesapplication.models.movieslist.Movies
import com.example.moviesapplication.repositories.MoviesRepository
import com.example.moviesapplication.utils.NetworkHelper

class MoviesViewModel(private val app: Application) : AndroidViewModel(app) {

    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()

    private lateinit var movies: MutableLiveData<Movies?>
    private lateinit var genres: MutableLiveData<List<GenreModel?>?>
    private lateinit var genresMovie: MutableLiveData<Movies?>
    private var mRepository = MoviesRepository.getInstance()

    fun getMovies(forceFetch: Boolean, page: Int): MutableLiveData<Movies?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            movies = mRepository.getMovies(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

            }, forceFetch, page)
        } else {
            mShowNetworkError.value = true
        }
        return movies
    }


    fun searchMovies(forceFetch: Boolean, movieName: String, page: Int): MutableLiveData<Movies?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            movies = mRepository.searchMovies(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }
            }, forceFetch, movieName, page)
        } else {
            mShowNetworkError.value = true
        }

        return movies
    }

    fun getGenres(): MutableLiveData<List<GenreModel?>?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            genres = mRepository.getGenres(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }
            })

        } else {
            mShowNetworkError.value = true
        }

        return genres
    }


    fun getGenresMovie(forceFetch: Boolean, genreId: Int, page: Int): MutableLiveData<Movies?> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            genresMovie = mRepository.getGenresMovie(object : NetworkResponseCallback {
                override fun onResponseSuccess() {
                    mShowProgressBar.value = false
                }

                override fun onResponseFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

            }, forceFetch, genreId, page)
        } else {
            mShowNetworkError.value = true
        }
        return genresMovie
    }

    fun onRefreshClicked(view: View) {
        getMovies(true, 1)
    }


}