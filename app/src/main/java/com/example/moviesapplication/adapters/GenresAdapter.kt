package com.example.moviesapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapplication.R
import com.example.moviesapplication.models.genre.GenreModel
import com.example.moviesapplication.models.movieslist.MoviesData

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {


    var genres: List<GenreModel>? = null
    var context: Context? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GenresViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_genres, p0, false)
        return GenresViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (genres != null) {
            genres?.size!!
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: GenresViewHolder, possion: Int) {
        val genre = genres?.get(possion)
        holder.tvName.text = genre?.name.toString()
    }


    fun setData(genresList: List<GenreModel>, context: Context) {
        this.genres = genresList
        this.context = context
        notifyDataSetChanged()
    }

    public class GenresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: AppCompatTextView = itemView.findViewById(R.id.tv_name)
        var lyGenres: ConstraintLayout = itemView.findViewById(R.id.ly_genres)
    }
}