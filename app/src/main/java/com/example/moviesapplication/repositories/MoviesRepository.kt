package com.example.moviesapplication.repositories

import androidx.lifecycle.MutableLiveData
import com.example.moviesapplication.interfaces.NetworkResponseCallback
import com.example.moviesapplication.models.addmovie.AddMovieModel
import com.example.moviesapplication.models.addmovie.MovieInput
import com.example.moviesapplication.models.genre.GenreModel
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

    private var movies: MutableLiveData<Movies?> = MutableLiveData<Movies?>()
    private lateinit var movieCall: Call<Movies>

    private var movieDetail: MutableLiveData<MovieDetailModel?> = MutableLiveData<MovieDetailModel?>()
    private lateinit var movieDetailCall: Call<MovieDetailModel>

    private var genres: MutableLiveData<List<GenreModel?>?> = MutableLiveData<List<GenreModel?>?>()
    private lateinit var genreCall: Call<List<GenreModel>>

    private var genresMovie: MutableLiveData<Movies?> = MutableLiveData<Movies?>()
    private lateinit var genresMovieCall: Call<Movies>


    private var addMovie: MutableLiveData<AddMovieModel?> = MutableLiveData<AddMovieModel?>()
    private lateinit var addMovieCall: Call<AddMovieModel>

    fun getMovies(callback: NetworkResponseCallback, forceFetch: Boolean, page: Int): MutableLiveData<Movies?> {
        mCallback = callback
        if (movies.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return movies
        }
        movieCall = RestClient.getInstance().getApiService().getMovies(page)
        movieCall.enqueue(object : Callback<Movies> {

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                movies.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                movies.value = null
                mCallback.onResponseFailure(t)
            }

        })
        return movies
    }


    fun searchMovies(
        callback: NetworkResponseCallback,
        forceFetch: Boolean,
        movieName: String,
        page: Int
    ): MutableLiveData<Movies?> {
        mCallback = callback
        if (movies.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return movies
        }

        movieCall = RestClient.getInstance().getApiService().searchMovies(movieName, page)
        movieCall.enqueue(object : Callback<Movies> {

            override fun onResponse(p0: Call<Movies>, response: Response<Movies>) {
                movies.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<Movies>, t: Throwable) {
                movies.value = null
                mCallback.onResponseFailure(t)
            }

        })

        return movies
    }

    fun getMovieDetail(
        callback: NetworkResponseCallback,
        forceFetch: Boolean,
        movieId: Int
    ): MutableLiveData<MovieDetailModel?> {
        mCallback = callback
        if (movieDetail.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return movieDetail
        }

        movieDetailCall = RestClient.getInstance().getApiService().getMoviesDetail(movieId)
        movieDetailCall.enqueue(object : Callback<MovieDetailModel> {

            override fun onResponse(p0: Call<MovieDetailModel>, response: Response<MovieDetailModel>) {
                movieDetail.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<MovieDetailModel>, t: Throwable) {
                movieDetail.value = null
                mCallback.onResponseFailure(t)
            }

        })

        return movieDetail
    }


    fun getGenres(callback: NetworkResponseCallback): MutableLiveData<List<GenreModel?>?> {
        mCallback = callback

        if (genres.value != null) {
            mCallback.onResponseSuccess()
            return genres;
        }
        genreCall = RestClient.getInstance().getApiService().getGenres()
        genreCall.enqueue(object : Callback<List<GenreModel>?> {
            override fun onResponse(p0: Call<List<GenreModel>?>, response: Response<List<GenreModel>?>) {
                genres.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<List<GenreModel>?>, t: Throwable) {
                movieDetail.value = null
                mCallback.onResponseFailure(t)
            }

        })

        return genres;
    }


    fun getGenresMovie(
        callback: NetworkResponseCallback,
        forceFetch: Boolean,
        genreId: Int,
        page: Int
    ): MutableLiveData<Movies?> {
        mCallback = callback
        if (genresMovie.value != null && !forceFetch) {
            mCallback.onResponseSuccess()
            return genresMovie
        }

        genresMovieCall = RestClient.getInstance().getApiService().getGenresMovie(genreId, page)
        genresMovieCall.enqueue(object : Callback<Movies?> {
            override fun onResponse(p0: Call<Movies?>, response: Response<Movies?>) {
                genresMovie.value = response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<Movies?>, t: Throwable) {
                genresMovie.value = null
                mCallback.onResponseFailure(t)
            }

        })
        return genresMovie;
    }


    fun addMovie(callback: NetworkResponseCallback, movieInput: MovieInput): MutableLiveData<AddMovieModel?> {
        mCallback = callback
        if (addMovie.value != null) {
            mCallback.onResponseSuccess()
            return addMovie
        }
        var file: RequestBody? =null
        if(movieInput.poster!=null)
            file = RequestBody.create(MediaType.parse("image/*"), movieInput.poster!!)

        addMovieCall = RestClient.getInstance().getApiService().addMovie(
            movieInput.title,
            movieInput.imdb_id,
            movieInput.country,
            movieInput.year,
            movieInput.director.toString(),
            movieInput.imdb_votes.toString(),
            movieInput.imdb_rating.toString(),
            file
        )

        addMovieCall.enqueue(object:Callback<AddMovieModel>{
            override fun onResponse(p0: Call<AddMovieModel>, response: Response<AddMovieModel>) {
                addMovie.value=response.body()
                mCallback.onResponseSuccess()
            }

            override fun onFailure(p0: Call<AddMovieModel>, t: Throwable) {
                addMovie.value=null
                mCallback.onResponseFailure(t)
            }

        } )
        return addMovie
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