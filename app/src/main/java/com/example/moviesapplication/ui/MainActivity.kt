package com.example.moviesapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var moviesViewModel: MoviesViewModel
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        binding.viewModel = moviesViewModel
        binding.lifecycleOwner = this
        initRecyclerView()
        initializeObservers()
    }


    fun initRecyclerView() {
        moviesAdapter = MoviesAdapter()
        binding.rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = moviesAdapter
        }
    }

    fun initializeObservers() {
        moviesViewModel.getMovies(false).observe(this, Observer { movie ->
            moviesAdapter.setData(movie?.data!!, this@MainActivity)
        })
//        moviesViewModel.mShowApiError.observe(this) {
//            AlertDialog.Builder(this).setMessage(it).show()
//        }
//        moviesViewModel.mShowProgressBar.observe(this) { bt ->
//            if (bt) {
//                binding.progressBar.visibility = View.VISIBLE
//                binding.fabRefresh.hide()
//            } else {
//                binding.progressBar.visibility = View.GONE
//                binding.fabRefresh.show()
//            }
//        }
//        moviesViewModel.mShowNetworkError.observe(this) {
//            AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
//        }

    }
}