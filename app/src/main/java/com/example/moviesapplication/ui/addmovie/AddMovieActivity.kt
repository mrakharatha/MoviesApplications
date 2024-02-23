package com.example.moviesapplication.ui.addmovie

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapplication.R
import com.example.moviesapplication.databinding.ActivityAddMovieBinding
import com.example.moviesapplication.models.addmovie.MovieInput
import com.example.moviesapplication.ui.MainActivity
import com.example.moviesapplication.viewmodels.AddMovieViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddMovieActivity : AppCompatActivity() {
    private lateinit var addMovieViewModel: AddMovieViewModel
    private lateinit var binding: ActivityAddMovieBinding
    private var PIC_IMAGE_MULTIPLE = 100
    private lateinit var file: File
    private var poster: File? = null
    private lateinit var context: Context

    private var title = ""
    private var imdb_id = ""
    private var country = ""
    private var year = ""
    private var director = ""
    private var imdb_rating = ""
    private var imdb_votes = ""
    private var isValid = false

    private var movieInput: MovieInput = MovieInput()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_movie)
        addMovieViewModel = ViewModelProvider(this).get(AddMovieViewModel::class.java)
        binding.viewModel = addMovieViewModel
        binding.lifecycleOwner = this
        context = this@AddMovieActivity
        listener()
    }

    private fun getImage() {
        if (Build.VERSION.SDK_INT < 19) {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.ACTION_OPEN_DOCUMENT, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Pic"), PIC_IMAGE_MULTIPLE
            )
        } else {
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            getResult.launch(intent)
        }
    }


    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.data != null) {
                val selectImage: Uri = it.data?.data!!
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                file = File(context!!.cacheDir, "Poster Image")
                file.createNewFile()
                val bos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bos)
                val bitmapData: ByteArray = bos.toByteArray()
                val fos = FileOutputStream(file)
                fos.write(bitmapData)
                fos.flush()
                fos.close()
                poster = file
                binding.ivPoster.setImageBitmap(bitmap)
            }
        }
    }

    private fun listener() {
        binding.ivPoster.setOnClickListener {
            getImage()
        }

        binding.btSave.setOnClickListener {
            isValid = true

            title = binding.etName.text.toString()
            imdb_id = binding.etImdbId.text.toString()
            country = binding.etCountry.text.toString()
            year = binding.etYear.text.toString()
            director = binding.etDirector.text.toString()
            imdb_rating = binding.etImdbRating.text.toString()
            imdb_votes = binding.etImdbVotes.text.toString()

            if (title.isEmpty()) {
                isValid = false
                Toast.makeText(context, "نام فیلم اجباری است", Toast.LENGTH_SHORT).show()
            }

            if (imdb_id.isEmpty()) {
                isValid = false
                Toast.makeText(context, "شناسه ی فیلم اجباری است", Toast.LENGTH_SHORT).show()
            }

            if (country.isEmpty()) {
                isValid = false
                Toast.makeText(context, "کشور سازنده ی فیلم اجباری است", Toast.LENGTH_SHORT).show()
            }
            if (year.isEmpty()) {
                isValid = false
                Toast.makeText(context, "سال ساخت فیلم اجباری است", Toast.LENGTH_SHORT).show()
            }

            if (isValid) {
                movieInput =
                    MovieInput(title, imdb_id, country, year.toInt(), director, imdb_rating, imdb_votes, poster)
                addMovieViewModel.addMovie(movieInput).observe(this) {
                    Toast.makeText(context, "فیلم با موفقیت ثبت شد", Toast.LENGTH_SHORT).show()
                    finish()
                }

                addMovieViewModel.mShowApiError.observe(this) {
                    AlertDialog.Builder(this).setMessage(it).show()
                }
                addMovieViewModel.mShowProgressBar.observe(this) { bt ->
                    if (bt) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                addMovieViewModel.mShowNetworkError.observe(this) {
                    AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
                }

            }

        }
    }
}