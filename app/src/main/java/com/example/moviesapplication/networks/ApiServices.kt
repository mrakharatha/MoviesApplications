import com.example.moviesapplication.models.moviedetail.MovieDetailModel
import com.example.moviesapplication.models.movieslist.Movies

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ApiServices {

    @GET("api/v1/movies")
    fun getMovies(@Query("page") page: Int): Call<Movies>


    @GET("api/v1/movies")
    fun searchMovies(@Query("q") movieName:String, @Query("page") page: Int): Call<Movies>

    @GET("api/v1/movies/{movie_id}")
    fun getMoviesDetail(@Path("movie_id") id: Int): Call<MovieDetailModel>
}