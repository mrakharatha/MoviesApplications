package com.example.moviesapplication.ui.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapplication.R
import com.example.moviesapplication.databinding.ActivityRegisterUserBinding
import com.example.moviesapplication.models.registeruser.RegisterUserInput
import com.example.moviesapplication.viewmodels.UserViewModel

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityRegisterUserBinding
    private var userInput = RegisterUserInput()
    private lateinit var context: Context

    private var email = ""
    private var password = ""
    private var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_user)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.viewModel = userViewModel
        binding.lifecycleOwner = this
        context = this@RegisterUserActivity

        listener()
    }


    private fun listener() {

        binding.btRegister.setOnClickListener {
            email = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()
            name = binding.etName.text.toString()
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(context, "همه فیلد ها را وارد کنید", Toast.LENGTH_SHORT).show()
            } else {
                userInput = RegisterUserInput(email, password, name)
                userViewModel.registerUser(userInput).observe(this, Observer { user ->
                    Toast.makeText(context, "id: ${user?.id.toString()} \n email : ${user?.email}", Toast.LENGTH_SHORT)
                        .show()

                    finish()
                })

                userViewModel.mShowApiError.observe(this) {
                    AlertDialog.Builder(context).setMessage(it).show()
                }
                userViewModel.mShowProgressBar.observe(this) { bt ->
                    if (bt) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                userViewModel.mShowNetworkError.observe(this) {
                    AlertDialog.Builder(context).setMessage(R.string.app_no_internet_msg).show()
                }
            }

        }
    }
}