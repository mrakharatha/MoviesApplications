package com.example.moviesapplication.ui.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapplication.R
import com.example.moviesapplication.databinding.ActivityLoginBinding
import com.example.moviesapplication.ui.MainActivity
import com.example.moviesapplication.viewmodels.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityLoginBinding

    private var username = ""
    private var password = ""
    private lateinit var content: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.viewModel = userViewModel
        binding.lifecycleOwner = this
        content = this@LoginActivity
        listener()
    }


    private fun listener() {
        binding.btLogin.setOnClickListener {
            username = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(content, "نام کاربری و رمز عبور را وارد کنید", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.loginUser(username, password).observe(this, Observer { login ->
                    if (login?.access_token != null) {

                        val sharedPreferences: SharedPreferences = content.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("PREFS_AUTH_ACCESSES_TOKEN", login.access_token.toString())
                        editor.putString("AUTH_REFRESH_TOKEN", login.refresh_token.toString())
                        editor.apply()
                        finish()

                        val intent = Intent(content, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(content, "ورود با خطا مواجه شد", Toast.LENGTH_LONG).show()
                    }
                })

                userViewModel.mShowApiError.observe(this, Observer {
                    AlertDialog.Builder(this).setMessage(it).show()
                })
                userViewModel.mShowProgressBar.observe(this, Observer { bt ->
                    if (bt) {
                        binding.progressBar.visibility = View.VISIBLE

                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                })
                userViewModel.mShowNetworkError.observe(this, Observer {
                    AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
                })
            }
        }
        binding.tvRegister.setOnClickListener {
            finish()
            val intent = Intent(this@LoginActivity, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }
}