import com.example.moviesapplication.models.addmovie.AddMovieModel
import com.example.moviesapplication.models.genre.GenreModel
import com.example.moviesapplication.models.moviedetail.MovieDetailModel
import com.example.moviesapplication.models.movieslist.Movies
import com.example.moviesapplication.models.registeruser.RegisterUserInput
import com.example.moviesapplication.models.registeruser.RegisterUserModel
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*
import java.io.File

interface ApiServices {

    @GET("api/v1/movies")
    fun getMovies(@Query("page") page: Int): Call<Movies>


    @GET("api/v1/movies")
    fun searchMovies(@Query("q") movieName:String, @Query("page") page: Int): Call<Movies>

    @GET("api/v1/movies/{movie_id}")
    fun getMoviesDetail(@Path("movie_id") id: Int): Call<MovieDetailModel>


    @GET("api/v1/genres")
    fun getGenres(): Call<List<GenreModel>>

    @GET("api/v1/genres/{genre_id}/movies")
    fun getGenresMovie(@Path("genre_id") genreId:Int, @Query("page") page: Int): Call<Movies>

    @POST("api/v1/movies/multi")
    @Multipart
    fun addMovie(
        @Part("title") title:String,
        @Part("imdb_id") imdb_id:String,
        @Part("country") country:String,
        @Part("year") year:Int,
        @Part("director") director:String?,
        @Part("imdb_rating") imdb_rating:String?,
        @Part("imdb_votes") imdb_votes:String?,
        @Part("poster") poster:RequestBody?,
    ):Call<AddMovieModel>

    @POST("api/v1/register")
    fun registerUser(@Body registerUser:RegisterUserInput):Call<RegisterUserModel>

}