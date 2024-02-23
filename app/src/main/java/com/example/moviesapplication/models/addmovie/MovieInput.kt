package com.example.moviesapplication.models.addmovie

import com.google.gson.annotations.SerializedName
import java.io.File

data class MovieInput(
    @SerializedName("title")
    var title: String,
    @SerializedName("imdb_id")
    var imdb_id: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("year")
    var year: Int,
    @SerializedName("director")
    var director: String?,
    @SerializedName("imdb_rating")
    var imdb_rating: String?,
    @SerializedName("imdb_votes")
    var imdb_votes: String?,
    @SerializedName("poster")
    var poster: File?
){
        constructor():this("","","",0,"","","",null)
}