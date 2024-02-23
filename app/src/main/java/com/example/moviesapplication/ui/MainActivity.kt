package com.example.moviesapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapplication.adapters.MoviesAdapter
import com.example.moviesapplication.databinding.ActivityMainBinding
import com.example.moviesapplication.viewmodels.MoviesViewModel
import com.example.moviesapplication.R
import com.example.moviesapplication.adapters.GenresAdapter
import com.example.moviesapplication.models.genre.GenreModel
import com.example.moviesapplication.ui.addmovie.AddMovieActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var moviesAdapter: MoviesAdapter
    lateinit var genresAdapter: GenresAdapter
    private lateinit var moviesViewModel: MoviesViewModel
    var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        binding.viewModel = moviesViewModel
        binding.lifecycleOwner = this
        initRecyclerView()
        initializeObservers()
        listener()
    }


    private fun initRecyclerView() {
        moviesAdapter = MoviesAdapter()
        genresAdapter = GenresAdapter()

        binding.rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = moviesAdapter
        }

        binding.rvGenres.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = genresAdapter
        }
    }

    private fun initializeObservers() {

        moviesViewModel.getGenresMovie(false, genreId, page)
            .observe(this, Observer { movie ->
                moviesAdapter.setData(movie?.data!!, this@MainActivity)
            })

        moviesViewModel.getMovies(false,1).observe(this, Observer { movie ->
            moviesAdapter.setData(movie?.data!!, this@MainActivity)
        })

        moviesViewModel.getGenres().observe(this, Observer { genre ->
            genresAdapter.setData(genre as List<GenreModel>, this@MainActivity, moviesViewModel)
        })

        moviesViewModel.mShowApiError.observe(this) {
            AlertDialog.Builder(this).setMessage(it).show()
        }
        moviesViewModel.mShowProgressBar.observe(this) { bt ->
            if (bt) {
                binding.progressBar.visibility = View.VISIBLE
                binding.fabRefresh.hide()
            } else {
                binding.progressBar.visibility = View.GONE
                binding.fabRefresh.show()
            }
        }
        moviesViewModel.mShowNetworkError.observe(this) {
            AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
        }


    }

    private fun listener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrEmpty() && text.length > 2) {
                    searchText = text.toString()
                    moviesViewModel.searchMovies(true, searchText, page)
                        .observe(this@MainActivity) { movie ->
                            moviesAdapter.setData(movie?.data!!, this@MainActivity)
                        }

                    moviesViewModel.mShowApiError.observe(this@MainActivity) {
                        AlertDialog.Builder(this@MainActivity).setMessage(it).show()
                    }
                    moviesViewModel.mShowProgressBar.observe(this@MainActivity) { bt ->
                        if (bt) {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.fabRefresh.hide()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.fabRefresh.show()
                        }
                    }
                    moviesViewModel.mShowNetworkError.observe(this@MainActivity) {
                        AlertDialog.Builder(this@MainActivity).setMessage(R.string.app_no_internet_msg).show()
                    }

                } else {
                    moviesViewModel.getMovies(true, page)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.fabAddMovie.setOnClickListener{
            val intent=Intent(this@MainActivity,AddMovieActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        var genreId: Int = 0
        var page: Int = 0
    }
}