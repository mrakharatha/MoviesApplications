package com.example.moviesapplication.repositories

import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.moviedetail.MovieDetailModel
import com.example.moviesapplication.models.movieslist.Movies
import com.example.moviesapplication.networks.RestClient
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviesRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback


    private var mMovie: MutableLiveData<Movies?> = MutableLiveData<Movies?>()
    private lateinit var movieCall: Call<Movies>


    private var mMovieDetail: MutableLiveData<MovieDetailModel?> = MutableLiveData<MovieDetailModel?>()
    private lateinit var movieDetailCall: Call<MovieDetailModel>

    fun getMovies(callback: NetworkResponseCallback, forceFetch: Boolean,page: Int): MutableLiveData<Movies?> {
        mCallback = callback
        if (mMovie.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return mMovie
        }
        movieCall = RestClient.getInstance().getApiService().getMovies(page)
        movieCall.enqueue(object : Callback<Movies> {

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                mMovie.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                mMovie.value = null
                mCallback.onResponseFailure(t)
            }

        })
        return mMovie
    }


    fun searchMovies(callback: NetworkResponseCallback, forceFetch: Boolean,movieName:String,page:Int):MutableLiveData<Movies?>{
        mCallback=callback
        if (mMovie.value!=null&&!forceFetch){
            mCallback.onResponseSuccess()
            return mMovie
        }

        movieCall=RestClient.getInstance().getApiService().searchMovies(movieName,page)
        movieCall.enqueue(object :Callback<Movies>{

            override fun onResponse(p0: Call<Movies>, response: Response<Movies>) {
                mMovie.value=response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<Movies>, t: Throwable) {
                mMovie.value = null
                mCallback.onResponseFailure(t)
            }

        })

        return mMovie
    }

    fun getMovieDetail(callback: NetworkResponseCallback, forceFetch: Boolean,movieId:Int) :MutableLiveData<MovieDetailModel?>{
        mCallback=callback
        if (mMovieDetail.value!=null&&!forceFetch){
            mCallback.onResponseSuccess()
            return mMovieDetail
        }

        movieDetailCall = RestClient.getInstance().getApiService().getMoviesDetail(movieId)
        movieDetailCall.enqueue(object : Callback<MovieDetailModel>{

            override fun onResponse(p0: Call<MovieDetailModel>, response: Response<MovieDetailModel>) {
                mMovieDetail.value=response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<MovieDetailModel>, t: Throwable) {
                mMovieDetail.value=null
                mCallback.onResponseFailure(t)
            }

        })

        return mMovieDetail
    }


    companion object {
        private var mInstance: MoviesRepository? = null
        fun getInstance(): MoviesRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = MoviesRepository()
                }
            }
            return mInstance!!
        }
    }

}