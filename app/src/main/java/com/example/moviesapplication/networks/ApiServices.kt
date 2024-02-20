import com.example.moviesapplication.models.movieslist.Movies

import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @GET("api/v1/movies")
    fun getMovies(@Query("page") page: Int): Call<Movies>


}