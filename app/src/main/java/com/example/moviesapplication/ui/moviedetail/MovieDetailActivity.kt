package com.example.moviesapplication.ui.moviedetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesapplication.R
import com.example.moviesapplication.databinding.ActivityMainBinding
import com.example.moviesapplication.databinding.ActivityMovieDetailBinding
import com.example.moviesapplication.models.moviedetail.MovieDetailModel
import com.example.moviesapplication.viewmodels.MovieDetailViewModel
import com.example.moviesapplication.viewmodels.MoviesViewModel
import java.util.Observer

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private lateinit var binding: ActivityMovieDetailBinding
    private var movieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = intent.getIntExtra("movieId", 0)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        binding.viewModel = movieDetailViewModel
        binding.lifecycleOwner = this
        initializeObservers()

    }

    private fun initializeObservers() {
        binding.progressBar.visibility = View.VISIBLE
        movieDetailViewModel.getMovie(true, movieId).observe(this) { movie ->

                 Glide.with(this@MovieDetailActivity)
                .load(movie?.poster)
                     .into(binding.ivPoster)


            Glide.with(this@MovieDetailActivity)
                .load(movie?.images?.get(0))
                .into(binding.ivOne)

            Glide.with(this@MovieDetailActivity)
                .load(movie?.images?.get(1))
                .into(binding.ivTwo)

            Glide.with(this@MovieDetailActivity)
                .load(movie?.images?.get(2))
                .into(binding.ivThree)


            binding.tvTitle.text=movie?.title
            binding.tvYear.text=movie?.year
            binding.tvRated.text=movie?.rated
            binding.tvReleased.text=movie?.released
            binding.tvRunTime.text=movie?.runtime
            binding.tvDirector.text=movie?.director
            binding.tvWriter.text=movie?.writer
            binding.tvActor.text=movie?.actors
            binding.tvPlot.text=movie?.plot
            binding.tvCountry.text=movie?.country
            binding.tvAwards.text=movie?.awards
            binding.tvMetaScore.text=movie?.metascore
            binding.tvImdbRating.text=movie?.imdb_rating
            binding.tvImdbVotes.text=movie?.imdb_votes
            binding.tvImdbId.text=movie?.imdb_id
            binding.tvType.text=movie?.type
            binding.tvGenres.text=movie?.genres?.toString()
        }


        movieDetailViewModel.mShowApiError.observe(this) {
            AlertDialog.Builder(this).setMessage(it).show()
        }
        movieDetailViewModel.mShowProgressBar.observe(this) { bt ->
            if (bt) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        movieDetailViewModel.mShowNetworkError.observe(this) {
            AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}